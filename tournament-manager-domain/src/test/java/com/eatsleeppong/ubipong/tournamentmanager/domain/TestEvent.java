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

    Event event;

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
        List<Match> matchList = event.getMatchList();

        assertThat(matchList.get(0).getPlayer1Id(), is(spongebob.getId()));
        assertThat(matchList.get(0).getPlayer2Id(), is(patrick.getId()));
        assertThat(matchList.get(0).getStatus(), is(Match.STATUS_COMPLETE));
        assertThat(matchList.get(0).getResultCode(), is(Match.RESULT_CODE_WIN_BY_PLAYING));
        assertThat(matchList.get(0).getGame(0).getPlayer1Score(), is(11));
        assertThat(matchList.get(0).getGame(0).getPlayer2Score(), is(3));
        assertThat(matchList.get(0).getGame(0).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(0).getGame(1).getPlayer1Score(), is(11));
        assertThat(matchList.get(0).getGame(1).getPlayer2Score(), is(5));
        assertThat(matchList.get(0).getGame(1).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(0).getGame(2).getPlayer1Score(), is(11));
        assertThat(matchList.get(0).getGame(2).getPlayer2Score(), is(1));
        assertThat(matchList.get(0).getGame(2).getStatus(), is(Game.STATUS_COMPLETE));

        assertThat(matchList.get(1).getPlayer1Id(), is(spongebob.getId()));
        assertThat(matchList.get(1).getPlayer2Id(), is(squidward.getId()));
        assertThat(matchList.get(1).getStatus(), is(Match.STATUS_COMPLETE));
        assertThat(matchList.get(1).getResultCode(), is(Match.RESULT_CODE_WIN_BY_PLAYING));
        assertThat(matchList.get(1).getGame(0).getPlayer1Score(), is(13));
        assertThat(matchList.get(1).getGame(0).getPlayer2Score(), is(11));
        assertThat(matchList.get(1).getGame(0).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(1).getGame(1).getPlayer1Score(), is(5));
        assertThat(matchList.get(1).getGame(1).getPlayer2Score(), is(11));
        assertThat(matchList.get(1).getGame(1).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(1).getGame(2).getPlayer1Score(), is(11));
        assertThat(matchList.get(1).getGame(2).getPlayer2Score(), is(9));
        assertThat(matchList.get(1).getGame(2).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(1).getGame(3).getPlayer1Score(), is(11));
        assertThat(matchList.get(1).getGame(3).getPlayer2Score(), is(9));
        assertThat(matchList.get(1).getGame(3).getStatus(), is(Game.STATUS_COMPLETE));

        assertThat(matchList.get(2).getPlayer1Id(), is(patrick.getId()));
        assertThat(matchList.get(2).getPlayer2Id(), is(squidward.getId()));
        assertThat(matchList.get(2).getStatus(), is(Match.STATUS_COMPLETE));
        assertThat(matchList.get(2).getResultCode(), is(Match.RESULT_CODE_WIN_BY_PLAYING));
        assertThat(matchList.get(2).getGame(0).getPlayer1Score(), is(11));
        assertThat(matchList.get(2).getGame(0).getPlayer2Score(), is(3));
        assertThat(matchList.get(2).getGame(0).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(2).getGame(1).getPlayer1Score(), is(11));
        assertThat(matchList.get(2).getGame(1).getPlayer2Score(), is(3));
        assertThat(matchList.get(2).getGame(1).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(2).getGame(2).getPlayer1Score(), is(11));
        assertThat(matchList.get(2).getGame(2).getPlayer2Score(), is(3));
        assertThat(matchList.get(2).getGame(2).getStatus(), is(Game.STATUS_COMPLETE));
    }

    @Test
    @DisplayName("should return a list of matches arranged for reporting")
    @Disabled
    public void testGetMatchListForReporting() {
        List<Match> matchList = event.getMatchListForReporting();

        assertThat(matchList.get(0).getPlayer1Id(), is(spongebob.getId()));
        assertThat(matchList.get(0).getPlayer2Id(), is(patrick.getId()));
        assertThat(matchList.get(0).getStatus(), is(Match.STATUS_COMPLETE));
        assertThat(matchList.get(0).getResultCode(), is(Match.RESULT_CODE_WIN_BY_PLAYING));
        assertThat(matchList.get(0).getGame(0).getPlayer1Score(), is(11));
        assertThat(matchList.get(0).getGame(0).getPlayer2Score(), is(3));
        assertThat(matchList.get(0).getGame(0).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(0).getGame(1).getPlayer1Score(), is(11));
        assertThat(matchList.get(0).getGame(1).getPlayer2Score(), is(5));
        assertThat(matchList.get(0).getGame(1).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(0).getGame(2).getPlayer1Score(), is(11));
        assertThat(matchList.get(0).getGame(2).getPlayer2Score(), is(1));
        assertThat(matchList.get(0).getGame(2).getStatus(), is(Game.STATUS_COMPLETE));

        assertThat(matchList.get(1).getPlayer1Id(), is(spongebob.getId()));
        assertThat(matchList.get(1).getPlayer2Id(), is(squidward.getId()));
        assertThat(matchList.get(1).getStatus(), is(Match.STATUS_COMPLETE));
        assertThat(matchList.get(1).getResultCode(), is(Match.RESULT_CODE_WIN_BY_PLAYING));
        assertThat(matchList.get(1).getGame(0).getPlayer1Score(), is(13));
        assertThat(matchList.get(1).getGame(0).getPlayer2Score(), is(11));
        assertThat(matchList.get(1).getGame(0).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(1).getGame(1).getPlayer1Score(), is(5));
        assertThat(matchList.get(1).getGame(1).getPlayer2Score(), is(11));
        assertThat(matchList.get(1).getGame(1).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(1).getGame(2).getPlayer1Score(), is(11));
        assertThat(matchList.get(1).getGame(2).getPlayer2Score(), is(9));
        assertThat(matchList.get(1).getGame(2).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(1).getGame(3).getPlayer1Score(), is(11));
        assertThat(matchList.get(1).getGame(3).getPlayer2Score(), is(9));
        assertThat(matchList.get(1).getGame(3).getStatus(), is(Game.STATUS_COMPLETE));

        assertThat(matchList.get(2).getPlayer1Id(), is(patrick.getId()));
        assertThat(matchList.get(2).getPlayer2Id(), is(squidward.getId()));
        assertThat(matchList.get(2).getStatus(), is(Match.STATUS_COMPLETE));
        assertThat(matchList.get(2).getResultCode(), is(Match.RESULT_CODE_WIN_BY_PLAYING));
        assertThat(matchList.get(2).getGame(0).getPlayer1Score(), is(11));
        assertThat(matchList.get(2).getGame(0).getPlayer2Score(), is(3));
        assertThat(matchList.get(2).getGame(0).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(2).getGame(1).getPlayer1Score(), is(11));
        assertThat(matchList.get(2).getGame(1).getPlayer2Score(), is(3));
        assertThat(matchList.get(2).getGame(1).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(2).getGame(2).getPlayer1Score(), is(11));
        assertThat(matchList.get(2).getGame(2).getPlayer2Score(), is(3));
        assertThat(matchList.get(2).getGame(2).getStatus(), is(Game.STATUS_COMPLETE));
    }
}
