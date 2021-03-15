package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.util.List;

public interface TournamentRepository {
    Tournament save(Tournament tournament);
    Tournament getOne(Integer id);
    List<Tournament> findAll();
}
