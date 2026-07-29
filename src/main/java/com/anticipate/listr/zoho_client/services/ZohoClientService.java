package com.anticipate.listr.zoho_client.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ZohoClientService {

    private final RestClient restClient;

    private final String preTokenBody;

    // Constructor preceeds @Value injection - Hence the constructor injection
    public ZohoClientService(
            @Value("${zoho.client.id}") String clientId,
            @Value("${zoho.client.secret}") String clientSecret,
            @Value("${zoho.client.scope}") String scope,
            @Value("${zoho.client.host}") String redirectHost,
            @Value("${zoho.client.zsoid}") String zsoid) {
        
        this.restClient = RestClient.create();

        this.preTokenBody = "grant_type=client_credentials&client_id=" + clientId +
                            "&client_secret=" + clientSecret + "&scope=Desk.tickets.READ&soid="
                            + zsoid;
    }

    public String getExample() {
        return restClient.get()
                .uri("https://example.com")
                .retrieve()
                .body(String.class);
    }

    public String getZohoAccessToken() {
        
        
        return preTokenBody;

        /*
        return restClient.post()
                .uri("https://accounts.zoho.com.au/oauth/v2/token")
                //.body(body)
                .retrieve()
                .body(String.class);
        */
    }
}
