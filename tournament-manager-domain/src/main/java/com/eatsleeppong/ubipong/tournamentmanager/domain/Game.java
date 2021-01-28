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

    public static class GameBuilder {
        private int player1Score = 0;
        private int player2Score = 0;
        private int status = Game.STATUS_INCOMPLETE;
    
        /**
         * Easier way to set scores, using the form
         * {player1score}-{player2score}
         * 
         * In case where there is no dash, the number is considered player2's score
         */
        public GameBuilder scores(final String rawScore) {
            if (rawScore == null || rawScore.isBlank()) {
                return this;
            }
            final String score = rawScore.trim();
            final int dash = score.indexOf('-');
            if (dash > 0) {
                player1Score = Integer.parseInt(score.substring(0, dash));
                player2Score = Integer.parseInt(score.substring(dash + 1));
            }
            else {
                player2Score = Integer.parseInt(score);
            }
            this.status = Game.STATUS_COMPLETE;
            return this;
        }
    }

    public boolean isWinForPlayer1() {
        if (winnerIndex != null) {
            return winnerIndex == 1;
        }
        else {
            return player1Score > player2Score;
        }
    }

    public Game transpose() {
        return Game.builder()
            .player1Score(player2Score)
            .player2Score(player1Score)
            .status(status)
            .winnerIndex(winnerIndex)
            .build();
    }

    /**
     * If player 1 wins, return player 2's score
     * If player 2 wins, return negative of player 1's score
     * @return
     */
    public int getSimplifiedScore() {
        if (isWinForPlayer1()) {
            return getPlayer2Score();
        } else {
            return -getPlayer1Score();
        }
    }
}