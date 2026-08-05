package com.anticipate.listr.embedding.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Agent {

    public Agent() {
    }

    @JsonProperty("name")
    private String name;

    @JsonProperty("zoho_id")
    private int zohoId;

    @JsonProperty("skills")
    private String[] skills;

    public String getName() {
        return name;
    }

    public int getZohoId() {
        return zohoId;
    }

    public String[] getSkills() {
        return skills;
    }
}