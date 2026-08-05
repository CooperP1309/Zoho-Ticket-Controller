package com.anticipate.listr.zoho_client.controllers;

import com.anticipate.listr.embedding.services.SkillEmbeddingService;
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
    private final SkillEmbeddingService skillEmbeddingService;

    public ZohoClientController(UserService userService, ZohoClientService zohoClientService, SkillEmbeddingService skillEmbeddingService) {
        this.userService = userService;
        this.zohoClientService = zohoClientService;
        this.skillEmbeddingService = skillEmbeddingService;
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

    @GetMapping("/control-test-ticket")
    public ZohoTicket controlleTestTicket() {
        
        // fetch and deserialize ticket from Zoho Desk
        ZohoTicket ticket = zohoClientService.getMostRecentTicket();
        System.out.println("\n\n[Zoho Controller] Most recent ticket description and number: " + ticket.getSubject() + ", " + ticket.getTicketNumber() + "\n\n");

        // OLD!!!: embed the ticket - ZohoClientController SHOULD NOT BE RESPONSIBLE FOR EMBEDDING
        //OllamaClientService ollamaClientService = new OllamaClientService();
        //float[] embeddedDescription = ollamaClientService.getEmbedding(ticket.getSubject());
        //ticket.setEmbeddedDescription(embeddedDescription);
        //System.out.println("\n[Zoho Controller] Embedded description: " + java.util.Arrays.toString(ticket.getEmbeddedDescription()) + "\n\n");
        
        // get order of most suitable agents for the ticket
        int agentId = skillEmbeddingService.getSimilarityAgentId(ticket.getSubject());

        if (agentId == -1) {
            System.out.println("\n\n[Zoho Controller] No suitable agent found for the ticket.\n\n");
        } else {
            System.out.println("\n\n[Zoho Controller] Most suitable agent ID for the ticket: " + agentId + "\n\n");
        }

        return ticket;
    }

    @GetMapping("/control-ticket")
    public ZohoTicket controlleTicket() {
        
        // fetch and deserialize ticket from Zoho Desk
        //ZohoTicket ticket = zohoClientService.getMostRecentTicket();
        
        ZohoTicket ticket = new ZohoTicket();
        ticket.setId("1234");
        ticket.setTicketNumber("56789");
        ticket.setSubject("My messages keep bouncing");
        ticket.setAssigneeId("101112");
        
        System.out.println("\n\n[Zoho Controller] Most recent ticket description and number: " + ticket.getSubject() + ", " + ticket.getTicketNumber() + "\n\n");

        // OLD!!!: embed the ticket - ZohoClientController SHOULD NOT BE RESPONSIBLE FOR EMBEDDING
        //OllamaClientService ollamaClientService = new OllamaClientService();
        //float[] embeddedDescription = ollamaClientService.getEmbedding(ticket.getSubject());
        //ticket.setEmbeddedDescription(embeddedDescription);
        //System.out.println("\n[Zoho Controller] Embedded description: " + java.util.Arrays.toString(ticket.getEmbeddedDescription()) + "\n\n");
        
        // get order of most suitable agents for the ticket
        int agentId = skillEmbeddingService.getSimilarityAgentId(ticket.getSubject());

        if (agentId == -1) {
            System.out.println("\n\n[Zoho Controller] No suitable agent found for the ticket.\n\n");
        } else {
            System.out.println("\n\n[Zoho Controller] Most suitable agent ID for the ticket: " + agentId + "\n\n");
        }

        return ticket;
    }

        @GetMapping("/control-ticket1")
    public ZohoTicket controlleTicket1() {
        
        // fetch and deserialize ticket from Zoho Desk
        //ZohoTicket ticket = zohoClientService.getMostRecentTicket();
        
        ZohoTicket ticket = new ZohoTicket();
        ticket.setId("1234");
        ticket.setTicketNumber("56789");
        ticket.setSubject("Getting paper jam error");
        ticket.setAssigneeId("101112");
        
        System.out.println("\n\n[Zoho Controller] Most recent ticket description and number: " + ticket.getSubject() + ", " + ticket.getTicketNumber() + "\n\n");

        // OLD!!!: embed the ticket - ZohoClientController SHOULD NOT BE RESPONSIBLE FOR EMBEDDING
        //OllamaClientService ollamaClientService = new OllamaClientService();
        //float[] embeddedDescription = ollamaClientService.getEmbedding(ticket.getSubject());
        //ticket.setEmbeddedDescription(embeddedDescription);
        //System.out.println("\n[Zoho Controller] Embedded description: " + java.util.Arrays.toString(ticket.getEmbeddedDescription()) + "\n\n");
        
        // get order of most suitable agents for the ticket
        int agentId = skillEmbeddingService.getSimilarityAgentId(ticket.getSubject());

        if (agentId == -1) {
            System.out.println("\n\n[Zoho Controller] No suitable agent found for the ticket.\n\n");
        } else {
            System.out.println("\n\n[Zoho Controller] Most suitable agent ID for the ticket: " + agentId + "\n\n");
        }
        
        return ticket;
    }

        @GetMapping("/control-ticket2")
    public ZohoTicket controlleTicket2() {
        
        // fetch and deserialize ticket from Zoho Desk
        //ZohoTicket ticket = zohoClientService.getMostRecentTicket();
        
        ZohoTicket ticket = new ZohoTicket();
        ticket.setId("1234");
        ticket.setTicketNumber("56789");
        ticket.setSubject("Files not syncing to cloud");
        ticket.setAssigneeId("101112");
        
        System.out.println("\n\n[Zoho Controller] Most recent ticket description and number: " + ticket.getSubject() + ", " + ticket.getTicketNumber() + "\n\n");

        // OLD!!!: embed the ticket - ZohoClientController SHOULD NOT BE RESPONSIBLE FOR EMBEDDING
        //OllamaClientService ollamaClientService = new OllamaClientService();
        //float[] embeddedDescription = ollamaClientService.getEmbedding(ticket.getSubject());
        //ticket.setEmbeddedDescription(embeddedDescription);
        //System.out.println("\n[Zoho Controller] Embedded description: " + java.util.Arrays.toString(ticket.getEmbeddedDescription()) + "\n\n");
        
        // get order of most suitable agents for the ticket
        int agentId = skillEmbeddingService.getSimilarityAgentId(ticket.getSubject());

        if (agentId == -1) {
            System.out.println("\n\n[Zoho Controller] No suitable agent found for the ticket.\n\n");
        } else {
            System.out.println("\n\n[Zoho Controller] Most suitable agent ID for the ticket: " + agentId + "\n\n");
        }
        
        return ticket;
    }

        @GetMapping("/control-ticket3")
    public ZohoTicket controlleTicket3() {
        
        // fetch and deserialize ticket from Zoho Desk
        //ZohoTicket ticket = zohoClientService.getMostRecentTicket();
        
        ZohoTicket ticket = new ZohoTicket();
        ticket.setId("1234");
        ticket.setTicketNumber("56789");
        ticket.setSubject("Can't connect to office Wi-Fi");
        ticket.setAssigneeId("101112");
        
        System.out.println("\n\n[Zoho Controller] Most recent ticket description and number: " + ticket.getSubject() + ", " + ticket.getTicketNumber() + "\n\n");

        // OLD!!!: embed the ticket - ZohoClientController SHOULD NOT BE RESPONSIBLE FOR EMBEDDING
        //OllamaClientService ollamaClientService = new OllamaClientService();
        //float[] embeddedDescription = ollamaClientService.getEmbedding(ticket.getSubject());
        //ticket.setEmbeddedDescription(embeddedDescription);
        //System.out.println("\n[Zoho Controller] Embedded description: " + java.util.Arrays.toString(ticket.getEmbeddedDescription()) + "\n\n");
        
        // get order of most suitable agents for the ticket
        int agentId = skillEmbeddingService.getSimilarityAgentId(ticket.getSubject());

        if (agentId == -1) {
            System.out.println("\n\n[Zoho Controller] No suitable agent found for the ticket.\n\n");
        } else {
            System.out.println("\n\n[Zoho Controller] Most suitable agent ID for the ticket: " + agentId + "\n\n");
        }
        
        return ticket;
    }

        @GetMapping("/control-ticket4")
    public ZohoTicket controlleTicket4() {
        
        // fetch and deserialize ticket from Zoho Desk
        //ZohoTicket ticket = zohoClientService.getMostRecentTicket();
        
        ZohoTicket ticket = new ZohoTicket();
        ticket.setId("1234");
        ticket.setTicketNumber("56789");
        ticket.setSubject("Computer keeps restarting");
        ticket.setAssigneeId("101112");
        
        System.out.println("\n\n[Zoho Controller] Most recent ticket description and number: " + ticket.getSubject() + ", " + ticket.getTicketNumber() + "\n\n");

        // OLD!!!: embed the ticket - ZohoClientController SHOULD NOT BE RESPONSIBLE FOR EMBEDDING
        //OllamaClientService ollamaClientService = new OllamaClientService();
        //float[] embeddedDescription = ollamaClientService.getEmbedding(ticket.getSubject());
        //ticket.setEmbeddedDescription(embeddedDescription);
        //System.out.println("\n[Zoho Controller] Embedded description: " + java.util.Arrays.toString(ticket.getEmbeddedDescription()) + "\n\n");
        
        // get order of most suitable agents for the ticket
        int agentId = skillEmbeddingService.getSimilarityAgentId(ticket.getSubject());

        if (agentId == -1) {
            System.out.println("\n\n[Zoho Controller] No suitable agent found for the ticket.\n\n");
        } else {
            System.out.println("\n\n[Zoho Controller] Most suitable agent ID for the ticket: " + agentId + "\n\n");
        }
        
        return ticket;
    }

    @GetMapping("/control-ticket5")
    public ZohoTicket controlleTicket5() {
        
        // fetch and deserialize ticket from Zoho Desk
        //ZohoTicket ticket = zohoClientService.getMostRecentTicket();
        
        ZohoTicket ticket = new ZohoTicket();
        ticket.setId("1234");
        ticket.setTicketNumber("56789");
        ticket.setSubject("Keep getting malware warnings");
        ticket.setAssigneeId("101112");
        
        System.out.println("\n\n[Zoho Controller] Most recent ticket description and number: " + ticket.getSubject() + ", " + ticket.getTicketNumber() + "\n\n");

        // OLD!!!: embed the ticket - ZohoClientController SHOULD NOT BE RESPONSIBLE FOR EMBEDDING
        //OllamaClientService ollamaClientService = new OllamaClientService();
        //float[] embeddedDescription = ollamaClientService.getEmbedding(ticket.getSubject());
        //ticket.setEmbeddedDescription(embeddedDescription);
        //System.out.println("\n[Zoho Controller] Embedded description: " + java.util.Arrays.toString(ticket.getEmbeddedDescription()) + "\n\n");
        
        // get order of most suitable agents for the ticket
        int agentId = skillEmbeddingService.getSimilarityAgentId(ticket.getSubject());

        if (agentId == -1) {
            System.out.println("\n\n[Zoho Controller] No suitable agent found for the ticket.\n\n");
        } else {
            System.out.println("\n\n[Zoho Controller] Most suitable agent ID for the ticket: " + agentId + "\n\n");
        }
        
        return ticket;
    }

    @GetMapping("/control-ticket6")
    public ZohoTicket controlleTicket6() {
        
        // fetch and deserialize ticket from Zoho Desk
        //ZohoTicket ticket = zohoClientService.getMostRecentTicket();
        
        ZohoTicket ticket = new ZohoTicket();
        ticket.setId("1234");
        ticket.setTicketNumber("56789");
        ticket.setSubject("App blocked by defender");
        ticket.setAssigneeId("101112");
        
        System.out.println("\n\n[Zoho Controller] Most recent ticket description and number: " + ticket.getSubject() + ", " + ticket.getTicketNumber() + "\n\n");

        // OLD!!!: embed the ticket - ZohoClientController SHOULD NOT BE RESPONSIBLE FOR EMBEDDING
        //OllamaClientService ollamaClientService = new OllamaClientService();
        //float[] embeddedDescription = ollamaClientService.getEmbedding(ticket.getSubject());
        //ticket.setEmbeddedDescription(embeddedDescription);
        //System.out.println("\n[Zoho Controller] Embedded description: " + java.util.Arrays.toString(ticket.getEmbeddedDescription()) + "\n\n");
        
        // get order of most suitable agents for the ticket
        int agentId = skillEmbeddingService.getSimilarityAgentId(ticket.getSubject());

        if (agentId == -1) {
            System.out.println("\n\n[Zoho Controller] No suitable agent found for the ticket.\n\n");
        } else {
            System.out.println("\n\n[Zoho Controller] Most suitable agent ID for the ticket: " + agentId + "\n\n");
        }
        
        return ticket;
    }
}