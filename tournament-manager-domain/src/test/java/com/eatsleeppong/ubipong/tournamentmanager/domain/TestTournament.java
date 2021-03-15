package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.*;

public class TestTournament {
    private final Player spongebob = TestHelper.createPlayerSpongebob();
    private final Player patrick = TestHelper.createPlayerPatrick();
    private final Player squidward = TestHelper.createPlayerSquidward();

    private Tournament tournament;

    @BeforeEach
    public void setupMocks() {
        tournament = TestHelper.createTournament();
    }

    @Test
    @DisplayName("should return events in the tournament")
    public void testGetEventList() {
        final List<Event> eventList = tournament.getEventList(); 

        assertThat(eventList, hasSize(1));
        assertThat(eventList.get(0).getTournamentId(), is(tournament.getId()));
        assertThat(eventList.get(0).getName(), is(TestHelper.EVENT_NAME));
        assertThat(eventList.get(0).getChallongeUrl(), is(TestHelper.CHALLONGE_URL));
    }

    @Test
    @DisplayName("should return tournament result")
    public void testGetResult() {
        // need to override event list because we cannot get result for incomplete
        // matches
        final Event event = tournament.getEventList().get(0);
        when(event.getMatchRepository().findByChallongeUrl(event.getChallongeUrl()))
            .thenReturn(List.of(
                TestHelper.createMatch1(), TestHelper.createMatch2()
            ));

        final TournamentResult tournamentResult = tournament.getResult();

        assertThat(tournamentResult.getTournamentName(), is(tournament.getName()));
        assertThat(tournamentResult.getTournamentDate(), is(tournament.getTournamentDate()));

        final List<MatchResult> matchResultList = tournamentResult.getMatchResultList();
        assertThat(matchResultList, hasSize(2));
        assertThat(matchResultList.get(0).getEventName(), is(event.getName()));
        assertThat(matchResultList.get(0).getWinnerName(), is(patrick.getName()));
        assertThat(matchResultList.get(0).getWinnerReferenceId(), is(String.valueOf(patrick.getUsattNumber())));
        assertThat(matchResultList.get(0).getLoserName(), is(spongebob.getName()));
        assertThat(matchResultList.get(0).getLoserReferenceId(), is(String.valueOf(spongebob.getUsattNumber())));
        assertThat(matchResultList.get(0).getScoreSummary(), is(List.of(3, 5, 1)));

        assertThat(matchResultList.get(1).getEventName(), is(event.getName()));
        assertThat(matchResultList.get(1).getWinnerName(), is(spongebob.getName()));
        assertThat(matchResultList.get(1).getWinnerReferenceId(), is(String.valueOf(spongebob.getUsattNumber())));
        assertThat(matchResultList.get(1).getLoserName(), is(squidward.getName()));
        assertThat(matchResultList.get(1).getLoserReferenceId(), is(String.valueOf(squidward.getUsattNumber())));
        assertThat(matchResultList.get(1).getScoreSummary(), is(List.of(11, -5, 9, 9)));
    }
}