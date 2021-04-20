package com.eatsleeppong.ubipong.tournamentmanager.controller.mapper;

import java.util.stream.Collectors;

import com.eatsleeppong.ubipong.tournamentmanager.domain.RoundRobinCell;
import com.eatsleeppong.ubipong.tournamentmanager.dto.RoundRobinCellDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.RoundRobinCellTypeDto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component("controllerRoundRobinCellMapper")
@AllArgsConstructor
public class RoundRobinCellMapper {
    private final GameMapper gameMapper;

    public RoundRobinCellDto mapRoundRobinCellToRoundRobinCellDto(final RoundRobinCell roundRobinCell) {
        return RoundRobinCellDto.builder()
            .type(RoundRobinCellTypeDto.valueOf(roundRobinCell.getType().name()))
            .content(roundRobinCell.getContent())
            .gameList(roundRobinCell.getGameList().stream()
                .map(gameMapper::mapGameToGameDto).collect(Collectors.toUnmodifiableList()))
            .build();
    }
}
