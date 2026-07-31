package com.anticipate.listr.skill_embedding.services;

import com.anticipate.listr.skill_embedding.entities.Agent; 
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.core.io.Resource;
import tools.jackson.databind.ObjectMapper;

@Service
public class SkillEmbeddingService {

    

    private ObjectMapper objectMapper;

    private Agent[] agentList;

    private final RestClient restClient;

    // Constructor preceeds @Value injection - Hence the constructor injection
    public SkillEmbeddingService(@Value("classpath:agent_skills.json") Resource agentSkills) {
        
        this.objectMapper = new ObjectMapper();

        try {
            this.agentList = objectMapper.readValue(agentSkills.getInputStream(), Agent[].class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }

        this.restClient = RestClient.create();

        System.out.println("\n\n\nSkillEmbeddingService initialized with " + agentList.length + " agents.\n\n\n");
        System.out.println("Agent3: " + agentList[2].getName() + ", Zoho ID: " + agentList[2].getZohoId() + ", Skills: " + String.join(", ", agentList[2].getSkills()) + "\n\n\n");
    }

}
