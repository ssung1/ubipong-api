package com.eatsleeppong.ubipong.tournamentmanager.domain;

public interface TournamentRepository {
    Tournament save(Tournament tournament);
    Tournament getOne(Integer id);
}
