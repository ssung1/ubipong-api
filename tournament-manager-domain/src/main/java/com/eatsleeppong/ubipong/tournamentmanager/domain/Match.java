package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.Builder.Default;

@Value
@Builder
@With
public class Match {
    public static final Integer STATUS_INCOMPLETE = 10;
    public static final Integer STATUS_COMPLETE = 11;

    public static final Integer RESULT_CODE_WIN_BY_PLAYING = 10;
    public static final Integer RESULT_CODE_WIN_BY_DEFAULT = 11;
    public static final Integer RESULT_CODE_BOTH_DEFAULT = 12;
    public static final Integer RESULT_CODE_TIE_BY_PLAYING = 13;

    private Integer id;
    // not used for now
    // private Integer eventId;
    private Integer status;
    private Integer player1Id;
    private Integer player2Id;
    /**
     * if null, then there is no winner
     */
    private Integer winnerId;
    /**
     * must have a code if the status is STATUS_COMPLETE
     */
    private Integer resultCode;

    @Default
    private List<Game> gameList = Collections.emptyList();

    public Game getGame(int index) {
        return gameList.get(index);
    }

    public boolean isResultValid() {
        return STATUS_COMPLETE == status;
    }

    public String getScoreSummary() {
        return gameList.stream()
            .map(g -> {
                if (g.isWinForPlayer1()) {
                    return g.getPlayer2Score();
                } else {
                    return -g.getPlayer1Score();
                }
            })
            .map(String::valueOf)
            .collect(Collectors.joining(" "));
    }

    public Match transpose() {
        return Match.builder()
            .id(id)
            .player1Id(player2Id)
            .player2Id(player1Id)
            .resultCode(resultCode)
            .status(status)
            .winnerId(winnerId)
            .gameList(gameList.stream().map(Game::transpose).collect(Collectors.toUnmodifiableList()))
            .build();
    }
}