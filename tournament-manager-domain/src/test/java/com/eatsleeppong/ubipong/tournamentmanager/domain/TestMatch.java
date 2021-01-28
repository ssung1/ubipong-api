package com.eatsleeppong.ubipong.tournamentmanager.domain;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;

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
    @DisplayName("should flag the result as invalid if match is not complete")
    public void testResultInvalidIfMatchIncomplete() {
        final Match m = match.withStatus(Match.STATUS_INCOMPLETE);
        assertThat(m.isResultValid(), is(false));
    }

    @Test
    @DisplayName("should flag the result as valid if match is complete")
    public void testResultValidIfMatchComplete() {
        final Match m = match.withStatus(Match.STATUS_COMPLETE);
        assertThat(m.isResultValid(), is(true));
    }

    @Test
    @DisplayName("should return score summary as positive or negative game-loser's score: 1-11")
    public void testScoreSummaryCase1_11() {
        final Game.GameBuilder gb = Game.builder().status(Game.STATUS_COMPLETE);
        final Match m = match.withGameList(List.of(
            gb.player1Score(1).player2Score(11).build()
        ));

        assertThat(m.getScoreSummary(), is("-1"));
    }

    @Test
    @DisplayName("should return score summary as positive or negative game-loser's score: 11-5,11-3")
    public void testScoreSummaryCase11_5_11_3() {
        final Game.GameBuilder gb = Game.builder().status(Game.STATUS_COMPLETE);
        final Match m = match.withGameList(List.of(
            gb.player1Score(11).player2Score(5).build(),
            gb.player1Score(11).player2Score(3).build()
        ));

        assertThat(m.getScoreSummary(), is("5 3"));
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

        assertThat(m.getScoreSummary(), is("9 -4 11"));
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
        assertThat(mt.getScoreSummary(), is("-9 4 -11"));
    }

    @Test
    @DisplayName("should return whether player1 is the winner: true")
    public void testIsWinForPlayer1True() {
        assertThat(TestHelper.createMatch1().isWinForPlayer1(), is(true));
    }

    @Test
    @DisplayName("should return whether player1 is the winner: false")
    @Disabled()
    public void testIsWinForPlayer1False() {
        assertThat(TestHelper.createMatch1().isWinForPlayer1(), is(false));
    }

    @Test
    @DisplayName("should flag the result as invalid if the scores are not valid")
    @Disabled("will do this later")
    public void testResultInvalidIfScoreInvalid() {

    }
}