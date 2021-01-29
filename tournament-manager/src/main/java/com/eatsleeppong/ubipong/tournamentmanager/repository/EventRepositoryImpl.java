package com.eatsleeppong.ubipong.tournamentmanager.repository;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import com.eatsleeppong.ubipong.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.model.challonge.ChallongeTournament;
import com.eatsleeppong.ubipong.model.challonge.ChallongeTournamentWrapper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import com.eatsleeppong.ubipong.tournamentmanager.mapper.EventMapper;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import com.eatsleeppong.ubipong.tournamentmanager.domain.EventRepository;

@Component
@Value
@AllArgsConstructor
@Slf4j
public class EventRepositoryImpl implements EventRepository {
    SpringJpaEventRepository springJpaEventRepository;
    ChallongeTournamentRepository challongeTournamentRepository;
    EventMapper eventMapper;

    @Override
    public Event save(final Event event) {
        final SpringJpaEvent springJpaEvent = eventMapper.mapEventToSpringJpaEvent(event);
        springJpaEvent.setId(null);

        final SpringJpaEvent savedSpringJpaEvent = springJpaEventRepository.save(springJpaEvent);

        final ChallongeTournament challongeTournament = new ChallongeTournament();
        challongeTournament.setName(event.getName());
        challongeTournament.setUrl(event.getChallongeUrl());
        challongeTournament.setDescription(event.getName());
        challongeTournament.setTournamentType(SpringJpaEvent.EVENT_TYPE_ROUND_ROBIN);
        challongeTournament.setGameName("table tennis");

        final ChallongeTournamentWrapper challongeTournamentWrapper = new ChallongeTournamentWrapper();
        challongeTournamentWrapper.setTournament(challongeTournament);
        challongeTournamentRepository.createTournament(challongeTournamentWrapper);

        return eventMapper.mapSpringJpaEventToEvent(savedSpringJpaEvent);
    }

    @Override
    public Event getOne(final Integer id) {
        return eventMapper.mapSpringJpaEventToEvent(springJpaEventRepository.getOne(id));
    }

    @Override
    public Event getOneByChallongeUrl(final String challongeUrl) {
        List<SpringJpaEvent> eventList = springJpaEventRepository.findByChallongeUrl(challongeUrl);
        if (eventList.size() < 1) {
            throw new EntityNotFoundException();
        }
        else if (eventList.size() > 1) {
            log.warn("More than one event with challongeUrl: " + challongeUrl);
        }

        return eventMapper.mapSpringJpaEventToEvent(eventList.get(0));
    }
}
