package com.anticipate.listr.zoho_client.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketCount {

    public TicketCount() {
    }

    @JsonProperty("count")
    private int count;

    @JsonProperty("value")
    private String value;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}