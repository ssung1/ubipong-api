package com.eatsleeppong.ubipong.tournamentmanager.controller;

import com.eatsleeppong.ubipong.ratingmanager.dto.MatchResultDto;
import com.eatsleeppong.ubipong.tournamentmanager.domain.MatchResult;

import org.springframework.stereotype.Component;

public class MatchResultMapper {
    public MatchResultDto mapMatchResultToMatchResultDto(final MatchResult matchResult) {
        return MatchResultDto.builder()
            .eventName(matchResult.getEventName())
            .winner(matchResult.getWinner())
            .loser(matchResult.getLoser())
            .resultString(matchResult.getResult())
            .build();
    }
}
