package com.eatsleeppong.ubipong.tournamentmanager.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;

import com.eatsleeppong.ubipong.tournamentmanager.AdapterContextConfiguration;
import com.eatsleeppong.ubipong.tournamentmanager.AdapterTestConfiguration;
import com.eatsleeppong.ubipong.tournamentmanager.TestHelper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import com.eatsleeppong.ubipong.tournamentmanager.domain.EventStatus;
import com.eatsleeppong.ubipong.model.challonge.ChallongeTournamentWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

@SpringBootTest(classes = AdapterContextConfiguration.class)
@Import(AdapterTestConfiguration.class)
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
    @DisplayName("should be able to add event and a corresponding tournament on challonge.com if it doesn't exist")
    public void testAddEventAndChallongeEventIfNotExists() {
        reset(mockChallongeTournamentRepository);
        final Event eventToAdd = TestHelper.createEvent();
        final Event addedEvent = eventRepositoryImpl.save(eventToAdd);

        final ArgumentCaptor<ChallongeTournamentWrapper> challongeTournamentWrapperArgumentCaptor = 
            ArgumentCaptor.forClass(ChallongeTournamentWrapper.class);
        assertThat(addedEvent.getId(), notNullValue());
        assertThat(addedEvent.getTournamentId(), is(eventToAdd.getTournamentId()));
        assertThat(addedEvent.getChallongeUrl(), is(eventToAdd.getChallongeUrl()));
        assertThat(addedEvent.getName(), is(eventToAdd.getName()));

        verify(mockChallongeTournamentRepository).createTournament(
            challongeTournamentWrapperArgumentCaptor.capture());

        assertThat(challongeTournamentWrapperArgumentCaptor.getValue().getTournament().getUrl(),
            is(eventToAdd.getChallongeUrl()));
    }

    @Test
    @DisplayName("should be able to add event and ignore error if corresponding tournament on challonge.com exists")
    public void testAddEventOnlyIfChallongeEventExists() {
        reset(mockChallongeTournamentRepository);
        final Event eventToAdd = TestHelper.createEvent();

        final ArgumentCaptor<ChallongeTournamentWrapper> challongeTournamentWrapperArgumentCaptor = 
            ArgumentCaptor.forClass(ChallongeTournamentWrapper.class);

        // 422 Unprocessable Entity: [{"errors":["URL is already taken"]}]
        when(mockChallongeTournamentRepository.createTournament(
            challongeTournamentWrapperArgumentCaptor.capture()))
        .thenThrow(new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "URL is already taken"));

        final Event addedEvent = eventRepositoryImpl.save(eventToAdd);

        assertThat(addedEvent.getId(), notNullValue());
        assertThat(addedEvent.getTournamentId(), is(eventToAdd.getTournamentId()));
        assertThat(addedEvent.getChallongeUrl(), is(eventToAdd.getChallongeUrl()));
        assertThat(addedEvent.getName(), is(eventToAdd.getName()));

        verify(mockChallongeTournamentRepository).createTournament(
            challongeTournamentWrapperArgumentCaptor.capture());

        assertThat(challongeTournamentWrapperArgumentCaptor.getValue().getTournament().getUrl(),
            is(eventToAdd.getChallongeUrl()));
    }

    @ParameterizedTest(name = "should be able to find event with status {1}")
    @CsvSource({
        "pending, CREATED",
        "underway, STARTED",
        "awaiting_review, AWAITING_REVIEW",
        "complete, COMPLETED",
    })
    public void testFindEvent(final String challongeStatus, final EventStatus eventStatus) {
        final ChallongeTournamentWrapper challongeTournamentWrapper = TestHelper.createChallongeTournamentWrapper();
        challongeTournamentWrapper.getTournament().setState(challongeStatus);
        when(mockChallongeTournamentRepository.getTournament(event.getChallongeUrl())).thenReturn(
            challongeTournamentWrapper
        );

        final Event loadedEvent = eventRepositoryImpl.getOne(event.getId());

        assertThat(loadedEvent.getId(), is(event.getId()));
        assertThat(loadedEvent.getName(), is(event.getName()));
        assertThat(loadedEvent.getChallongeUrl(), is(event.getChallongeUrl()));
        assertThat(loadedEvent.getStatus(), is(eventStatus));
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
