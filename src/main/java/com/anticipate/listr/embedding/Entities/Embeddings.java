package com.anticipate.listr.embedding.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

// A quick class to JSON match response from Ollama API
public class Embeddings {

    public Embeddings() {
    }

    // /api/embed returns a 2D array of floats
    @JsonProperty("embeddings")
    private float[][] embeddings;

    public float[][] getEmbeddings() {
        return embeddings;
    }

}