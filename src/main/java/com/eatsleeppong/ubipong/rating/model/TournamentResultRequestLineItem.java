package com.eatsleeppong.ubipong.rating.model;

import lombok.Data;

/**
 * This is a match result, but we are calling it TournamentResultRequestLineItem to avoid confusion with the entity
 * actually called MatchResult
 */
@Data
public class TournamentResultRequestLineItem {
    private String winner;
    private String loser;

    private String resultString;
}
