package com.anticipate.listr.zoho_client.services;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ZohoClientService {

    private final RestClient restClient;

    private final String preTokenBody;

    private final String tokenHost;

    // Constructor preceeds @Value injection - Hence the constructor injection
    public ZohoClientService(
            @Value("${zoho.client.id}") String clientId,
            @Value("${zoho.client.secret}") String clientSecret,
            @Value("${zoho.client.zsoid}") String zsoid,
            @Value("${zoho.client.host}") String tokenHost) {
        
        this.restClient = RestClient.create();

        this.preTokenBody = "grant_type=client_credentials&client_id=" + clientId +
                            "&client_secret=" + clientSecret + 
                            "&scope=Desk.tickets.READ&soid=Desk.tickets.READ";


        this.tokenHost = tokenHost;
    }

    public String getExample() {
        return restClient.get()
                .uri("https://example.com")
                .retrieve()
                .body(String.class);
    }

    public String getZohoAccessToken() {

        return restClient.post()
                .uri(tokenHost + "/oauth/v2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(preTokenBody)
                .retrieve()
                .body(String.class);
    }
}
