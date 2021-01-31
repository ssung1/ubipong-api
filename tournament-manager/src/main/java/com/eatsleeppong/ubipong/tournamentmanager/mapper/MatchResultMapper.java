package com.eatsleeppong.ubipong.tournamentmanager.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eatsleeppong.ubipong.ratingmanager.dto.MatchResultDto;
import com.eatsleeppong.ubipong.tournamentmanager.domain.MatchResult;

import org.springframework.stereotype.Component;

@Component
public class MatchResultMapper {
    public MatchResultDto mapMatchResultToMatchResultDto(final MatchResult matchResult) {
        return MatchResultDto.builder()
            .eventName(matchResult.getEventName())
            .winner(matchResult.getWinner())
            .loser(matchResult.getLoser())
            .resultString(matchResult.getResult())
            .build();
    }

    public String mapMatchResultToUsattCsv(final MatchResult matchResult) {
        return List.of(
            "id?",
            matchResult.getWinner(),
            matchResult.getLoser(),
            matchResult.getResult(),
            matchResult.getEventName()
        ).stream().collect(Collectors.joining(","));
    }
}
