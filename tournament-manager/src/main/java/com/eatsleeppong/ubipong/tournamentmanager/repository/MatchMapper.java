package com.eatsleeppong.ubipong.tournamentmanager.repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.eatsleeppong.ubipong.model.challonge.ChallongeMatch;
import com.eatsleeppong.ubipong.model.challonge.ChallongeMatchWrapper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Game;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Match;

import org.springframework.stereotype.Component;

@Component
public class MatchMapper {
    private int mapParticipantMatchStateToMatchStatus(final String matchState) {
        if (ChallongeMatch.STATE_COMPLETE.equalsIgnoreCase(matchState)) {
            return Match.STATUS_COMPLETE;
        } else {
            return Match.STATUS_INCOMPLETE;
        }
    }

    private Integer mapParticipantMatchStateToMatchResultCode(final String matchState) {
        if (ChallongeMatch.STATE_COMPLETE.equalsIgnoreCase(matchState)) {
            return Match.RESULT_CODE_WIN_BY_PLAYING;
        } else {
            return null;
        }
    }

    /**
     * score is in the form of {player1Score}-{player2Score}
     * 
     * in case where there is no dash found, the entire score is considered player1Score
     * 
     * @param score
     * @return
     */
    private Game mapScoreToGame(final String score) {
        final Game.GameBuilder gameBuilder = Game.builder();

        int dash = score.indexOf('-');
        if (dash > 0) {
            int player1Score = Integer.parseInt(score.substring(0, dash));
            int player2Score = Integer.parseInt(score.substring(dash + 1));

            gameBuilder
                .player1Score(player1Score)
                .player2Score(player2Score)
                .status(Game.STATUS_COMPLETE)
                .winForPlayer1(player1Score > player2Score);
        }
        else {
            gameBuilder.player1Score(Integer.parseInt(score));
        }

        return gameBuilder.build();
    }

    private List<Game> mapScoreCsvToGameList(final String scoreCsv) {
        if (scoreCsv == null) {
            return Collections.emptyList();
        }
        String[] scoreArray = scoreCsv.split(",");
        return Arrays.stream(scoreArray)
            .map(String::trim)
            .map(this::mapScoreToGame)
            .collect(Collectors.toUnmodifiableList());
    }

    public Match mapChallongeMatchToMatch(ChallongeMatch challongeMatch) {
        return Match.builder()
            .id(challongeMatch.getId())
            .status(mapParticipantMatchStateToMatchStatus(challongeMatch.getState()))
            .player1Id(challongeMatch.getPlayer1Id())
            .player2Id(challongeMatch.getPlayer2Id())
            .winnerId(challongeMatch.getWinnerId())
            .resultCode(mapParticipantMatchStateToMatchResultCode(challongeMatch.getState()))
            .gameList(mapScoreCsvToGameList(challongeMatch.getScoresCsv()))
            .build();
    }

    public Match mapChallongeMatchWrapperToMatch(ChallongeMatchWrapper challongeMatchWrapper) {
        return mapChallongeMatchToMatch(challongeMatchWrapper.getMatch());
    }
}