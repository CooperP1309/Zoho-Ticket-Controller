package com.anticipate.listr.embedding.services;

/* spring specific modules */
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.core.io.Resource;
import tools.jackson.databind.ObjectMapper;

/* java modules */
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/* local modules */
import com.anticipate.listr.embedding.entities.AgentRanking;
import com.anticipate.listr.embedding.entities.EmbeddedSkill;
import com.anticipate.listr.embedding.entities.Agent; 

@Service
public class SkillEmbeddingService {

    private ObjectMapper objectMapper;

    private Agent[] agentList;

    private List<EmbeddedSkill> embeddedSkills = new ArrayList<>();
    
    OllamaClientService ollamaClientService;

    public SkillEmbeddingService(@Value("classpath:agent_skills.json") Resource agentJson) {
        
        // Initialize the agents with skills still as strings
        this.objectMapper = new ObjectMapper();
        try {
            this.agentList = objectMapper.readValue(agentJson.getInputStream(), Agent[].class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }

        ollamaClientService = new OllamaClientService();

        System.out.println("\n[SkillEmbeddingService]   Starting agent skill vectorising...\n");

        for (Agent agent : agentList) {

            String[] agentSkills = agent.getSkills();

            for (String skill : agentSkills) {
                float[] embeddeding = ollamaClientService.getEmbedding(skill);
                EmbeddedSkill embeddedSkill = new EmbeddedSkill(String.valueOf(agent.getZohoId()), skill, embeddeding);
                embeddedSkills.add(embeddedSkill);

                System.out.println("[SkillEmbeddingService]   Skill: " + embeddedSkill.getSkill() + ", zohoId: " + embeddedSkill.getZohoId() + ", Embedding: " + embeddedSkill.embedding[0] + ", " + embeddedSkill.embedding[1] + ", ..., " + embeddedSkill.embedding[767]);
            }
        }

        System.out.println("\n[SkillEmbeddingService]   Agent skill vectorising completed.\n");
    }

    /*
    *   Finds the best agent for a given ticket subject based on skill embeddings.
    *
    *   ticketSubject: The subject of the ticket for which we want to find the best agent.
    *   returns: The Zoho ID of the agent whose skills best match the ticket subject
    *   based on cosine similarity of embeddings.
    */
    public int getSimilarityAgentId(String ticketSubject) {

        // vectorize the ticket subject
        float[] ticketEmbedding = ollamaClientService.getEmbedding(ticketSubject);

        int bestAgentId = -1;
        float bestSimilarity = -1.0f;

        // run similarity search against each embedded skill
        for (EmbeddedSkill embeddedSkill : embeddedSkills) {
            float similarity = cosineSimilarity(ticketEmbedding, embeddedSkill.getEmbedding());
            if (similarity > bestSimilarity) {
                bestSimilarity = similarity;
                bestAgentId = Integer.valueOf(embeddedSkill.getZohoId());
            }
        }

        System.out.println("[SkillEmbeddingService] Best agent ID for ticket subject '" + ticketSubject + "' is: " + bestAgentId + " with similarity: " + bestSimilarity);

        return bestAgentId;
    }

    /*
    *   Finds the best agent for a given ticket subject based on skill embeddings.
    *
    *   Returns agent ranking objects with name, zoho_id, similarity_score, and number_of_tickets.
    */
    public List<AgentRanking> getSimilarityAgentRanking(String ticketSubject) {

        // vectorize the ticket subject
        float[] ticketEmbedding = ollamaClientService.getEmbedding(ticketSubject);

        int bestAgentId = -1;
        float bestSimilarity = -1.0f;
        List<AgentRanking> agentRankings = new ArrayList<>();

        // compile the similarity scores of each agent into a list
        for (EmbeddedSkill embeddedSkill : embeddedSkills) {
            float similarity = cosineSimilarity(ticketEmbedding, embeddedSkill.getEmbedding());
            
            AgentRanking agentRanking = new AgentRanking();
            agentRanking.setZohoId(Integer.valueOf(embeddedSkill.getZohoId()));
            agentRanking.setSimilarityScore((double) similarity);
            agentRanking.setName(getNameFromAgentId(agentRanking.getZohoId()));
            agentRankings.add(agentRanking);
        }

        // sort agent rankings by similarity score
        agentRankings.sort(Comparator.comparingDouble(AgentRanking::getSimilarityScore).reversed());

        // remove duplicate agent names, starting from the lowest skill rankings
        List<AgentRanking> uniqueAgentRankings = new ArrayList<>();
        Set<String> seenAgentNames = new HashSet<>();
        for (AgentRanking agentRanking : agentRankings) {
            if (seenAgentNames.add(agentRanking.getName())) {
                uniqueAgentRankings.add(agentRanking);
            }
        }
        agentRankings = uniqueAgentRankings;

        System.out.println("[SkillEmbeddingService] Best agent ID for ticket subject '" + ticketSubject + "' is: " + agentRankings.get(0).getZohoId() + " with similarity: " + agentRankings.get(0).getSimilarityScore());

        return agentRankings;
    }

    /*
    *   Computes the cosine similarity between two vectors.
    *
    *   ticketVector: The vector representation of the ticket subject.
    *   skillVector: The vector representation of the agent's skill.
    *   returns: The cosine similarity score between the two vectors.
    */
    private static float cosineSimilarity(float[] ticketVector, float[] skillVector) {
    
        if (ticketVector.length != skillVector.length) {
            throw new IllegalArgumentException("Vectors must be of the same length");
        }

        float dotProduct = 0.0f;
        float normTicket = 0.0f;
        float normSkill = 0.0f;

        for (int i = 0; i < ticketVector.length; i++) {
            dotProduct += ticketVector[i] * skillVector[i];
            normTicket += ticketVector[i] * ticketVector[i];
            normSkill += skillVector[i] * skillVector[i];
        }

        if (normTicket == 0.0f || normSkill == 0.0f) {
            return 0.0f;
        }

        return dotProduct / (float) (Math.sqrt(normTicket) * Math.sqrt(normSkill));        
    }

    /*
    *   Retrieves the name of an agent based on their Zoho ID.
    *
    *   agentId: The Zoho ID of the agent whose name is to be retrieved.
    *   returns: The name of the agent if found; otherwise, returns null.
    * 
    *   TODO: Modify getSimilarityAgentId so we can delete this stupid function
    */
    public String getNameFromAgentId(int agentId) {
        for (Agent agent : agentList) {
            if (agent.getZohoId() == agentId) {
                return agent.getName();
            }
        }
        return null;
    }
}
