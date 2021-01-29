package com.eatsleeppong.ubipong.tournamentmanager.mapper;

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

    public Match mapChallongeMatchToMatch(ChallongeMatch challongeMatch) {
        return Match.builder()
            .id(challongeMatch.getId())
            .status(mapParticipantMatchStateToMatchStatus(challongeMatch.getState()))
            .player1Id(challongeMatch.getPlayer1Id())
            .player2Id(challongeMatch.getPlayer2Id())
            .winnerId(challongeMatch.getWinnerId())
            .resultCode(mapParticipantMatchStateToMatchResultCode(challongeMatch.getState()))
            .scores(challongeMatch.getScoresCsv())
            .build();
    }

    public Match mapChallongeMatchWrapperToMatch(ChallongeMatchWrapper challongeMatchWrapper) {
        return mapChallongeMatchToMatch(challongeMatchWrapper.getMatch());
    }
}