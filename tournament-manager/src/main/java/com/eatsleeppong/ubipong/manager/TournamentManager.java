package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.entity.Event;
import com.eatsleeppong.ubipong.entity.Tournament;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequest;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequestLineItem;
import com.eatsleeppong.ubipong.repo.SpringJpaEventRepository;
import com.eatsleeppong.ubipong.repo.SpringJpaTournamentRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Manages tournaments.  Should not be called by EventManager
 */
@Service
public class TournamentManager {
    private final EventManager eventManager;
    private final SpringJpaEventRepository eventRepository;
    private final SpringJpaTournamentRepository tournamentRepository;

    public TournamentManager(final EventManager eventManager, final SpringJpaEventRepository eventRepository,
            final SpringJpaTournamentRepository tournamentRepository) {
        this.eventManager = eventManager;
        this.eventRepository = eventRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public TournamentResultRequest createTournamentResultRequest(final Integer id) {
        final List<Event> eventList = eventRepository.findByTournamentId(id);

        final TournamentResultRequestLineItem[] tournamentResultRequestLineItemList = eventList.stream()
                .map(Event::getChallongeUrl)
                .map(eventManager::createTournamentResultList)
                .flatMap(Arrays::stream)
                .toArray(TournamentResultRequestLineItem[]::new);

        final TournamentResultRequest tournamentResultRequest = new TournamentResultRequest();

        final Tournament tournament = tournamentRepository.getOne(id);
        tournamentResultRequest.setTournamentName(tournament.getName());
        tournamentResultRequest.setTournamentDate(tournament.getTournamentDate());
        tournamentResultRequest.setTournamentResultList(tournamentResultRequestLineItemList);

        return tournamentResultRequest;
    }
}
