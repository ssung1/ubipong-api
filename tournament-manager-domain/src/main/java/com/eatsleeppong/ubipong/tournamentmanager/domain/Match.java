package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Value;
import lombok.With;

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

    private List<Game> gameList;

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
}