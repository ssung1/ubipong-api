package com.eatsleeppong.ubipong.tournamentmanager.domain;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import com.eatsleeppong.ubipong.tournamentmanager.TestHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class TestEvent {
    final Player spongebob = TestHelper.createPlayerSpongebob();
    final Player patrick = TestHelper.createPlayerPatrick();
    final Player squidward = TestHelper.createPlayerSquidward();

    private final PlayerRepository mockPlayerRepository = mock(PlayerRepository.class);
    private final MatchRepository mockMatchRepository = mock(MatchRepository.class);

    private final Event event = TestHelper.createEvent();

    @BeforeEach
    public void setupMocks() {
        when(event.getPlayerRepository().findByChallongeUrl(event.getChallongeUrl()))
            .thenReturn(List.of(spongebob, patrick, squidward));
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

    }
}
