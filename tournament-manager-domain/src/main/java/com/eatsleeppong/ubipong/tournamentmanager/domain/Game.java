package com.eatsleeppong.ubipong.tournamentmanager.domain;

import lombok.Builder;
import lombok.Value;
import lombok.Builder.Default;

@Value
@Builder
public class Game {
    public static final Integer STATUS_INCOMPLETE = 10;
    public static final Integer STATUS_COMPLETE = 11;

    private int player1Score;
    private int player2Score;

    /**
     * Can either be 1 (for player 1) or 2 (for player 2).
     * It is only set for incomplete games.
     * If null, then whoever has the higher score wins.
     */
    @Default
    private Integer winnerIndex = null;

    private int status;

    public boolean isWinForPlayer1() {
        if (winnerIndex != null) {
            return winnerIndex == 1;
        }
        else {
            return player1Score > player2Score;
        }
    }
}