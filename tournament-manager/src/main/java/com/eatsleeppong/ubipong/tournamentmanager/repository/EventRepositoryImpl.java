package com.eatsleeppong.ubipong.tournamentmanager.repository;

import com.eatsleeppong.ubipong.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.model.challonge.ChallongeTournament;
import com.eatsleeppong.ubipong.model.challonge.ChallongeTournamentWrapper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.eatsleeppong.ubipong.tournamentmanager.domain.EventRepository;
import org.springframework.stereotype.Service;

@Component
@Value
@AllArgsConstructor
public class EventRepositoryImpl implements EventRepository {
    SpringJpaEventRepository springJpaEventRepository;
    ChallongeTournamentRepository challongeTournamentRepository;

    private SpringJpaEvent mapToSpringJpaEvent(Event event) {
        SpringJpaEvent springJpaEvent = new SpringJpaEvent();
        springJpaEvent.setName(event.getName());
        springJpaEvent.setTournamentId(event.getTournamentId());
        springJpaEvent.setId(event.getId());
        springJpaEvent.setChallongeUrl(event.getChallongeUrl());

        return springJpaEvent;
    }

    private Event mapToEvent(SpringJpaEvent springJpaEvent) {
        return Event.builder()
            .challongeUrl(springJpaEvent.getChallongeUrl())
            .id(springJpaEvent.getId())
            .tournamentId(springJpaEvent.getTournamentId())
            .name(springJpaEvent.getName())
            .build();
    }

    @Override
    public Event addEvent(Event event) {
        SpringJpaEvent springJpaEvent = mapToSpringJpaEvent(event);
        springJpaEvent.setId(null);

        SpringJpaEvent savedSpringJpaEvent = springJpaEventRepository.save(springJpaEvent);

        ChallongeTournament challongeTournament = new ChallongeTournament();
        challongeTournament.setName(event.getName());
        challongeTournament.setUrl(event.getChallongeUrl());
        challongeTournament.setDescription(event.getName());
        challongeTournament.setTournamentType(SpringJpaEvent.EVENT_TYPE_ROUND_ROBIN);
        challongeTournament.setGameName("table tennis");

        ChallongeTournamentWrapper challongeTournamentWrapper = new ChallongeTournamentWrapper();
        challongeTournamentWrapper.setTournament(challongeTournament);
        challongeTournamentRepository.createTournament(challongeTournamentWrapper);

        return mapToEvent(savedSpringJpaEvent);
    }
}
