package com.eatsleeppong.ubipong.tournamentmanager.domain;

public enum BracketType {
    ROUND_ROBIN("round robin"),
    SINGLE_ELIMINATION("single elimination"),
    DOUBLE_ELIMINATION("double elimination");

    public final String name;
    BracketType(String name) {
        this.name = name;
    }
}
