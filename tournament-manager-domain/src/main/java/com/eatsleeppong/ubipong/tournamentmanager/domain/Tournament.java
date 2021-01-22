package com.eatsleeppong.ubipong.tournamentmanager.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Tournament {
//    private final EventManager eventManager;
//    private final EventRepository eventRepository;
//    private final TournamentRepository tournamentRepository;
//
//    public Tournament(final EventManager eventManager, final EventRepository eventRepository,
//        final TournamentRepository tournamentRepository) {
//        this.eventManager = eventManager;
//        this.eventRepository = eventRepository;
//        this.tournamentRepository = tournamentRepository;
//    }
//
//    public TournamentResultRequest createTournamentResultRequest(final Integer tournamentId) {
//        final List<Event> eventList = eventRepository.findByTournamentId(tournamentId);
//
//        final TournamentResultRequestLineItem[] tournamentResultRequestLineItemList = eventList.stream()
//            .map(Event::getChallongeUrl)
//            .map(eventManager::createTournamentResultList)
//            .flatMap(Arrays::stream)
//            .toArray(TournamentResultRequestLineItem[]::new);
//
//        final TournamentResultRequest tournamentResultRequest = new TournamentResultRequest();
//
//        final Tournament tournament = tournamentRepository.getOne(tournamentId);
//        tournamentResultRequest.setTournamentName(tournament.getName());
//        tournamentResultRequest.setTournamentDate(tournament.getTournamentDate());
//        tournamentResultRequest.setTournamentResultList(tournamentResultRequestLineItemList);
//
//        return tournamentResultRequest;
//    }
}
