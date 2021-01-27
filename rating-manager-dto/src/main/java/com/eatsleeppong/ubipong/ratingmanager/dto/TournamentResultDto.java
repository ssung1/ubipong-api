package com.eatsleeppong.ubipong.ratingmanager.dto;

import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // replace with @Jacksonized after VSCode is fixed
@AllArgsConstructor // remove after VSCode is fixed
public class TournamentResultDto {
    private String tournamentName;
    private Date tournamentDate;

    private MatchResultDto[] tournamentResultList;
}