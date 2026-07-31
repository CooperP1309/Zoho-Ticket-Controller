package com.anticipate.listr.skill_embedding.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmbeddedSkill {

    private String zohoId;

    private String skill;

    public float[] embedding;

    public EmbeddedSkill(String zohoId, String skill, float[] embedding) {
        this.zohoId = zohoId;
        this.skill = skill;
        this.embedding = embedding;
    }

    public String getZohoId() {
        return zohoId;
    }

    public String getSkill() {
        return skill;
    }

    public float[] getEmbedding() {
        return embedding;
    }
}