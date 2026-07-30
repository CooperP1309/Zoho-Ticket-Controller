package com.anticipate.listr.zoho_client.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ZohoAuthHeader {

    public ZohoAuthHeader() {
    }

    @JsonProperty("access_token")
    private String access_token;

    @JsonProperty("zsoid")
    private String zsoid;

    public String getAccessToken() {
        return access_token;
    }

    public String getZsoid() {
        return zsoid;
    }

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    public void setZsoid(String zsoid) {
        this.zsoid = zsoid;
    }

    // http header getters
    public String getAuthHeader() {
        return "Authorization: Zoho-oauthtoken " + access_token;
    }

    public String getOrgIdHeader() {
        return "orgId: " + zsoid;
    }
}