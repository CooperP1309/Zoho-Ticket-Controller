package com.anticipate.listr.zoho_client.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ZohoClientService {
    private final RestClient restClient;

    public ZohoClientService() {
        this.restClient = RestClient.create();
    }

    public String getExample() {
        return restClient.get()
                .uri("https://example.com")
                .retrieve()
                .body(String.class);
    }

    public String getZohoAccessToken() {
        
        // HARDCODING IN application.properties FOR NOW| TODO: Fix this AAHAHAH
        String body = "grant_type=authorization_code&client_id=1000.3F4A1B2C3D4E5F6G7H8I9J0K1L2M3N4O&client_secret=abcdef1234567890abcdef1234567890abcdef12&redirect_uri=https://example.com/callback&code=1000.abcdef1234567890abcdef1234567890abcdef12";
        
        return restClient.post()
                .uri("https://accounts.zoho.com.au/oauth/v2/token")
                .body(body)
                .retrieve()
                .body(String.class);
    }
}
