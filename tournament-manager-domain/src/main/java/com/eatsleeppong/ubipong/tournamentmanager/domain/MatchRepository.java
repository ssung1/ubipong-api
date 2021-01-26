package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.util.List;

public interface MatchRepository {
    List<Match> findByChallongeUrl(String challongeUrl);
}