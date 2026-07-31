package com.anticipate.listr.skill_embedding.services;

import com.anticipate.listr.skill_embedding.entities.EmbeddedSkill; 
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.core.io.Resource;
import tools.jackson.databind.ObjectMapper;
import java.util.Random;

@Service
public class OllamaClientService {

    private final RestClient restClient;

    public OllamaClientService() {
        this.restClient = RestClient.create();
    }

    public float[] getEmbedding(String skill) {
        Random random = new Random();
        float[] embedding = new float[768];

        for (int i = 0; i < 768; i++) {
            embedding[i] = random.nextFloat();
        }

        return embedding;
        /*
        String url = "http://localhost:11434/embeddings";
        String requestBody = "{\"model\": \"ollama/embedding-model\", \"input\": \"" + skill + "\"}";

        try {
            String response = restClient.post(url, requestBody, MediaType.APPLICATION_JSON);
            // Assuming the response is a JSON object with an "embedding" field
            ObjectMapper objectMapper = new ObjectMapper();
            EmbeddedSkill embeddedSkill = objectMapper.readValue(response, EmbeddedSkill.class);
            return embeddedSkill.getEmbedding();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get embedding from Ollama API", e);
        }
        */
    }



}
