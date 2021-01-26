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
    @DisplayName("should return a map of playerer ID and their index (seeding)")
    public void testGetPlayerIndexMap() {
        Map<Integer, Integer> indexMap = event.getPlayerIndexMap();

        assertThat(indexMap.get(spongebobId), is(0));
        assertThat(indexMap.get(patrickId), is(1));
        assertThat(indexMap.get(squidwardId), is(2));
    }
}
