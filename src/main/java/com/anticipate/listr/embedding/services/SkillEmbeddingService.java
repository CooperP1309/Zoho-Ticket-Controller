package com.anticipate.listr.embedding.services;

import com.anticipate.listr.embedding.entities.EmbeddedSkill;
import com.anticipate.listr.embedding.entities.Agent; 
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.core.io.Resource;
import tools.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.ArrayList;

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

        System.out.println("\n\n");

        for (Agent agent : agentList) {

            String[] agentSkills = agent.getSkills();

            for (String skill : agentSkills) {
                float[] embeddeding = ollamaClientService.getEmbedding(skill);
                EmbeddedSkill embeddedSkill = new EmbeddedSkill(String.valueOf(agent.getZohoId()), skill, embeddeding);
                embeddedSkills.add(embeddedSkill);

                System.out.println("\nSkill name: " + embeddedSkill.getSkill() + ", zohoId: " + embeddedSkill.getZohoId() + ", Embedding: " + embeddedSkill.embedding[0] + ", " + embeddedSkill.embedding[1] + ", ..., " + embeddedSkill.embedding[767] + "\n");
            }
        }

        System.out.println("\n\n DID WE GET EMBEDDINGS FROM THE ACTUAL LLM SERVER? \n\n");

        //System.out.println("\n\n\nSkillEmbeddingService initialized with " + agentList.length + " agents.\n\n\n");
        //System.out.println("Agent3: " + agentList[2].getName() + ", Zoho ID: " + agentList[2].getZohoId() + ", Skills: " + String.join(", ", agentList[2].getSkills()) + "\n\n\n");

        // Build embedddings

    }

}
