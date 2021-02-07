package com.eatsleeppong.ubipong.tournamentmanager.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class MatchSheetDto {
    String eventName;
    Integer matchId;

    Integer player1UsattNumber;
    Integer player2UsattNumber;

    String player1Name;
    String player2Name;

    /**
     * Seeding of the player within the event, starting with 1
     */
    Integer player1SeedAsNumber;
    /**
     * Seeding of the player within the event, starting with 1
     */
    Integer player2SeedAsNumber;

    /**
     * Seeding of the player within the event, starting with A
     */
    String player1SeedAsAlphabet;
    /**
     * Seeding of the player within the event, starting with A
     */
    String player2SeedAsAlphabet;
}
