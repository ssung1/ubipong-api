package com.eatsleeppong.ubipong.tournamentmanager.mapper;

import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import com.eatsleeppong.ubipong.tournamentmanager.domain.EventStatus;
import com.eatsleeppong.ubipong.tournamentmanager.domain.MatchRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.PlayerRepository;
import com.eatsleeppong.ubipong.tournamentmanager.dto.EventDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.EventStatusDto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
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
            .build();
    }

    public Event mapEventDtoToEvent(final EventDto eventDto) {
        return Event.builder()
            .id(eventDto.getId())
            .tournamentId(eventDto.getTournamentId())
            .name(eventDto.getName())
            .challongeUrl(eventDto.getChallongeUrl())
            .playerRepository(playerRepository)
            .matchRepository(matchRepository)
            .status(EventStatus.valueOf(eventDto.getStatus().name()))
            .build();
    }

    public EventDto mapEventToEventDto(final Event event) {
        return EventDto.builder()
            .id(event.getId())
            .tournamentId(event.getTournamentId())
            .name(event.getName())
            .challongeUrl(event.getChallongeUrl())
            .status(EventStatusDto.valueOf(event.getStatus().name()))
            .build();
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
