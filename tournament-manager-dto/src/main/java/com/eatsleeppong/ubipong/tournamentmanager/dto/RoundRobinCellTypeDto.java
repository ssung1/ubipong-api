package com.eatsleeppong.ubipong.tournamentmanager.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoundRobinCellTypeDto {
    NAME(10),
    EMPTY(11),
    MATCH_COMPLETE(12),
    MATCH_INCOMPLETE(13),
    TEXT(14);

    private final int value;
    RoundRobinCellTypeDto(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
