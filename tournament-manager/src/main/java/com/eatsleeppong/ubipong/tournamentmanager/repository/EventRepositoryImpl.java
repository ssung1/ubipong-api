package com.eatsleeppong.ubipong.tournamentmanager.repository;

import com.eatsleeppong.ubipong.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.model.challonge.ChallongeTournament;
import com.eatsleeppong.ubipong.model.challonge.ChallongeTournamentWrapper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import com.eatsleeppong.ubipong.tournamentmanager.domain.EventRepository;

@Component
@Value
@AllArgsConstructor
public class EventRepositoryImpl implements EventRepository {
    SpringJpaEventRepository springJpaEventRepository;
    ChallongeTournamentRepository challongeTournamentRepository;
    EventMapper eventMapper;

    @Override
    public Event addEvent(Event event) {
        SpringJpaEvent springJpaEvent = eventMapper.mapEventToSpringJpaEvent(event);
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

        return eventMapper.mapSpringJpaEventToEvent(savedSpringJpaEvent);
    }
}
