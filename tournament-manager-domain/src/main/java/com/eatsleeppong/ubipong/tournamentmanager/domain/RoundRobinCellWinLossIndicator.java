package com.eatsleeppong.ubipong.tournamentmanager.domain;

public enum RoundRobinCellWinLossIndicator {
    WIN("W"),
    LOSS("L");

    private String value;
    RoundRobinCellWinLossIndicator(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
