package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.time.Instant;
import java.util.List;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class TournamentResult {
    private String tournamentName;
    private Instant tournamentDate;

    private List<MatchResult> matchResultList;
}
