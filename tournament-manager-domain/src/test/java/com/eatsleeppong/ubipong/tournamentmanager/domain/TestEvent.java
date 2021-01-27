package com.eatsleeppong.ubipong.tournamentmanager.domain;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class TestEvent {
    final String challongeUrl = "esp_201903_pg_rr_1";
    final Integer spongebobId = 1;
    final String spongebobName = "spongebob";
    final Player spongebob = Player.builder()
        .id(spongebobId)
        .name(spongebobName)
        .build();

    final Integer patrickId = 2;
    final String patrickName = "patrick";
    final Player patrick = Player.builder()
        .id(patrickId)
        .name(patrickName)
        .build();

    final Integer squidwardId = 3;
    final String squidwardName = "squidward";
    final Player squidward = Player.builder()
        .id(squidwardId)
        .name(squidwardName)
        .build();

    private final PlayerRepository mockPlayerRepository = mock(PlayerRepository.class);
    private final Event event = Event.builder()
        .id(0)
        .name("Preliminary Group 1")
        .challongeUrl(challongeUrl)
        .playerRepository(mockPlayerRepository)
        .build();

    @BeforeEach
    public void setupMocks() {
        when(mockPlayerRepository.findByChallongeUrl(challongeUrl))
            .thenReturn(List.of(spongebob, patrick, squidward));
    }

    @Test
    @DisplayName("should return a list of players with seeding populated")
    public void testGetPlayerList() {
        List<Player> playerList = event.getPlayerList();

        assertThat(playerList.get(0).getId(), is(spongebobId));
        assertThat(playerList.get(0).getName(), is(spongebobName));
        assertThat(playerList.get(0).getEventSeed(), is(0));
        assertThat(playerList.get(1).getId(), is(patrickId));
        assertThat(playerList.get(1).getName(), is(patrickName));
        assertThat(playerList.get(1).getEventSeed(), is(1));
        assertThat(playerList.get(2).getId(), is(squidwardId));
        assertThat(playerList.get(2).getName(), is(squidwardName));
        assertThat(playerList.get(2).getEventSeed(), is(2));
    }

    @Test
    @DisplayName("should return a map of players keyed by ID")
    public void testGetPlayerMap() {
        Map<Integer, Player> playerMap = event.getPlayerMap();

        assertThat(playerMap.get(spongebobId).getId(), is(spongebobId));
        assertThat(playerMap.get(spongebobId).getName(), is(spongebobName));
        assertThat(playerMap.get(spongebobId).getEventSeed(), is(0));
        assertThat(playerMap.get(patrickId).getId(), is(patrickId));
        assertThat(playerMap.get(patrickId).getName(), is(patrickName));
        assertThat(playerMap.get(patrickId).getEventSeed(), is(1));
        assertThat(playerMap.get(squidwardId).getId(), is(squidwardId));
        assertThat(playerMap.get(squidwardId).getName(), is(squidwardName));
        assertThat(playerMap.get(squidwardId).getEventSeed(), is(2));
    }
}
