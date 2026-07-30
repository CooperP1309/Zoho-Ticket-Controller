package com.anticipate.listr.zoho_client.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ZohoAuthHeader {

    public ZohoAuthHeader() {
    }

    @JsonProperty("access_token")
    private String access_token;

    @JsonProperty("orgId")
    private String orgId;

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAccessToken() {
        return access_token;
    }

    // http header name/value pairs
    // rest client api requires separate name and value
    public String getAuthHeaderName() {
        return "Authorization";
    }

    public String getAuthHeaderValue() {
        return "Zoho-oauthtoken " + access_token;
    }

    public String getOrgIdHeaderName() {
        return "orgId";
    }

    public String getOrgIdHeaderValue() {
        return orgId;
    }
}