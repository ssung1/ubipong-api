package com.eatsleeppong.ubipong.tournamentmanager.domain;

public enum EventStatus {
    CREATED("created"),
    STARTED("started"),
    AWAITING_REVIEW("awaiting review"),
    COMPLETED("completed");

    public final String name;
    EventStatus(String name) {
        this.name = name;
    }
}
