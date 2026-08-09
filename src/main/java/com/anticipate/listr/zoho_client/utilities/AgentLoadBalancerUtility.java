package com.anticipate.listr.zoho_client.utilities;

/* spring specific modules */
import org.springframework.stereotype.Component;

/* java modules */
import java.util.ArrayList;
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
    *   to them. To bump an agent to the bottom of the list, ther
    *   overloaded agent is put into a temporary "overloaded" list.
    *   At the end, that list is then merged to the back of the final
    *   rankings list.
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

        // two temporary lists will group over loaded and under loaded agents accordingly
        List<AgentRanking> overLoadedAgents = new ArrayList<>();
        List<AgentRanking> balancedAgentRankings = new ArrayList<>();

        // splitting of the overloaded and underloaded agents
        for (AgentRanking agentRanking: agentRankings) {

            if (agentRanking.getNumberOfTickets() > ticketLimit) {
                overLoadedAgents.add(agentRanking);
                continue;
            }

            balancedAgentRankings.add(agentRanking);
        }
        
        // remerging of the list
        for (AgentRanking agentRanking: overLoadedAgents) {
            balancedAgentRankings.add(agentRanking);
        }

        return balancedAgentRankings;
    }

    /*
    *   Escapes a string for safe embedding inside a JSON string literal.
    *
    *   Handles quotes, backslashes, and control characters so that
    *   hand-built JSON bodies (e.g. ticket subjects) stay valid JSON.
    */
    public static String escapeJsonString(String input) {
        if (input == null) {
            return "";
        }

        StringBuilder escaped = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '"':
                    escaped.append("\\\"");
                    break;
                case '\\':
                    escaped.append("\\\\");
                    break;
                case '\b':
                    escaped.append("\\b");
                    break;
                case '\f':
                    escaped.append("\\f");
                    break;
                case '\n':
                    escaped.append("\\n");
                    break;
                case '\r':
                    escaped.append("\\r");
                    break;
                case '\t':
                    escaped.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        escaped.append(String.format("\\u%04x", (int) c));
                    } else {
                        escaped.append(c);
                    }
            }
        }
        return escaped.toString();
    }
}


