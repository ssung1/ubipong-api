package com.eatsleeppong.ubipong.tournamentmanager.repository;

import org.junit.jupiter.api.Test;

import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TestEventRepositoryImpl {
    private final Integer tournamentId = 123;
    @MockBean
    private ChallongeTournamentRepository challongeTournamentRepository;

    @Autowired
    private EventRepositoryImpl eventRepositoryImpl;

    private Event event;

    private Event createEvent() {
        return Event.builder()
            .name("Preliminary Group 1")
            .challongeUrl("bb_201906_pg_rr_1")
            .tournamentId(tournamentId)
            .build();
    }

    @BeforeEach
    public void saveEvent() {
        event = eventRepositoryImpl.save(createEvent());
    }

    @Test
    @DisplayName("should be able to add event and a corresponding tournament on challonge.com")
    public void testAddEvent() {
        final Event eventToAdd = createEvent();
        final Event addedEvent = eventRepositoryImpl.save(eventToAdd);

        assertThat(addedEvent.getId(), notNullValue());
        assertThat(addedEvent.getTournamentId(), is(eventToAdd.getTournamentId()));
        assertThat(addedEvent.getChallongeUrl(), is(eventToAdd.getChallongeUrl()));
        assertThat(addedEvent.getName(), is(eventToAdd.getName()));
    }

    @Test
    @DisplayName("should find an existing event")
    public void testFindEvent() {
        final Event loadedEvent = eventRepositoryImpl.getOne(event.getId());

        assertThat(loadedEvent.getId(), is(event.getId()));
        assertThat(loadedEvent.getName(), is(event.getName()));
        assertThat(loadedEvent.getChallongeUrl(), is(event.getChallongeUrl()));
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
