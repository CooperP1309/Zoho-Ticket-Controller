package com.anticipate.listr.zoho_client.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ZohoAuthHeader {

    public ZohoAuthHeader() {
    }

    @JsonProperty("access_token")
    private String access_token;

    @JsonProperty("orgId")
    private String orgId;

    @JsonProperty("expires_in")
    private int expiresIn;

    // epoch millis at which the access token expires; not part of the Zoho response
    private long expiryTime;

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    // derives an absolute expiry instant from the "expires_in" (seconds) value returned by Zoho
    public void setExpiryTime(int expiresIn) {
        this.expiryTime = System.currentTimeMillis() + (expiresIn * 1000L);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= expiryTime;
    }

    public String getAccessToken() {
        return access_token;
    }

    public int getExpiresIn() {
        return expiresIn;
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