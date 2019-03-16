package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.entity.Event;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequest;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequestLineItem;
import com.eatsleeppong.ubipong.repo.EventRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Manages tournaments.  Should not be called by EventManager
 */
@Service
public class TournamentManager {
    private final EventManager eventManager;
    private final EventRepository eventRepository;

    public TournamentManager(final EventManager eventManager, final EventRepository eventRepository) {
        this.eventManager = eventManager;
        this.eventRepository = eventRepository;
    }

    public TournamentResultRequest createTournamentResultRequest(final Integer tournamentId) {
        final List<Event> eventList = eventRepository.findByTournamentId(tournamentId);
//        final TournamentResultRequestLineItem[] tournamentResultRequestLineItemList = eventList.stream()
//                .map(Event::getName)
//                .map(eventManager::createTournamentResultList)
//                .flatMap(Arrays::stream)
//                .toArray(TournamentResultRequestLineItem[]::new);

        final TournamentResultRequestLineItem[] listOfList = eventList.stream()
                .map(Event::getName)
                .map(eventManager::createTournamentResultList)
                .flatMap(Arrays::stream)
                .toArray(TournamentResultRequestLineItem[]::new);

        TournamentResultRequest tournamentResultRequest = new TournamentResultRequest();
        //tournamentResultRequest.setTournamentResultList(tournamentResultRequestLineItemList);

        return tournamentResultRequest;
    }
}
