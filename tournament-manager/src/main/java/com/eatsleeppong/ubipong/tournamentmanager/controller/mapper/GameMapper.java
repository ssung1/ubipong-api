package com.eatsleeppong.ubipong.tournamentmanager.controller.mapper;

import com.eatsleeppong.ubipong.tournamentmanager.dto.GameDto;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Game;

import org.springframework.stereotype.Component;

@Component
public class GameMapper {
    public GameDto mapGameToGameDto(final Game game) {
        return GameDto.builder()
            .player1Score(game.getPlayer1Score())
            .player2Score(game.getPlayer2Score())
            .winForPlayer1(game.isWinForPlayer1())
            .build();
    }
}
