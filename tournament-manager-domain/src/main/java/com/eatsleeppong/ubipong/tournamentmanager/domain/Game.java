package com.eatsleeppong.ubipong.tournamentmanager.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Game {
    public static final Integer STATUS_INCOMPLETE = 10;
    public static final Integer STATUS_COMPLETE = 11;

    private int player1Score;
    private int player2Score;
    private boolean winForPlayer1;
    private int status;
}