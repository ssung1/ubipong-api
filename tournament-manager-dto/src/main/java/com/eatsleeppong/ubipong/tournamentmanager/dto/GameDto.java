package com.eatsleeppong.ubipong.tournamentmanager.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // replace with @Jacksonized after VSCode is fixed
@AllArgsConstructor // remove after VSCode is fixed
public class GameDto {
    private int player1Score;
    private int player2Score;
    private boolean winForPlayer1;
}
