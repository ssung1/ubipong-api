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
    String eventName;
    Integer matchId;

    Integer player1Id;
    Integer player2Id;

    Integer player1UsattNumber;
    Integer player2UsattNumber;

    String player1Name;
    String player2Name;

    /**
     * Seeding of the player within the event, starting with 0
     */
    Integer player1Seed;
    Integer player2Seed;
}
