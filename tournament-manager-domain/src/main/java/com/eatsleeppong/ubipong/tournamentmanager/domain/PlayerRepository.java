package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.util.List;

public interface PlayerRepository {
    List<Player> findByChallongeUrl(String challongeUrl);
}