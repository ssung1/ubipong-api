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
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // replace with @Jacksonized after VSCode is fixed
@AllArgsConstructor // remove after VSCode is fixed
public class MatchSheet {
    private String eventName;

    private String player1Name;
    private String player2Name;

    /**
     * Seeding of the player within the event
     */
    private String player1Seed;
    private String player2Seed;
}
