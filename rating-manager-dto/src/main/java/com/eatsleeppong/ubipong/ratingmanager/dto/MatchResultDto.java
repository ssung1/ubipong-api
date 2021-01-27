package com.eatsleeppong.ubipong.ratingmanager.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * This is replacement for TournamentResultRequestLineItem
 */
@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // replace with @Jacksonized after VSCode is fixed
@AllArgsConstructor // remove after VSCode is fixed
public class MatchResultDto {
    private String winner;
    private String loser;

    private String eventName;
    private String resultString;
}