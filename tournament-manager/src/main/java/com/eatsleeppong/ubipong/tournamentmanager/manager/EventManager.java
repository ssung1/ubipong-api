package com.eatsleeppong.ubipong.tournamentmanager.manager;

import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import com.eatsleeppong.ubipong.tournamentmanager.domain.EventRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Player;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Match;
import com.eatsleeppong.ubipong.tournamentmanager.mapper.EventMapper;
import com.eatsleeppong.ubipong.tournamentmanager.dto.response.Game;
import com.eatsleeppong.ubipong.tournamentmanager.dto.response.RoundRobinCell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventManager {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

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

    private RoundRobinCell createRoundRobinCell(Match match) {
        Integer player1 = match.getPlayer1Id();
        Integer player2 = match.getPlayer2Id();
        Integer winner = match.getWinnerId();

        RoundRobinCell cell = new RoundRobinCell();
        if (match.isResultValid()) {
            cell.setType(RoundRobinCell.TYPE_MATCH_COMPLETE);
            // cell.setWinForPlayer1(isWinForPlayer1(player1, player2, winner));
            cell.setWinForPlayer1(match.isWinForPlayer1());

            cell.setGameList(match.getGameList().stream().map(g -> {
                final Game game = new Game();
                game.setPlayer1Score(g.getPlayer1Score());
                game.setPlayer2Score(g.getPlayer2Score());
                if (game.getPlayer1Score() > game.getPlayer2Score()) {
                    game.setWinForPlayer1(true);
                } else {
                    game.setWinForPlayer1(false);
                }
                return game;
            }).collect(Collectors.toUnmodifiableList()));

            updateCellContent(cell);
        }

        return cell;
    }

    private String convertToGameSummary(List<Game> gameList) {
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

    private void updateCellContent(RoundRobinCell cell) {
        StringBuilder content = new StringBuilder();
        if (cell.isWinForPlayer1()) {
            content.append("W");
        } else {
            content.append("L");
        }

        content.append(" ");
        content.append(convertToGameSummary(cell.getGameList()));

        cell.setContent(content.toString());
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
     *               A         B         C
     * A player      x        W 8 8
     * B player     L -8 -8    x
     * C player                          x
     * ...
     *
     * @param matchList
     * @param playerList
     */
    public RoundRobinCell[][] createRoundRobinGrid(final String challongeUrl) {
        Event event = eventRepository.getOneByChallongeUrl(challongeUrl);
        List<Match> matchList = event.getMatchList();
        List<Player> playerList = event.getPlayerList();

        int size = playerList.size();
        RoundRobinCell[][] innerGrid = new RoundRobinCell[size][size];

        // create an empty grid
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                innerGrid[i][j] = new RoundRobinCell();
            }
        }

        final Map<Integer, Player> playerMap = event.getPlayerMap();

        for(Match match : matchList) {
            Integer player1 = match.getPlayer1Id();
            Integer player2 = match.getPlayer2Id();
            int row = playerMap.get(player1).getEventSeed();
            int col = playerMap.get(player2).getEventSeed();

            RoundRobinCell cell = createRoundRobinCell(match);
            innerGrid[row][col] = cell;
            innerGrid[col][row] = createRoundRobinCell(match.transpose());
        }

        List<RoundRobinCell[]> resultAsList = new ArrayList<>();
        RoundRobinCell[] rowHeader = new RoundRobinCell[size + 2];
        rowHeader[0] = new RoundRobinCell();
        rowHeader[0].setType(RoundRobinCell.TYPE_EMPTY);
        rowHeader[1] = new RoundRobinCell();
        rowHeader[1].setType(RoundRobinCell.TYPE_EMPTY);
        for (int j = 0; j < size; ++j) {
            RoundRobinCell cell = new RoundRobinCell();
            cell.setType(RoundRobinCell.TYPE_TEXT);
            cell.setContent(numberToLetter(j));
            rowHeader[j + 2] = cell;
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
            secondColumn.setContent(playerList.get(i).getName());
            columnList.add(secondColumn);

            // add remaining cells
            columnList.addAll(Arrays.asList(row));
            resultAsList.add(columnList.toArray(new RoundRobinCell[0]));
        }

        return resultAsList.toArray(new RoundRobinCell[0][0]);
    }
}
