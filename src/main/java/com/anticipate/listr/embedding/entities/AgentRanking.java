package com.anticipate.listr.embedding.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AgentRanking {

    public AgentRanking() {
    }

    @JsonProperty("name")
    private String name;

    @JsonProperty("zoho_id")
    private int zohoId;

    @JsonProperty("similarity_score")
    private double similarityScore;

    @JsonProperty("number_of_tickets")
    private int numberOfTickets;

    public String getName() {
        return name;
    }

    public int getZohoId() {
        return zohoId;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setZohoId(int zohoId) {
        this.zohoId = zohoId;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }
}