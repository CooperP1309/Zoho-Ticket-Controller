package com.anticipate.listr.embedding.services;

/* spring specific modules */
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.core.io.Resource;
import tools.jackson.databind.ObjectMapper;

/* local modules */
import com.anticipate.listr.embedding.entities.Embeddings;
import com.anticipate.listr.embedding.entities.EmbeddedSkill;
import com.anticipate.listr.embedding.utilities.RequestEscapeUtility;

@Service
public class OllamaClientService 
{

    private final RestClient restClient;

    public OllamaClientService() 
    {
        this.restClient = RestClient.create();
    }

    public float[] getEmbedding(String skill) 
    {
        
        String ollamaURI = "http://localhost:11434/api/embed";
        String requestBody = "{\"model\": \"nomic-embed-text\", \"input\": \"" + RequestEscapeUtility.escapeJsonString(skill) + "\"}";

        return restClient.post()
            .uri(ollamaURI)
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
            .retrieve()
            .body(Embeddings.class)
            .getEmbeddings()[0];
    }
}