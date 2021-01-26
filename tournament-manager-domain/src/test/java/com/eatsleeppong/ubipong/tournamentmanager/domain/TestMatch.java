package com.eatsleeppong.ubipong.tournamentmanager.domain;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class TestMatch {
    private final int spongebobId = 1;
    private final int patrickId = 1;

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
    @DisplayName("should flag the result as invalid if the scores are not valid")
    @Disabled("will do this later")
    public void testResultInvalidIfScoreInvalid() {

    }
}