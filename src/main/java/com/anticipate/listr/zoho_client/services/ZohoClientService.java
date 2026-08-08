package com.anticipate.listr.zoho_client.services;

/* spring specific modules */
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/* local modules */
import com.anticipate.listr.zoho_client.entities.ZohoAuthHeader;
import com.anticipate.listr.zoho_client.entities.ZohoTicket;
import com.anticipate.listr.zoho_client.entities.ZohoTicketListResponse;
import com.anticipate.listr.zoho_client.entities.TicketCountList;
import com.anticipate.listr.zoho_client.entities.TicketCount;
import com.anticipate.listr.zoho_client.utilities.AgentLoadBalancerUtility;

@Service
public class ZohoClientService {

    private final RestClient restClient;

    private final String preTokenBody;

    private final String tokenHost;

    ZohoAuthHeader zohoAuthHeader;

    public ZohoClientService(
            @Value("${zoho.client.id}") String clientId,
            @Value("${zoho.client.secret}") String clientSecret,
            @Value("${zoho.client.orgId}") String orgId,
            @Value("${zoho.client.host}") String tokenHost) {
        
        this.restClient = RestClient.create();

        this.preTokenBody = "grant_type=client_credentials&client_id=" + clientId +
                            "&client_secret=" + clientSecret + 
                            "&scope=Desk.tickets.READ,Desk.search.READ&soid=Desk." + orgId;

        this.tokenHost = tokenHost;
        this.zohoAuthHeader = new ZohoAuthHeader();
        this.zohoAuthHeader.setOrgId(orgId);

        getZohoAccessToken();
    }

    /*
    *   Gets an access token
    *
    *   Uses Zoho Client Service to fetch an access token
    *   from Zoho Desk. The token returned from this
    *   endpoint is only scoped to the Zoho Desk API.
    */
    public String getZohoAccessToken() {

        ZohoAuthHeader tokenResponse =  
                
                restClient.post()
                .uri(tokenHost + "/oauth/v2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(preTokenBody)
                .retrieve()
                .body(ZohoAuthHeader.class);

        // NOTE: access token set seperately to not overwrite the zsoid written during constructor
        this.zohoAuthHeader.setAccessToken(tokenResponse.getAccessToken());
        this.zohoAuthHeader.setExpiryTime(tokenResponse.getExpiresIn());

        return "RESULTING HEADERS FROM ZOHO AUTH CLASS:\n" +
                this.zohoAuthHeader.getAuthHeaderName() + ": " + this.zohoAuthHeader.getAuthHeaderValue() + "\n" +
                this.zohoAuthHeader.getOrgIdHeaderName() + ": " + this.zohoAuthHeader.getOrgIdHeaderValue();
    }

    /*
    *   Ensures the access token is valid
    *
    *   Refreshes the access token if it has expired before
    *   it is used to make a request against the Zoho Desk API.
    */
    private void ensureValidAccessToken() {
        if (this.zohoAuthHeader.isExpired()) {
            getZohoAccessToken();
        }
    }

    /*
    *   Fetches the access token as string
    *
    *   Returns the retrieved access token in string format.
    */
    public String printZohoAccessToken() {
        return this.zohoAuthHeader.getAuthHeaderName() + ": " + this.zohoAuthHeader.getAuthHeaderValue() + "\n" +
                this.zohoAuthHeader.getOrgIdHeaderName() + ": " + this.zohoAuthHeader.getOrgIdHeaderValue();
    }

    /*
    *   Uses the access token
    *
    *   Uses the access token to make a test request to
    *   fetch the most recent ticket in your tenancies
    *   Zoho Desk ticket queue. DOESN'T deserialize response.
    */
    public String useZohoAccessToken() {
        ensureValidAccessToken();

        return restClient.get()
                .uri("https://desk.zoho.com.au/api/v1/tickets?sortBy=-createdTime&limit=1")
                .header(this.zohoAuthHeader.getAuthHeaderName(), this.zohoAuthHeader.getAuthHeaderValue())
                .header(this.zohoAuthHeader.getOrgIdHeaderName(), this.zohoAuthHeader.getOrgIdHeaderValue())
                .retrieve()
                .body(String.class);
    }

    /*
    *   Fetches the most recent ticket
    *
    *   Uses the access token to make a request to
    *   fetch the most recent ticket in your tenancies
    *   Zoho Desk ticket queue. This resposne is then
    *   deserialized into a ZohoTicket object.
    */
    public ZohoTicket getMostRecentTicket() {
        ensureValidAccessToken();

        ZohoTicketListResponse response =

            restClient.get()
                .uri("https://desk.zoho.com.au/api/v1/tickets?sortBy=-createdTime&limit=1&assignee=Unassigned")
                .header(this.zohoAuthHeader.getAuthHeaderName(), this.zohoAuthHeader.getAuthHeaderValue())
                .header(this.zohoAuthHeader.getOrgIdHeaderName(), this.zohoAuthHeader.getOrgIdHeaderValue())
                .retrieve()
                .body(ZohoTicketListResponse.class);

        if (response.getData().isEmpty()) {
            return null;
        }

        return response.getData().get(0);
    }

    /*
    *   Fetches the agent traffic data
    *
    *   Uses the access token to make a request to
    *   fetch the ticket counts. The response from Zoho
    *   returns that agents ticket count for each status type.
    */
    public int getAgentTicketCounts(String assigneeId) {
        ensureValidAccessToken();

        TicketCountList response =

            restClient.get()
                .uri("https://desk.zoho.com.au/api/v1/ticketsCountByFieldValues?assigneeId=" + assigneeId + "&field=statusType")
                .header(this.zohoAuthHeader.getAuthHeaderName(), this.zohoAuthHeader.getAuthHeaderValue())
                .header(this.zohoAuthHeader.getOrgIdHeaderName(), this.zohoAuthHeader.getOrgIdHeaderValue())
                .retrieve()
                .body(TicketCountList.class);

        TicketCount ticketCountOpen = response.getStatusType().get(0);
        TicketCount ticketCountOnHold = response.getStatusType().get(1);

        int totalTickets = ticketCountOpen.getCount() + ticketCountOnHold.getCount();

        System.out.println("[ZohoClientService] Ticket Count of agentID: " + assigneeId + " = " + totalTickets);

        return totalTickets;
    }
}
