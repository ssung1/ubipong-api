package com.eatsleeppong.ubipong.tournamentmanger.dto.request;

import com.eatsleeppong.ubipong.tournamentmanager.dto.request.TournamentRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class TestTournamentRequest {

    @Test
    @DisplayName("should build a Tournament Request")
    public void testShouldBuildTournamentRequest() {
        final TournamentRequest tournamentRequest =
            TournamentRequest.builder().build();

        assertThat(tournamentRequest, notNullValue());
    }
}
