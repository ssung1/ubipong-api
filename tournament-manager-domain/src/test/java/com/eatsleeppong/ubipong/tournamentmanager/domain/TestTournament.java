package com.eatsleeppong.ubipong.tournamentmanager.domain;

import com.eatsleeppong.ubipong.tournamentmanager.TestHelper;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class TestTournament {
    private Tournament tournament;

    @BeforeEach
    public void setupMocks() {
        tournament = TestHelper.createTournament();
    }

    @Test
    @DisplayName("should return events in the tournament")
    public void testGetEventList() {
        final List<Event> eventList = tournament.getEventList(); 
    }

    @Test
    @DisplayName("should return tournament result")
    public void testGetResult() {
        final String eventName = "xxx";
        final TournamentResult tournamentResult = tournament.getResult();

        // assertThat(tournamentResult.getTournamentName(), is(tournament.getName()));
        // assertThat(tournamentResult.getTournamentDate(), is(tournament.getTournamentDate()));

        // final List<MatchResult> matchResultList = tournamentResult.getMatchResultList();
        // assertThat(matchResultList, hasSize(2));
        // assertThat(matchResultList[0].getEventName(), is(event.getName()));
        // assertThat(matchResultList[0].getWinner(), is(patrick.getName()));
        // assertThat(matchResultList[0].getLoser(), is(spongebob.getName()));
        // assertThat(matchResultList[0].getResult(), is("3 5 1"));

        // assertThat(matchResultList[1].getEventName(), is(eventName));
        // assertThat(matchResultList[1].getWinner(), is("spongebob"));
        // assertThat(matchResultList[1].getLoser(), is("squidward"));
        // assertThat(matchResultList[1].getResult(), is("11 -5 9 9"));

        // assertThat(spongebobVsSquidward.getEventName(), is(event.getName()));
        // assertThat(spongebobVsSquidward.getWinner(), is(spongebob.getName()));
        // assertThat(spongebobVsSquidward.getLoser(), is(squidward.getName()));
        // assertThat(spongebobVsSquidward.getResult(), is("11 -5 9 9"));

    }
}