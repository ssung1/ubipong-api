package com.eatsleeppong.ubipong.tournamentmanager.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

import com.eatsleeppong.ubipong.model.challonge.ChallongeParticipant;
import com.eatsleeppong.ubipong.model.challonge.ChallongeParticipantWrapper;
import com.eatsleeppong.ubipong.tournamentmanager.controller.mapper.PlayerMapper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.client.RestTemplate;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class TestChallongeParticipantRepository {
    private final Integer spongebobId = 1;
    private final String spongebobName = "spongebob";
    private final Integer patrickId = 2;
    private final String patrickName = "patrick";
    private final Integer squidwardId = 3;
    private final String squidwardName = "squidward";
    private final String challongeUrl = "esp_201903_pg_rr_1";
    private final String host = "api.challonge.com";
    private final String apiKey = "this-is-api-key-from-challonge.com";
    private final PlayerMapper playerMapper = new PlayerMapper();
    private final RestTemplate mockRestTemplate = mock(RestTemplate.class);

    private ChallongeParticipantRepository challongeParticipantRepository 
        = new ChallongeParticipantRepository(host, apiKey, playerMapper, mockRestTemplate);

    private ChallongeParticipantWrapper[] createParticipantWrapperList() {
        ChallongeParticipant spongebob = new ChallongeParticipant();
        spongebob.setId(spongebobId);
        spongebob.setDisplayName(spongebobName);

        ChallongeParticipant patrick = new ChallongeParticipant();
        patrick.setId(patrickId);
        patrick.setDisplayName(patrickName);

        ChallongeParticipant squidward = new ChallongeParticipant();
        squidward.setId(squidwardId);
        squidward.setDisplayName(squidwardName);

        return Stream.of(spongebob, patrick, squidward)
            .map(p -> {
                ChallongeParticipantWrapper pw =
                    new ChallongeParticipantWrapper();
                pw.setParticipant(p);
                return pw;
            })
            .toArray(ChallongeParticipantWrapper[]::new);
    }

    @BeforeEach
    public void setupMocks() {
        when(mockRestTemplate.getForObject(any(), eq(ChallongeParticipantWrapper[].class)))
            .thenReturn(createParticipantWrapperList());
    }

    @Test
    @DisplayName("should be able to find a list of players by challongeUrl")
    public void testFindByChallongeUrl() throws Exception {
        final ArgumentCaptor<URI> argument = ArgumentCaptor.forClass(URI.class);
        when(mockRestTemplate.getForObject(argument.capture(),
            eq(ChallongeParticipantWrapper[].class)))
            .thenReturn(createParticipantWrapperList());

        List<Player> playerList = 
            challongeParticipantRepository.findByChallongeUrl(challongeUrl);

        assertThat(argument.getValue().toString(), containsString(host));

        assertThat(playerList, hasSize(3));

        assertThat(playerList.get(0).getId(), is(spongebobId));
        assertThat(playerList.get(0).getName(), is(spongebobName));

        assertThat(playerList.get(1).getId(), is(patrickId));
        assertThat(playerList.get(1).getName(), is(patrickName));

        assertThat(playerList.get(2).getId(), is(squidwardId));
        assertThat(playerList.get(2).getName(), is(squidwardName));
    }
}