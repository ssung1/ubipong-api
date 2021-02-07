package com.eatsleeppong.ubipong.tournamentmanager.domain;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import com.eatsleeppong.ubipong.tournamentmanager.TestHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class TestEvent {
    private final Player spongebob = TestHelper.createPlayerSpongebob();
    private final Player patrick = TestHelper.createPlayerPatrick();
    private final Player squidward = TestHelper.createPlayerSquidward();

    private Event event;

    @BeforeEach
    public void setupMocks() {
        event = TestHelper.createEvent();
    }

    @Test
    @DisplayName("should return a list of players with seeding populated")
    public void testGetPlayerList() {
        List<Player> playerList = event.getPlayerList();

        assertThat(playerList.get(0).getId(), is(spongebob.getId()));
        assertThat(playerList.get(0).getName(), is(spongebob.getName()));
        assertThat(playerList.get(0).getEventSeed(), is(0));
        assertThat(playerList.get(1).getId(), is(patrick.getId()));
        assertThat(playerList.get(1).getName(), is(patrick.getName()));
        assertThat(playerList.get(1).getEventSeed(), is(1));
        assertThat(playerList.get(2).getId(), is(squidward.getId()));
        assertThat(playerList.get(2).getName(), is(squidward.getName()));
        assertThat(playerList.get(2).getEventSeed(), is(2));
    }

    @Test
    @DisplayName("should return a map of players keyed by ID")
    public void testGetPlayerMap() {
        Map<Integer, Player> playerMap = event.getPlayerMap();

        assertThat(playerMap.get(spongebob.getId()).getId(), is(spongebob.getId()));
        assertThat(playerMap.get(spongebob.getId()).getName(), is(spongebob.getName()));
        assertThat(playerMap.get(spongebob.getId()).getEventSeed(), is(0));
        assertThat(playerMap.get(patrick.getId()).getId(), is(patrick.getId()));
        assertThat(playerMap.get(patrick.getId()).getName(), is(patrick.getName()));
        assertThat(playerMap.get(patrick.getId()).getEventSeed(), is(1));
        assertThat(playerMap.get(squidward.getId()).getId(), is(squidward.getId()));
        assertThat(playerMap.get(squidward.getId()).getName(), is(squidward.getName()));
        assertThat(playerMap.get(squidward.getId()).getEventSeed(), is(2));
    }

    @Test
    @DisplayName("should return a list of matches")
    public void testGetMatchList() {
        final List<Match> matchList = event.getMatchList();

        final Match spongebobVsPatrick = matchList.get(0);
        assertThat(spongebobVsPatrick.getPlayer1Id(), is(spongebob.getId()));
        assertThat(spongebobVsPatrick.getPlayer2Id(), is(patrick.getId()));
        assertThat(spongebobVsPatrick.isResultValid(), is(true));
        assertThat(spongebobVsPatrick.getResultCode(), is(Match.RESULT_CODE_WIN_BY_PLAYING));
        assertThat(spongebobVsPatrick.getWinnerId(), is(patrick.getId()));
        assertThat(spongebobVsPatrick.getGame(0).getPlayer1Score(), is(3));
        assertThat(spongebobVsPatrick.getGame(0).getPlayer2Score(), is(11));
        assertThat(spongebobVsPatrick.getGame(0).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(spongebobVsPatrick.getGame(1).getPlayer1Score(), is(5));
        assertThat(spongebobVsPatrick.getGame(1).getPlayer2Score(), is(11));
        assertThat(spongebobVsPatrick.getGame(1).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(spongebobVsPatrick.getGame(2).getPlayer1Score(), is(1));
        assertThat(spongebobVsPatrick.getGame(2).getPlayer2Score(), is(11));
        assertThat(spongebobVsPatrick.getGame(2).getStatus(), is(Game.STATUS_COMPLETE));

        final Match spongebobVsSquidward = matchList.get(1);
        assertThat(spongebobVsSquidward.getPlayer1Id(), is(spongebob.getId()));
        assertThat(spongebobVsSquidward.getPlayer2Id(), is(squidward.getId()));
        assertThat(spongebobVsSquidward.isResultValid(), is(true));
        assertThat(spongebobVsSquidward.getResultCode(), is(Match.RESULT_CODE_WIN_BY_PLAYING));
        assertThat(spongebobVsSquidward.getWinnerId(), is(spongebob.getId()));
        assertThat(spongebobVsSquidward.getGame(0).getPlayer1Score(), is(13));
        assertThat(spongebobVsSquidward.getGame(0).getPlayer2Score(), is(11));
        assertThat(spongebobVsSquidward.getGame(0).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(spongebobVsSquidward.getGame(1).getPlayer1Score(), is(5));
        assertThat(spongebobVsSquidward.getGame(1).getPlayer2Score(), is(11));
        assertThat(spongebobVsSquidward.getGame(1).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(spongebobVsSquidward.getGame(2).getPlayer1Score(), is(11));
        assertThat(spongebobVsSquidward.getGame(2).getPlayer2Score(), is(9));
        assertThat(spongebobVsSquidward.getGame(2).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(spongebobVsSquidward.getGame(3).getPlayer1Score(), is(11));
        assertThat(spongebobVsSquidward.getGame(3).getPlayer2Score(), is(9));
        assertThat(spongebobVsSquidward.getGame(3).getStatus(), is(Game.STATUS_COMPLETE));

        final Match patrickVsSquidward = matchList.get(2);
        assertThat(patrickVsSquidward.getPlayer1Id(), is(patrick.getId()));
        assertThat(patrickVsSquidward.getPlayer2Id(), is(squidward.getId()));
        assertThat(patrickVsSquidward.isResultValid(), is(false));
    }

    @Test
    @DisplayName("should return a list of match results")
    public void testGetMatchResultList() {
        // for this test, we cannot have any pending matches
        when(event.getMatchRepository().findByChallongeUrl(event.getChallongeUrl()))
            .thenReturn(List.of(
                TestHelper.createMatch1(), TestHelper.createMatch2()
            ));

        final List<MatchResult> matchResultList = event.getMatchResultList();
        assertThat(matchResultList, hasSize(2));

        final MatchResult spongebobVsPatrick = matchResultList.get(0);
        assertThat(spongebobVsPatrick.getEventName(), is(event.getName()));
        assertThat(spongebobVsPatrick.getWinnerName(), is(patrick.getName()));
        assertThat(spongebobVsPatrick.getWinnerReferenceId(), is(String.valueOf(patrick.getUsattNumber())));
        assertThat(spongebobVsPatrick.getLoserName(), is(spongebob.getName()));
        assertThat(spongebobVsPatrick.getLoserReferenceId(), is(String.valueOf(spongebob.getUsattNumber())));
        assertThat(spongebobVsPatrick.getScoreSummary(), is(List.of(3, 5 ,1)));

        final MatchResult spongebobVsSquidward = matchResultList.get(1);
        assertThat(spongebobVsSquidward.getEventName(), is(event.getName()));
        assertThat(spongebobVsSquidward.getWinnerName(), is(spongebob.getName()));
        assertThat(spongebobVsSquidward.getWinnerReferenceId(), is(String.valueOf(spongebob.getUsattNumber())));
        assertThat(spongebobVsSquidward.getLoserName(), is(squidward.getName()));
        assertThat(spongebobVsSquidward.getLoserReferenceId(), is(String.valueOf(squidward.getUsattNumber())));
        assertThat(spongebobVsSquidward.getScoreSummary(), is(List.of(11, -5, 9, 9)));
    }

    @Test
    @DisplayName("should return a list of match sheets")
    public void testGetMatchSheetList() {
        final Match spongebobVsPatrick = TestHelper.createMatch1();
        final Match matchWithNoPlayers = Match.builder()
            .player1Id(null)
            .player2Id(null)
            .build();
        when(event.getMatchRepository().findByChallongeUrl(event.getChallongeUrl()))
            .thenReturn(List.of(spongebobVsPatrick, matchWithNoPlayers));

        final List<MatchSheet> matchSheetList = event.getMatchSheetList();
        assertThat(matchSheetList, hasSize(1));

        final MatchSheet spongebobVsPatrickSheet = matchSheetList.get(0);
        assertThat(spongebobVsPatrickSheet.getEventName(), is(event.getName()));
        assertThat(spongebobVsPatrickSheet.getMatchId(), is(spongebobVsPatrick.getId()));
        assertThat(spongebobVsPatrickSheet.getPlayer1UsattNumber(), is(spongebob.getUsattNumber()));
        assertThat(spongebobVsPatrickSheet.getPlayer1Name(), is(spongebob.getName()));
        assertThat(spongebobVsPatrickSheet.getPlayer1Seed(), is(0));
        assertThat(spongebobVsPatrickSheet.getPlayer2UsattNumber(), is(patrick.getUsattNumber()));
        assertThat(spongebobVsPatrickSheet.getPlayer2Name(), is(patrick.getName()));
        assertThat(spongebobVsPatrickSheet.getPlayer2Seed(), is(1));
    }
}
