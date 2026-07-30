package com.anticipate.listr.zoho_client.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ZohoAccessToken {

    @JsonProperty("access_token")
    private String access_token;

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }
}