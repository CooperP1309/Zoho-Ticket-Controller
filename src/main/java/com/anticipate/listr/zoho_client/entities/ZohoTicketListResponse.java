package com.anticipate.listr.zoho_client.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ZohoTicketListResponse {

    public ZohoTicketListResponse() {
    }

    @JsonProperty("data")
    private List<ZohoTicket> data;

    public List<ZohoTicket> getData() {
        return data;
    }

    public void setData(List<ZohoTicket> data) {
        this.data = data;
    }
}
