package com.eatsleeppong.ubipong.tournamentmanager.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Game {
    private int player1Score;
    private int player2Score;
    private boolean winForPlayer1;
}