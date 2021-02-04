package com.eatsleeppong.ubipong.tournamentmanager.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EventStatusDto {
    CREATED("created"),
    STARTED("started"),
    AWAITING_REVIEW("awaiting review"),
    COMPLETED("completed");

    private final String value;
    EventStatusDto(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
