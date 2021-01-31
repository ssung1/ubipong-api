package com.eatsleeppong.ubipong.tournamentmanager.mapper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Collectors;

import com.eatsleeppong.ubipong.ratingmanager.dto.TournamentResultDto;
import com.eatsleeppong.ubipong.tournamentmanager.domain.TournamentResult;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TournamentResultMapper {

    private final MatchResultMapper matchResultMapper;

    public TournamentResultDto mapTournamentResultToTournamentResultDto(final TournamentResult tournamentResult) {
        return TournamentResultDto.builder()
            .tournamentName(tournamentResult.getTournamentName())
            .tournamentDate(tournamentResult.getTournamentDate())
            .tournamentResultList(tournamentResult.getMatchResultList().stream()
                .map(matchResultMapper::mapMatchResultToMatchResultDto)
                .collect(Collectors.toUnmodifiableList())
            )
            .build();
    }

    public String mapTournamentResultToUsattCsv(final TournamentResult tournamentResult) {
        return tournamentResult.getMatchResultList().stream()
            .map(matchResultMapper::mapMatchResultToUsattCsv)
            .collect(Collectors.joining("\r\n"));
    }
}
