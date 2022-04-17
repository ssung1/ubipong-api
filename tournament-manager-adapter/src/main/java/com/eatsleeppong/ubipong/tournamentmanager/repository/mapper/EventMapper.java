package com.eatsleeppong.ubipong.tournamentmanager.repository.mapper;

import java.time.Instant;
import java.util.Date;

import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import com.eatsleeppong.ubipong.tournamentmanager.domain.EventStatus;
import com.eatsleeppong.ubipong.tournamentmanager.domain.MatchRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.PlayerRepository;
import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaEvent;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component("repositoryEventMapper")
@AllArgsConstructor
public class EventMapper {
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;

    public SpringJpaEvent mapEventToSpringJpaEvent(final Event event) {
        final SpringJpaEvent springJpaEvent = new SpringJpaEvent();
        springJpaEvent.setName(event.getName());
        springJpaEvent.setTournamentId(event.getTournamentId());
        springJpaEvent.setId(event.getId());
        springJpaEvent.setChallongeUrl(event.getChallongeUrl());
        springJpaEvent.setStartTime(mapInstantToDate(event.getStartTime()));

        return springJpaEvent;
    }

    public Event mapSpringJpaEventToEvent(final SpringJpaEvent springJpaEvent) {
        return Event.builder()
            .challongeUrl(springJpaEvent.getChallongeUrl())
            .id(springJpaEvent.getId())
            .tournamentId(springJpaEvent.getTournamentId())
            .name(springJpaEvent.getName())
            .playerRepository(playerRepository)
            .matchRepository(matchRepository)
            .startTime(mapDateToInstant(springJpaEvent.getStartTime()))
            .build();
    }

    private Date mapInstantToDate(final Instant instant) {
        if (instant == null) {
            return null;
        }
        return Date.from(instant);
    }

    private Instant mapDateToInstant(final Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant();
    }

    public EventStatus mapChallongeTournamentStateToEventStatus(final String challongeTournamentState) {
        if ("pending".equals(challongeTournamentState)) {
            return EventStatus.CREATED;
        } else if ("underway".equals(challongeTournamentState)) {
            return EventStatus.STARTED;
        } else if ("awaiting_review".equals(challongeTournamentState)) {
            return EventStatus.AWAITING_REVIEW;
        } else if ("complete".equals(challongeTournamentState)) {
            return EventStatus.COMPLETED;
        } else {
            throw new IllegalArgumentException("Cannot understand challong tournament state: " +
                challongeTournamentState);
        }
    }
}
