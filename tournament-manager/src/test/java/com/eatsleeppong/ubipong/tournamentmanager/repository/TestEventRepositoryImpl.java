package com.eatsleeppong.ubipong.tournamentmanager.repository;

import org.junit.jupiter.api.Test;

import com.eatsleeppong.ubipong.tournamentmanager.TestHelper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import com.eatsleeppong.ubipong.tournamentmanager.domain.EventStatus;
import com.eatsleeppong.ubipong.model.challonge.ChallongeTournament;
import com.eatsleeppong.ubipong.model.challonge.ChallongeTournamentWrapper;
import com.eatsleeppong.ubipong.entity.SpringJpaEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TestEventRepositoryImpl {
    private final Integer tournamentId = TestHelper.TOURNAMENT_ID;

    @MockBean
    private ChallongeTournamentRepository mockChallongeTournamentRepository;

    @Autowired
    private EventRepositoryImpl eventRepositoryImpl;

    private Event event;

    @BeforeEach
    public void saveEvent() {
        event = eventRepositoryImpl.save(TestHelper.createEvent());

        when(mockChallongeTournamentRepository.getTournament(event.getChallongeUrl())).thenReturn(
            TestHelper.createChallongeTournamentWrapper()
        );
    }

    @Test
    @DisplayName("should be able to add event and a corresponding tournament on challonge.com")
    public void testAddEvent() {
        final Event eventToAdd = TestHelper.createEvent();
        final Event addedEvent = eventRepositoryImpl.save(eventToAdd);

        assertThat(addedEvent.getId(), notNullValue());
        assertThat(addedEvent.getTournamentId(), is(eventToAdd.getTournamentId()));
        assertThat(addedEvent.getChallongeUrl(), is(eventToAdd.getChallongeUrl()));
        assertThat(addedEvent.getName(), is(eventToAdd.getName()));
    }

    @Test
    @DisplayName("should find an existing event in 'created' status")
    public void testFindEventStatusCreated() {
        final ChallongeTournamentWrapper challongeTournamentWrapper = TestHelper.createChallongeTournamentWrapper();
        challongeTournamentWrapper.getTournament().setState("pending");
        when(mockChallongeTournamentRepository.getTournament(event.getChallongeUrl())).thenReturn(
            challongeTournamentWrapper
        );

        final Event loadedEvent = eventRepositoryImpl.getOne(event.getId());

        assertThat(loadedEvent.getId(), is(event.getId()));
        assertThat(loadedEvent.getName(), is(event.getName()));
        assertThat(loadedEvent.getChallongeUrl(), is(event.getChallongeUrl()));
        assertThat(loadedEvent.getStatus(), is(EventStatus.CREATED));
    }

    @Test
    @DisplayName("should find an existing event in 'started' status")
    public void testFindEventStatusStarted() {
        final ChallongeTournamentWrapper challongeTournamentWrapper = TestHelper.createChallongeTournamentWrapper();
        challongeTournamentWrapper.getTournament().setState("underway");
        when(mockChallongeTournamentRepository.getTournament(event.getChallongeUrl())).thenReturn(
            challongeTournamentWrapper
        );

        final Event loadedEvent = eventRepositoryImpl.getOne(event.getId());

        assertThat(loadedEvent.getId(), is(event.getId()));
        assertThat(loadedEvent.getName(), is(event.getName()));
        assertThat(loadedEvent.getChallongeUrl(), is(event.getChallongeUrl()));
        assertThat(loadedEvent.getStatus(), is(EventStatus.STARTED));
    }

    @Test
    @DisplayName("should find an existing event in 'awaiting review' status")
    public void testFindEventStatusAwaitingReview() {
        final ChallongeTournamentWrapper challongeTournamentWrapper = TestHelper.createChallongeTournamentWrapper();
        challongeTournamentWrapper.getTournament().setState("awaiting_review");
        when(mockChallongeTournamentRepository.getTournament(event.getChallongeUrl())).thenReturn(
            challongeTournamentWrapper
        );

        final Event loadedEvent = eventRepositoryImpl.getOne(event.getId());

        assertThat(loadedEvent.getId(), is(event.getId()));
        assertThat(loadedEvent.getName(), is(event.getName()));
        assertThat(loadedEvent.getChallongeUrl(), is(event.getChallongeUrl()));
        assertThat(loadedEvent.getStatus(), is(EventStatus.AWAITING_REVIEW));
    }

    @Test
    @DisplayName("should find an existing event in 'completed' status")
    public void testFindEventStatusCompleted() {
        final ChallongeTournamentWrapper challongeTournamentWrapper = TestHelper.createChallongeTournamentWrapper();
        challongeTournamentWrapper.getTournament().setState("complete");
        when(mockChallongeTournamentRepository.getTournament(event.getChallongeUrl())).thenReturn(
            challongeTournamentWrapper
        );

        final Event loadedEvent = eventRepositoryImpl.getOne(event.getId());

        assertThat(loadedEvent.getId(), is(event.getId()));
        assertThat(loadedEvent.getName(), is(event.getName()));
        assertThat(loadedEvent.getChallongeUrl(), is(event.getChallongeUrl()));
        assertThat(loadedEvent.getStatus(), is(EventStatus.COMPLETED));
    }

    @Test
    @DisplayName("should find an existing event by challongeUrl")
    public void testFindEventByChallongeUrl() {
        final Event loadedEvent = eventRepositoryImpl.getOneByChallongeUrl(event.getChallongeUrl());

        assertThat(loadedEvent.getId(), is(event.getId()));
        assertThat(loadedEvent.getName(), is(event.getName()));
        assertThat(loadedEvent.getChallongeUrl(), is(event.getChallongeUrl()));
    }

    @Test
    @DisplayName("should find list of events of a tournament")
    public void testFindEventByTournamentId() {
        final List<Event> eventList = eventRepositoryImpl.findByTournamentId(tournamentId);

        assertThat(eventList, hasSize(1));
        assertThat(eventList.get(0).getId(), is(event.getId()));
        assertThat(eventList.get(0).getName(), is(event.getName()));
        assertThat(eventList.get(0).getChallongeUrl(), is(event.getChallongeUrl()));
    }
}
