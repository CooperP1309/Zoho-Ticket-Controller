package com.anticipate.listr.zoho_client.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TicketCountList {

    public TicketCountList() {
    }

    @JsonProperty("statusType")
    private List<TicketCount> statusType;

    public List<TicketCount> getStatusType() {
        return statusType;
    }
}
