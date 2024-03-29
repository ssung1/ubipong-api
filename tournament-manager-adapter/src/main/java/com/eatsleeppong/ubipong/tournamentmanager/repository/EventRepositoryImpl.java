package com.eatsleeppong.ubipong.tournamentmanager.repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.model.challonge.ChallongeTournament;
import com.eatsleeppong.ubipong.model.challonge.ChallongeTournamentWrapper;
import com.eatsleeppong.ubipong.tournamentmanager.repository.mapper.EventMapper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;

import com.eatsleeppong.ubipong.tournamentmanager.domain.EventRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.EventStatus;

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

        final SpringJpaEvent savedSpringJpaEvent = springJpaEventRepository.save(springJpaEvent);

        final ChallongeTournament challongeTournament = new ChallongeTournament();
        challongeTournament.setName(event.getName());
        challongeTournament.setUrl(event.getChallongeUrl());
        challongeTournament.setDescription(event.getName());
        challongeTournament.setTournamentType(SpringJpaEvent.EVENT_TYPE_ROUND_ROBIN);
        challongeTournament.setGameName("table tennis");

        final ChallongeTournamentWrapper challongeTournamentWrapper = new ChallongeTournamentWrapper();
        challongeTournamentWrapper.setTournament(challongeTournament);

        try {
            challongeTournamentRepository.getTournament(event.getChallongeUrl());
            challongeTournamentRepository.updateTournament(challongeTournamentWrapper);
        } catch (final HttpClientErrorException getTournamentException) {
            if (getTournamentException.getStatusCode() == HttpStatus.NOT_FOUND) {
                createChallongeTournament(challongeTournamentWrapper);
            } else {
                throw getTournamentException;
            }
        }

        return eventMapper.mapSpringJpaEventToEvent(savedSpringJpaEvent);
    }
    
    private void createChallongeTournament(final ChallongeTournamentWrapper challongeTournamentWrapper) {
        try {
            challongeTournamentRepository.createTournament(challongeTournamentWrapper);
        } catch (final HttpClientErrorException createTournamentException) {
            if (HttpStatus.UNPROCESSABLE_ENTITY == createTournamentException.getStatusCode()){
                log.info("Challonge tournament {} already exists",
                    challongeTournamentWrapper.getTournament().getUrl());
            } else {
                throw createTournamentException;
            }
        }
    }

    private Event mapSpringJpaEventToEvent(final SpringJpaEvent springJpaEvent) {
        final ChallongeTournamentWrapper challongeTournamentWrapper =
            challongeTournamentRepository.getTournament(springJpaEvent.getChallongeUrl());

        final EventStatus eventStatus = eventMapper.mapChallongeTournamentStateToEventStatus(
            challongeTournamentWrapper.getTournament().getState());

        return eventMapper
            .mapSpringJpaEventToEvent(springJpaEvent)
            .withStatus(eventStatus);
    }

    @Override
    public Event getOne(final Integer id) {
        final SpringJpaEvent springJpaEvent = springJpaEventRepository.getOne(id);

        return mapSpringJpaEventToEvent(springJpaEvent);
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

    @Override
    public List<Event> findByTournamentId(final Integer tournamentId) {
        return springJpaEventRepository.findByTournamentId(tournamentId).stream()
            .map(this::mapSpringJpaEventToEvent)
            .collect(Collectors.toUnmodifiableList());
    }
}
