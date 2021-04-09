package com.eatsleeppong.ubipong.tournamentmanager.controller.mapper;

import com.eatsleeppong.ubipong.tournamentmanager.domain.MatchSheet;
import com.eatsleeppong.ubipong.tournamentmanager.dto.MatchSheetDto;
import org.springframework.stereotype.Component;

@Component
public class MatchSheetMapper {
    private String mapNumberToAlphabet(final Integer number) {
        if (number > 26) {
            throw new IllegalArgumentException("Cannot convert number to single alphabet: " + number);
        }
        return Character.toString((char)('A' + number));
    }

    public MatchSheetDto mapMatchSheetToMatchSheetDto(final MatchSheet matchSheet) {
        return MatchSheetDto.builder()
            .eventName(matchSheet.getEventName())
            .matchId(matchSheet.getMatchId())
            .player1Id(matchSheet.getPlayer1Id())
            .player1UsattNumber(matchSheet.getPlayer1UsattNumber())
            .player1Name(matchSheet.getPlayer1Name())
            .player1SeedAsNumber(matchSheet.getPlayer1Seed() + 1)
            .player1SeedAsAlphabet(mapNumberToAlphabet(matchSheet.getPlayer1Seed()))
            .player2Id(matchSheet.getPlayer2Id())
            .player2UsattNumber(matchSheet.getPlayer2UsattNumber())
            .player2Name(matchSheet.getPlayer2Name())
            .player2SeedAsNumber(matchSheet.getPlayer2Seed() + 1)
            .player2SeedAsAlphabet(mapNumberToAlphabet(matchSheet.getPlayer2Seed()))
            .build();
    }
}
