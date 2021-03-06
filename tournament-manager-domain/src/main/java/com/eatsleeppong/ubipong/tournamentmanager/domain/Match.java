package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class Match {

    public static final Integer RESULT_CODE_WIN_BY_PLAYING = 10;
    public static final Integer RESULT_CODE_WIN_BY_DEFAULT = 11;
    public static final Integer RESULT_CODE_BOTH_DEFAULT = 12;
    public static final Integer RESULT_CODE_TIE_BY_PLAYING = 13;

    private Integer id;
    // not used for now
    // private Integer eventId;

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

    public static class MatchBuilder {
        private List<Game> gameList = Collections.emptyList();

        public MatchBuilder scores(final String scoreCsv) {
            if (scoreCsv == null || scoreCsv.isBlank()) {
                return this;
            }
            final String[] scoreArray = scoreCsv.split(",");
            gameList = Arrays.stream(scoreArray)
                .map(scores -> Game.builder().scores(scores).build())
                .collect(Collectors.toUnmodifiableList());
            return this;
        }
    }

    public Game getGame(int index) {
        return gameList.get(index);
    }

    public boolean isResultValid() {
        return winnerId != null || gameList.size() > 0;
    }

    public boolean arePlayersValid() {
        return player1Id != null && player2Id != null;
    }

    public List<Integer> getScoreSummary() {
        return gameList.stream()
            .map(Game::getSimplifiedScore)
            .collect(Collectors.toUnmodifiableList());
    }

    public Match transpose() {
        return Match.builder()
            .id(id)
            .player1Id(player2Id)
            .player2Id(player1Id)
            .resultCode(resultCode)
            .winnerId(winnerId)
            .gameList(gameList.stream().map(Game::transpose).collect(Collectors.toUnmodifiableList()))
            .build();
    }

    public long getGamesWonByPlayer1() {
        return getGameList().stream().filter(Game::isWinForPlayer1).count();
    }

    public long getGamesWonByPlayer2() {
        return getGameList().stream().filter(game -> !game.isWinForPlayer1()).count();
    }

    public boolean isWinForPlayer1() {
        if (!isResultValid()) {
            throw new IllegalStateException("Cannot determine winner");
        } else if (winnerId != null) {
            // winner is set by *someone*
            if (winnerId.equals(player1Id)) {
                return true;
            } else if (winnerId.equals(player2Id)) {
                return false;
            } else {
                throw new IllegalStateException("Winner ID does not match any player");
            }
        } else {
            // determine winner by game scores
            final long player1Games = getGamesWonByPlayer1();
            final long player2Games = getGamesWonByPlayer2();

            if (player1Games == player2Games) {
                throw new IllegalStateException("Game scores are tied");
            }
            return player1Games > player2Games;
        }
    }

    /**
     * Transpose the match if it is a win for player2
     * Otherwise, return original match
     * @return
     */
    public Match transposeIfWinForPlayer2() {
        if (!isWinForPlayer1()) {
            return transpose();
        } else {
            return this;
        }
    }

    public RoundRobinCellWinLossIndicator getRoundRobinWinLossIndicator() {
        if (isWinForPlayer1()) {
            return RoundRobinCellWinLossIndicator.WIN;
        } else {
            return RoundRobinCellWinLossIndicator.LOSS;
        }
    }

    public RoundRobinCell toRoundRobinCell() {
        if (isResultValid()) {
            return RoundRobinCell.builder()
                .type(RoundRobinCellType.MATCH_COMPLETE)
                .content(getRoundRobinWinLossIndicator().getValue() + " " +
                    getScoreSummary().stream().map(String::valueOf).collect(Collectors.joining(" ")))
                .winForPlayer1(isWinForPlayer1())
                .winByDefault(RESULT_CODE_WIN_BY_DEFAULT.equals(getResultCode()))
                .gameList(getGameList())
                .build();
        } else {
            return RoundRobinCell.builder()
                .type(RoundRobinCellType.MATCH_INCOMPLETE)
                .winForPlayer1(false)
                .winByDefault(RESULT_CODE_WIN_BY_DEFAULT.equals(getResultCode()))
                .gameList(getGameList())
                .build();
        }
   }
}