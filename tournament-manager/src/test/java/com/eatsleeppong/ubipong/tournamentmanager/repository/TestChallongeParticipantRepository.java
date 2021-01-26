package com.eatsleeppong.ubipong.tournamentmanager.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestChallongeParticipantRepository {
    private final String host = "http://api.challonge.com";
    private final String apiKey = "this-is-api-key-from-challonge.com";
    private final PlayerMapper playerMapper = new PlayerMapper();

    private ChallongeParticipantRepository challongeParticipantRepository 
        = new ChallongeParticipantRepository(host, apiKey, playerMapper);

    @Test
    @DisplayName("should be able to find a list of players by challongeUrl")
    public void testFindByChallongeUrl() throws Exception {
    }
}