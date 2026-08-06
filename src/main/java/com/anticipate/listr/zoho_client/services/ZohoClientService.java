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
                            "&scope=Desk.tickets.READ&soid=Desk." + orgId;

        this.tokenHost = tokenHost;
        this.zohoAuthHeader = new ZohoAuthHeader();
        this.zohoAuthHeader.setOrgId(orgId);
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

        return "RESULTING HEADERS FROM ZOHO AUTH CLASS:\n" +
                this.zohoAuthHeader.getAuthHeaderName() + ": " + this.zohoAuthHeader.getAuthHeaderValue() + "\n" +
                this.zohoAuthHeader.getOrgIdHeaderName() + ": " + this.zohoAuthHeader.getOrgIdHeaderValue();
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
        ZohoTicketListResponse response =

            restClient.get()
                .uri("https://desk.zoho.com.au/api/v1/tickets?sortBy=-createdTime&limit=1")
                .header(this.zohoAuthHeader.getAuthHeaderName(), this.zohoAuthHeader.getAuthHeaderValue())
                .header(this.zohoAuthHeader.getOrgIdHeaderName(), this.zohoAuthHeader.getOrgIdHeaderValue())
                .retrieve()
                .body(ZohoTicketListResponse.class);

        ZohoTicket ticket = response.getData().get(0);

        return ticket;
    }

    /*
    *   Fetches the agent traffic data
    *
    *   Uses the access token to make a request to
    *   fetch the ticket counts, broken down by status,
    *   for a single agent in your tenancies Zoho Desk.
    *   ticketsCountByFieldValues only groups by ticket
    *   fields (statusType/status/priority/etc.), so it
    *   must be scoped to one agent per call via assigneeId
    *   rather than grouping by agent directly.
    */
    public String getAgentTicketCounts(String assigneeId) {

        String response =

            restClient.get()
                .uri("https://desk.zoho.com.au/api/v1/ticketsCountByFieldValues?assigneeId=" + assigneeId + "&field=statusType")
                .header(this.zohoAuthHeader.getAuthHeaderName(), this.zohoAuthHeader.getAuthHeaderValue())
                .header(this.zohoAuthHeader.getOrgIdHeaderName(), this.zohoAuthHeader.getOrgIdHeaderValue())
                .retrieve()
                .body(String.class);

        System.out.println("[ZohoClientService] Agent traffic response: " + response);

        return response;
    }
}
