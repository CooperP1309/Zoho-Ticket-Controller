package com.anticipate.listr.zoho_client.utilities;

/* spring specific modules */
import org.springframework.stereotype.Component;

/* java modules */
import java.util.List;

/* local modules */
import com.anticipate.listr.zoho_client.services.ZohoClientService;
import com.anticipate.listr.embedding.entities.AgentRanking;

@Component
public class AgentLoadBalancerUtility {

    private ZohoClientService zohoClientService;
 
    public AgentLoadBalancerUtility(ZohoClientService zohoClientService) {
        this.zohoClientService = zohoClientService;
    }

    /*
    *   Reorganises a list of agents based on ticket load
    *
    *   Give this method a list of agents in order of suitabliltiy 
    *   for a given ticket. This method will then check the number
    *   of tickets assigned to each agent and bump an agent to the
    *   bottom of the list if they have too many tickets assigned 
    *   to them.
    */
    public List<AgentRanking> loadBalanceAgentRanking(List<AgentRanking> agentRankings) {
        
        if (agentRankings.size() == 0) {
            return agentRankings;
        }

        int ticketLimit = 0;

        for (int i = 0; i < agentRankings.size(); i++) {

            int ticketCount = zohoClientService.getAgentTicketCounts(agentRankings.get(i).getZohoId());
            agentRankings.get(i).setNumberOfTickets(ticketCount);
            ticketLimit += ticketCount;
        }

        ticketLimit /= agentRankings.size();
        ticketLimit += 5;

        for (int i = 0; i < agentRankings.size();) {

            if (agentRankings.get(i).getNumberOfTickets() > ticketLimit) {
                agentRankings.add(agentRankings.remove(i)); // bump to end of list
                continue;
            }

            i++;
        }
        
        return agentRankings;
    }
}


