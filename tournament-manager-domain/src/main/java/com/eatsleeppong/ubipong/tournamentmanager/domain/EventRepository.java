package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.util.List;

public interface EventRepository {
    Event save(Event event);
    Event getOne(Integer id);
    Event getOneByChallongeUrl(String challongeUrl);
    List<Event> findByTournamentId(Integer tournamentId);
}
