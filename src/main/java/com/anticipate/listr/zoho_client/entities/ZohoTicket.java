package com.anticipate.listr.zoho_client.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ZohoTicket {

    public ZohoTicket() {
    }

    @JsonProperty("id")
    private String id;

    @JsonProperty("ticketNumber")
    private String ticketNumber;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("assigneeId")
    private String assigneeId;

    private float[] embeddedDescription;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public float[] getEmbeddedDescription() {
        return embeddedDescription;
    }

    public void setEmbeddedDescription(float[] embeddedDescription) {
        this.embeddedDescription = embeddedDescription;
    }
}