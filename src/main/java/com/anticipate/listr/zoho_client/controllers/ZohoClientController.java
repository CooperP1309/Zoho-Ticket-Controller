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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequestMapping("/zoho")
@Controller
public class ZohoClientController {
    private final UserService userService;
    private final ZohoClientService zohoClientService;
    private final SkillEmbeddingService skillEmbeddingService;

    public ZohoClientController(UserService userService, ZohoClientService zohoClientService, SkillEmbeddingService skillEmbeddingService) {
        this.userService = userService;
        this.zohoClientService = zohoClientService;
        this.skillEmbeddingService = skillEmbeddingService;
    }



    /*----- access token debugging -----*/

    @GetMapping("/get-access-token")
    @ResponseBody
    /*
    *   Gets an access token
    *
    *   Uses Zoho Client Service to fetch an access token
    *   from Zoho Desk. The token returned from this
    *   endpoint is only scoped to the Zoho Desk API.
    */
    public String getZohoAccessToken() {
        return zohoClientService.getZohoAccessToken();
    }

    @GetMapping("/print-access-token")
    @ResponseBody
    /*
    *   Prints the access token
    *
    *   Prints the retrieved access token to console as
    *   well as returns it to the user in HTTP response.
    */
    public String printAccessToken() {
        return zohoClientService.printZohoAccessToken();
    }

    @GetMapping("/test-access-token")
    @ResponseBody
    /*
    *   Tests the access token
    *
    *   Uses the access token to make a test request to
    *   fetch the most recent ticket in your tenancies
    *   Zoho Desk ticket queue.
    */
    public String testAccessToken() {
        return zohoClientService.useZohoAccessToken();
    }



    /*----- ticket and embedding testing -----*/

    @GetMapping("/test-page")
    /*  
    *   Displays testing page 
    *
    *   When called, returns html for a test form.
    *   This form allows the submitting of test ticket 
    *   subjects from the browser. This form submits to 
    *   to the function after this one. 
    */
    public String showTestForm(Model model) {
        model.addAttribute("ticket", new ZohoTicket());
        return "test-page";
    }

    @PostMapping("/test-page")
    /*  
    *   Submits a test ticket
    *   
    *   This function is called when the above test form is
    *   submitted. The submitted ticket is sent through
    *   the Zoho Ticket Controller pipeline, only stopping
    *   short of actually updating the ticket in Zoho Desk. 
    */
    public String submitTestTicket(@ModelAttribute ZohoTicket ticket, Model model) {
        
        ticket.setId("1234");
        ticket.setTicketNumber("56789");
        ticket.setAssigneeId("101112");

        int agentId = skillEmbeddingService.getSimilarityAgentId(ticket.getSubject());
        System.out.println("\n\n[Zoho Controller] Most suitable agent ID for the ticket: " + agentId + "\n\n");

        model.addAttribute("ticket", ticket);
        model.addAttribute("agentId", agentId);

        return "test-page";
    }

    /*
    *   Submits a hardcoded test ticket
    *
    *   This function has a hardcoded test ticket that is
    *   sent through the Zoho Ticket Controller pipeline, 
    *   only stopping short of actually updating the ticket
    *   in Zoho Desk.
    */
    @GetMapping("/sort-test-ticket")
    @ResponseBody
    public String sortTestTicket() {

        ZohoTicket ticket = new ZohoTicket();
        ticket.setId("1234");
        ticket.setTicketNumber("56789");
        ticket.setSubject("My messages keep bouncing");
        ticket.setAssigneeId("101112");

        System.out.println("\n\n[Zoho Controller] Most recent ticket description and number: " + ticket.getSubject() + ", " + ticket.getTicketNumber() + "\n\n");

        // get order of most suitable agents for the ticket
        int agentId = skillEmbeddingService.getSimilarityAgentId(ticket.getSubject());
        System.out.println("\n\n[Zoho Controller] Most suitable agent ID for the ticket: " + agentId + "\n\n");

        return ticket.toString();
    }

    /*
    *   Submits a ticket fetched from Zoho Desk
    *
    *   This function fetches the most recent ticket from Zoho Desk
    *   and sends it through the Zoho Ticket Controller pipeline,
    *   only stopping short of actually updating the ticket in Zoho Desk.
    */
    @GetMapping("/sort-ticket")
    @ResponseBody
    public String sortTicket() {

        // fetch and deserialize ticket from Zoho Desk
        ZohoTicket ticket = zohoClientService.getMostRecentTicket();
        System.out.println("\n\n[Zoho Controller] Most recent ticket description and number: " + ticket.getSubject() + ", " + ticket.getTicketNumber() + "\n\n");

        // get order of most suitable agents for the ticket
        int agentId = skillEmbeddingService.getSimilarityAgentId(ticket.getSubject());
        System.out.println("\n\n[Zoho Controller] Most suitable agent ID for the ticket: " + agentId + "\n\n");

        return ticket.toString();
    }
}