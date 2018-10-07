package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.model.Game;
import com.eatsleeppong.ubipong.model.RoundRobinCell;
import com.eatsleeppong.ubipong.model.challonge.ChallongeMatch;
import com.eatsleeppong.ubipong.model.challonge.ChallongeMatchWrapper;
import com.eatsleeppong.ubipong.model.challonge.ChallongeParticipant;

import java.util.*;
import java.util.stream.Collectors;

public class EventManager {
    public List<ChallongeMatch> findByPlayer1(List<ChallongeMatch> matchList,
        Integer playerId) {
        return matchList.stream()
            .filter((m) -> m.getPlayer1Id().equals(playerId))
            .collect(Collectors.toList());
    }

    public List<ChallongeMatch> unwrapChallongeMatchWrapperArray(
        ChallongeMatchWrapper[] matchWrapperArray
    ) {
        return Arrays.stream(matchWrapperArray)
            .map(ChallongeMatchWrapper::getMatch)
            .collect(Collectors.toList());
    }

    /**
     * given a list of intergers, form a map so that
     *
     * first integer maps to 0
     * second integer maps to 1
     * ...
     * and so on
     *
     * @param playerList
     * @return
     */
    public Map<Integer, Integer> createPlayerIndexMap(
        List<ChallongeParticipant> playerList) {
        Map<Integer, Integer> result = new HashMap<>();
        for (int index = 0; index < playerList.size(); ++index) {
            result.put(playerList.get(index).getId(), index);
        }
        return result;
    }

    /**
     * win or loss -- if player 1 wins, it is a "win"
     * if player 1 loses, it is a "loss"
     */
    private boolean isWinForPlayer1(Integer player1, Integer player2,
        Integer winner) {
        if (player1.equals(winner)) {
            return true;
        }
        return false;
    }

    private List<Game> createGameList(String scoreCsv) {
        String[] scoreArray = scoreCsv.split(",");
        List<Game> result = new ArrayList<>();

        for (String scoreRaw : scoreArray) {
            String score = scoreRaw.trim();
            Game game = new Game();
            int dash = score.indexOf('-');
            if (dash > 0) {
                String player1Score = score.substring(0, dash);
                game.setPlayer1Score(Integer.parseInt(player1Score));
                String player2Score = score.substring(dash + 1);
                game.setPlayer2Score(Integer.parseInt(player2Score));

                if (game.getPlayer1Score() > game.getPlayer2Score()) {
                    game.setWinForPlayer1(true);
                } else {
                    game.setWinForPlayer1(false);
                }
            }
            else {
                game.setPlayer1Score(Integer.parseInt(score));
            }
            result.add(game);
        }
        return result;
    }

    private RoundRobinCell createRoundRobinCell(ChallongeMatch match) {
        Integer player1 = match.getPlayer1Id();
        Integer player2 = match.getPlayer2Id();
        Integer winner = match.getWinnerId();

        RoundRobinCell cell = new RoundRobinCell();
        if ("complete".equals(match.getState())) {
            StringBuilder content = new StringBuilder();
            cell.setType(RoundRobinCell.TYPE_MATCH_COMPLETE);
            if (isWinForPlayer1(player1, player2, winner)) {
                content.append("W");
            } else {
                content.append("L");
            }

            List<Game> gameList = createGameList(match.getScoresCsv());
            for (Game game : gameList) {
                content.append(" ");
                if (game.isWinForPlayer1()) {
                    content.append(game.getPlayer2Score());
                } else {
                    content.append('-');
                    content.append(game.getPlayer1Score());
                }
            }

            cell.setGameList(gameList);
            cell.setContent(content.toString());
        }

        return cell;
    }

    /**
     * translates 0 to A, 1 to B, ... and so on
     * @param number
     * @return
     */
    private String numberToLetter(int number) {
        return Character.toString((char)('A' + number));
    }

    /**
     * For a given match list and player list, form a grid of
     *
     * form a matrix of
     *
     *            player 1, player 2, player 3 ...
     * player 1      x
     * player 2                x
     * player 3                          x
     * ...
     *
     * @param matchList
     * @param playerList
     */
    public RoundRobinCell[][] createRoundRobinGrid(
        List<ChallongeMatch> matchList,
        List<ChallongeParticipant> playerList) {

        int size = playerList.size();
        RoundRobinCell[][] innerGrid = new RoundRobinCell[size][size];

        // create an empty grid
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                innerGrid[i][j] = new RoundRobinCell();
            }
        }

        Map<Integer, Integer> playerMap = createPlayerIndexMap(playerList);

        for (ChallongeMatch match : matchList) {
            Integer player1 = match.getPlayer1Id();
            Integer player2 = match.getPlayer2Id();
            int row = playerMap.get(player1);
            int col = playerMap.get(player2);

            innerGrid[row][col] = createRoundRobinCell(match);
        }

        List<RoundRobinCell[]> resultAsList = new ArrayList<>();
        RoundRobinCell[] rowHeader = new RoundRobinCell[size + 1];
        rowHeader[0] = new RoundRobinCell();
        rowHeader[0].setType(RoundRobinCell.TYPE_EMPTY);
        for (int j = 1; j < size + 1; ++j) {
            RoundRobinCell cell = new RoundRobinCell();
            cell.setType(RoundRobinCell.TYPE_TEXT);
            cell.setContent(numberToLetter(j - 1));
            rowHeader[j] = cell;
        }
        resultAsList.add(rowHeader);

        for (int i = 0; i < size; ++i) {
            RoundRobinCell[] row = innerGrid[i];
            List<RoundRobinCell> columnList = new ArrayList<>();
            // first column is letter
            RoundRobinCell firstColumn = new RoundRobinCell();
            firstColumn.setType(RoundRobinCell.TYPE_TEXT);
            firstColumn.setContent(numberToLetter(i));
            columnList.add(firstColumn);
            // second column is name
            RoundRobinCell secondColumn = new RoundRobinCell();
            secondColumn.setType(RoundRobinCell.TYPE_TEXT);
            secondColumn.setContent(playerList.get(i).getDisplayName());
            columnList.add(secondColumn);

            // add remaining cells
            columnList.addAll(Arrays.asList(row));
            resultAsList.add(columnList.toArray(new RoundRobinCell[0]));
        }

        return resultAsList.toArray(new RoundRobinCell[0][0]);
    }
}
