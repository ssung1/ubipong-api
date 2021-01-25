package com.eatsleeppong.ubipong.tournamentmanager.repository;

import org.junit.jupiter.api.Test;

import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.DisplayName;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TestEventRepositoryImpl {
    @MockBean
    private ChallongeTournamentRepository challongeTournamentRepository;

    @Autowired
    private EventRepositoryImpl eventRepositoryImpl;

    private Event createEvent() {
        return Event.builder()
            .name("Preliminary Group 1")
            .challongeUrl("bb_201906_pg_rr_1")
            .tournamentId(1)
            .build();
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
        final Event eventToAdd = createEvent();
        final Event addedEvent = eventRepositoryImpl.save(eventToAdd);

        final Event loadedEvent = eventRepositoryImpl.getOne(addedEvent.getId());

        assertThat(loadedEvent.getId(), is(addedEvent.getId()));
        assertThat(loadedEvent.getName(), is(addedEvent.getName()));
        assertThat(loadedEvent.getChallongeUrl(), is(addedEvent.getChallongeUrl()));
    }

    @Test
    @DisplayName("should find an existing event by challongeUrl")
    public void testFindEventByChallongeUrl() {
        final Event eventToAdd = createEvent();
        final Event addedEvent = eventRepositoryImpl.save(eventToAdd);

        final Event loadedEvent = eventRepositoryImpl.getOneByChallongeUrl(addedEvent.getChallongeUrl());

        assertThat(loadedEvent.getId(), is(addedEvent.getId()));
        assertThat(loadedEvent.getName(), is(addedEvent.getName()));
        assertThat(loadedEvent.getChallongeUrl(), is(addedEvent.getChallongeUrl()));
    }
}
