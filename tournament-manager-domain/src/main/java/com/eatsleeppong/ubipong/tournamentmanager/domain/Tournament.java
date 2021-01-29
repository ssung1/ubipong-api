package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Tournament {
    private final EventRepository eventRepository;

    private final Integer id;
    private final String name;
    private final Instant tournamentDate;

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

    public TournamentResult getResult() {
        return TournamentResult.builder().build();
    }

    public List<Event> getEventList() {
        return eventRepository.findByTournamentId(id);
    }
}
