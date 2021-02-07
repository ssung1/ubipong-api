package com.eatsleeppong.ubipong.tournamentmanager.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * A MatchSheet is used to print information about a match that the players
 * can use to record scores.
 */
@Value
@Builder
public class MatchSheet {
    private String eventName;
    private Integer matchId;

    private Integer player1UsattNumber;
    private Integer player2UsattNumber;

    private String player1Name;
    private String player2Name;

    /**
     * Seeding of the player within the event, starting with 0
     */
    private Integer player1Seed;
    private Integer player2Seed;
}
