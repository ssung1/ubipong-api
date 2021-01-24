package com.eatsleeppong.ubipong.tournamentmanager.repository;

import com.eatsleeppong.ubipong.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import com.eatsleeppong.ubipong.tournamentmanager.dto.EventDto;

import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public SpringJpaEvent mapEventToSpringJpaEvent(Event event) {
        SpringJpaEvent springJpaEvent = new SpringJpaEvent();
        springJpaEvent.setName(event.getName());
        springJpaEvent.setTournamentId(event.getTournamentId());
        springJpaEvent.setId(event.getId());
        springJpaEvent.setChallongeUrl(event.getChallongeUrl());

        return springJpaEvent;
    }

    public Event mapSpringJpaEventToEvent(SpringJpaEvent springJpaEvent) {
        return Event.builder()
            .challongeUrl(springJpaEvent.getChallongeUrl())
            .id(springJpaEvent.getId())
            .tournamentId(springJpaEvent.getTournamentId())
            .name(springJpaEvent.getName())
            .build();
    }

    public Event mapEventDtoToEvent(EventDto eventDto) {
        return Event.builder()
            .id(eventDto.getId())
            .tournamentId(eventDto.getTournamentId())
            .name(eventDto.getName())
            .challongeUrl(eventDto.getChallongeUrl())
            .build();
    }
}
