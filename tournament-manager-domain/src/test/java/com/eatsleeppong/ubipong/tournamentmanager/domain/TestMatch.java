package com.eatsleeppong.ubipong.tournamentmanager.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;

import com.eatsleeppong.ubipong.tournamentmanager.TestHelper;

import static org.hamcrest.MatcherAssert.*;

public class TestMatch {
    private final int spongebobId = 1;
    private final int patrickId = 2;

    private Match match = Match.builder()
        .player1Id(spongebobId)
        .player2Id(patrickId)
        .winnerId(patrickId)
        .build();

    @Test
    @DisplayName("should flag the result as invalid if match has no winners and no games played")
    public void testResultInvalidIfMatchHasNoWinnerAndNoGames() {
        final Match m = match.withWinnerId(null).withGameList(Collections.emptyList());
        assertThat(m.isResultValid(), is(false));
    }

    @Test
    @DisplayName("should flag the result as invalid if match has winner but no games")
    public void testResultValidIfMatchHasWinnerButNoGames() {
        final Match m = match.withGameList(Collections.emptyList());
        assertThat(m.isResultValid(), is(true));
    }

    @Test
    @DisplayName("should flag the result as invalid if match has no winner but has games")
    public void testResultInvalidIfMatchHasNoWinnerButHasGames() {
        final Match m = match.withWinnerId(null);
        assertThat(m.isResultValid(), is(false));
    }

    @Test
    @DisplayName("should return score summary as positive or negative game-loser's score: 1-11")
    public void testScoreSummaryCase1_11() {
        final Game.GameBuilder gb = Game.builder().status(Game.STATUS_COMPLETE);
        final Match m = match.withGameList(List.of(
            gb.player1Score(1).player2Score(11).build()
        ));

        assertThat(m.getScoreSummary(), is(List.of(-1)));
    }

    @Test
    @DisplayName("should return score summary as positive or negative game-loser's score: 11-5,11-3")
    public void testScoreSummaryCase11_5_11_3() {
        final Game.GameBuilder gb = Game.builder().status(Game.STATUS_COMPLETE);
        final Match m = match.withGameList(List.of(
            gb.player1Score(11).player2Score(5).build(),
            gb.player1Score(11).player2Score(3).build()
        ));

        assertThat(m.getScoreSummary(), is(List.of(5, 3)));
    }

    @Test
    @DisplayName("should return score summary as positive or negative game-loser's score: 11-9,4-11,13-11")
    public void testScoreSummaryCase11_9_4_11_13_11() {
        final Game.GameBuilder gb = Game.builder().status(Game.STATUS_COMPLETE);
        final Match m = match.withGameList(List.of(
            gb.player1Score(11).player2Score(9).build(),
            gb.player1Score(4).player2Score(11).build(),
            gb.player1Score(13).player2Score(11).build()
        ));

        assertThat(m.getScoreSummary(), is(List.of(9, -4, 11)));
    }

    @Test
    @DisplayName("should be able to create the transposition, which has the same reult, but with player1 and 2 reversed")
    public void testTranspose() {
        final Game.GameBuilder gb = Game.builder().status(Game.STATUS_COMPLETE);
        final Match m = match.withGameList(List.of(
            gb.player1Score(11).player2Score(9).build(),
            gb.player1Score(4).player2Score(11).build(),
            gb.player1Score(13).player2Score(11).build()
        ));

        final Match mt = m.transpose();

        assertThat(mt.getPlayer1Id(), is(m.getPlayer2Id()));
        assertThat(mt.getPlayer2Id(), is(m.getPlayer1Id()));
        assertThat(mt.getWinnerId(), is(m.getWinnerId()));
        assertThat(mt.getScoreSummary(), is(List.of(-9, 4, -11)));
    }

    @Test
    @DisplayName("should return whether player1 is the winner: true")
    public void testIsWinForPlayer1True() {
        assertThat(TestHelper.createMatch2().isWinForPlayer1(), is(true));
    }

    @Test
    @DisplayName("should return whether player1 is the winner: false")
    public void testIsWinForPlayer1False() {
        assertThat(TestHelper.createMatch1().isWinForPlayer1(), is(false));
    }

    @Test
    @DisplayName("should return whether player1 is the winner: error because winner is invalid")
    public void testIsWinForPlayer1MatchInvalidWinner() {
        final Match m = TestHelper.createMatch1().withWinnerId(-100);
        assertThrows(IllegalStateException.class, () -> {
            m.isWinForPlayer1();
        });
    }

    @Test
    @DisplayName("should return whether player1 is the winner: error because games are tied")
    public void testIsWinForPlayer1MatchGamesTied() {
        final Match m = TestHelper.createMatch1()
            .withGameList(List.of(
                Game.builder().scores("11-5").build(),
                Game.builder().scores("8-11").build()
            ))
            .withWinnerId(null);
        assertThrows(IllegalStateException.class, () -> {
            m.isWinForPlayer1();
        });
    }

    @Test
    @DisplayName("should return whether player1 is the winner: true because player1 won more games")
    public void testIsWinForPlayer1Player1WonMoreGames() {
        final Match m = TestHelper.createMatch1()
            .withGameList(List.of(
                Game.builder().scores("11-5").build(),
                Game.builder().scores("11-8").build()
            ))
            .withWinnerId(null);

        assertThat(m.isWinForPlayer1(), is(true));
    }

    @Test
    @DisplayName("should return whether player1 is the winner: false because player2 won more games")
    public void testIsWinForPlayer1Player2WonMoreGames() {
        final Match m = TestHelper.createMatch1()
            .withGameList(List.of(
                Game.builder().scores("4-11").build(),
                Game.builder().scores("11-8").build(),
                Game.builder().scores("10-12").build()
            ))
            .withWinnerId(null);

        assertThat(m.isWinForPlayer1(), is(false));
    }

    @Test
    @DisplayName("should return number of games won by player 1")
    public void testGetNumberOfGamesWonByPlayer1() {
        assertThat(TestHelper.createMatch1().getGamesWonByPlayer1(), is(0L));
        assertThat(TestHelper.createMatch2().getGamesWonByPlayer1(), is(3L));
    }

    @Test
    @DisplayName("should return number of games won by player 2")
    public void testGetNumberOfGamesWonByPlayer2() {
        assertThat(TestHelper.createMatch1().getGamesWonByPlayer2(), is(3L));
        assertThat(TestHelper.createMatch2().getGamesWonByPlayer2(), is(1L));
    }
}
