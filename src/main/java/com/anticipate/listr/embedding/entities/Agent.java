package com.anticipate.listr.embedding.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Agent {

    public Agent() {
    }

    @JsonProperty("name")
    private String name;

    @JsonProperty("zoho_id")
    private String zohoId;

    @JsonProperty("skills")
    private String[] skills;

    public String getName() {
        return name;
    }

    public String getZohoId() {
        return zohoId;
    }

    public String[] getSkills() {
        return skills;
    }
}