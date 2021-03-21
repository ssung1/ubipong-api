package com.eatsleeppong.ubipong.tournamentmanager.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleDto {
    ORGANIZATION_ADMIN("organization admin"),
    TOURNAMENT_ADMIN("tournament admin"),
    SCORE_ENTRY("score entry"),
    EVERYONE("everyone");

    private final String value;
    RoleDto(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
