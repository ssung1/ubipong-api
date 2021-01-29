package com.eatsleeppong.ubipong.tournamentmanager.mapper;

import com.eatsleeppong.ubipong.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import com.eatsleeppong.ubipong.tournamentmanager.domain.MatchRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.PlayerRepository;
import com.eatsleeppong.ubipong.tournamentmanager.dto.EventDto;

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
            .build();
    }

    public EventDto mapEventToEventDto(final Event event) {
        return EventDto.builder()
            .id(event.getId())
            .tournamentId(event.getTournamentId())
            .name(event.getName())
            .challongeUrl(event.getChallongeUrl())
            .build();
    }
}
