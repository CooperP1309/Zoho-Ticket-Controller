package com.anticipate.listr.embedding.services;

import com.anticipate.listr.embedding.entities.Embeddings;
import com.anticipate.listr.embedding.entities.EmbeddedSkill; 
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
        /*
        Random random = new Random();
        float[] embedding = new float[768];

        for (int i = 0; i < 768; i++) {
            embedding[i] = random.nextFloat();
        }

        return embedding;
        */

        /*
        curl http://localhost:11434/api/embed -d '{
            "model": "nomic-embed-text",
            "input": "Your sample text goes here"
        }'
        */

        /*
            NOTE! Laptop couldn't run llm due to 4gb of ram ;(

            For now, hardcoding ip of an ollama server... TODO: Change this
         */
        String ollamaURI = "http://192.168.1.124:11434/api/embed";
        String requestBody = "{\"model\": \"nomic-embed-text\", \"input\": \"" + skill + "\"}";

        return restClient.post()
            .uri(ollamaURI)
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
            .retrieve()
            .body(Embeddings.class)
            .getEmbeddings()[0];
    }
}
