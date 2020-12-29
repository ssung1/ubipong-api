package com.eatsleeppong.ubipong.tournamentmanager.response;

import lombok.Data;

@Data
public class Game {
    private int player1Score;
    private int player2Score;
    private boolean winForPlayer1;
}
