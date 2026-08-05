package com.anticipate.listr.zoho_client.controllers;

import com.anticipate.listr.embedding.services.OllamaClientService;
import com.anticipate.listr.zoho_client.entities.ZohoTicket;
import com.anticipate.listr.zoho_client.services.ZohoClientService;
import com.anticipate.listr.jwt_handling.entities.User;
import com.anticipate.listr.jwt_handling.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/test")
@RestController
public class ZohoClientController {
    private final UserService userService;
    private final ZohoClientService zohoClientService;

    public ZohoClientController(UserService userService, ZohoClientService zohoClientService) {
        this.userService = userService;
        this.zohoClientService = zohoClientService;
    }

    @GetMapping("/me")
    public String getExample() {
        return zohoClientService.getExample();
    }

    @GetMapping("/get-access-token")
    public String getZohoAccessToken() {
        return zohoClientService.getZohoAccessToken();
    }

    @GetMapping("/print-access-token")
    public String printAccessToken() {
        return zohoClientService.printZohoAccessToken();
    }

    @GetMapping("/use-access-token")
    public String callZohoApi() {
        return zohoClientService.useZohoAccessToken();
    }

    @GetMapping("/most-recent-ticket")
    public ZohoTicket getMostRecentTicket() {
        
        ZohoTicket ticket = zohoClientService.getMostRecentTicket();
        
        // embed the ticket - OLD: ZohoClientController SHOULD NOT BE RESPONSIBLE FOR EMBEDDING
        //OllamaClientService ollamaClientService = new OllamaClientService();
        //float[] embeddedDescription = ollamaClientService.getEmbedding(ticket.getSubject());
        //ticket.setEmbeddedDescription(embeddedDescription);
        //System.out.println("\n[Zoho Controller] Embedded description: " + java.util.Arrays.toString(ticket.getEmbeddedDescription()) + "\n\n");
        
        System.out.println("\n\n[Zoho Controller] Most recent ticket description and number: " + ticket.getSubject() + ", " + ticket.getTicketNumber() + "\n\n");

        return ticket;
    }
}