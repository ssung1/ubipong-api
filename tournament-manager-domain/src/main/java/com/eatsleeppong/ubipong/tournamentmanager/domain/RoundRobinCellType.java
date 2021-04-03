package com.eatsleeppong.ubipong.tournamentmanager.domain;

public enum RoundRobinCellType {
    NAME(10),
    EMPTY(11),
    MATCH_COMPLETE(12),
    MATCH_INCOMPLETE(13),
    TEXT(14);

    private final int value;
    RoundRobinCellType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
