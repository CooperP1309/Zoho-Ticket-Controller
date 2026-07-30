package com.anticipate.listr.zoho_client.services;

import com.anticipate.listr.zoho_client.entities.ZohoAuthHeader;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ZohoClientService {

    private final RestClient restClient;

    private final String preTokenBody;

    private final String tokenHost;

    ZohoAuthHeader zohoAuthHeader;

    // Constructor preceeds @Value injection - Hence the constructor injection
    public ZohoClientService(
            @Value("${zoho.client.id}") String clientId,
            @Value("${zoho.client.secret}") String clientSecret,
            @Value("${zoho.client.orgId}") String orgId,
            @Value("${zoho.client.host}") String tokenHost) {
        
        this.restClient = RestClient.create();

        this.preTokenBody = "grant_type=client_credentials&client_id=" + clientId +
                            "&client_secret=" + clientSecret + 
                            "&scope=Desk.tickets.READ&soid=Desk." + orgId;

        this.tokenHost = tokenHost;

        this.zohoAuthHeader = new ZohoAuthHeader();
        this.zohoAuthHeader.setOrgId(orgId);
    }

    public String getExample() {
        return restClient.get()
                .uri("https://example.com")
                .retrieve()
                .body(String.class);
    }

    public String getZohoAccessToken() {

        ZohoAuthHeader tokenResponse =  
                
                restClient.post()
                .uri(tokenHost + "/oauth/v2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(preTokenBody)
                .retrieve()
                .body(ZohoAuthHeader.class);

        // access token set seperately to not overwrite the zsoid written in constructor
        this.zohoAuthHeader.setAccessToken(tokenResponse.getAccessToken());

        return "RESULTING HEADERS FROM ZOHO AUTH CLASS:\n" +
                this.zohoAuthHeader.getAuthHeaderName() + ": " + this.zohoAuthHeader.getAuthHeaderValue() + "\n" +
                this.zohoAuthHeader.getOrgIdHeaderName() + ": " + this.zohoAuthHeader.getOrgIdHeaderValue();
    }

    public String printZohoAccessToken() {
        return this.zohoAuthHeader.getAuthHeaderName() + ": " + this.zohoAuthHeader.getAuthHeaderValue() + "\n" +
                this.zohoAuthHeader.getOrgIdHeaderName() + ": " + this.zohoAuthHeader.getOrgIdHeaderValue();
    }

    public String useZohoAccessToken() {
        return restClient.get()
                .uri("https://desk.zoho.com.au/api/v1/tickets?sortBy=-createdTime&limit=1")
                .header(this.zohoAuthHeader.getAuthHeaderName(), this.zohoAuthHeader.getAuthHeaderValue())
                .header(this.zohoAuthHeader.getOrgIdHeaderName(), this.zohoAuthHeader.getOrgIdHeaderValue())
                .retrieve()
                .body(String.class);
    }
}
