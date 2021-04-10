package com.eatsleeppong.ubipong.tournamentmanager.controller.mapper;

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
}
