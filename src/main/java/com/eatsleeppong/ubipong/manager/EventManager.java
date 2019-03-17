package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.entity.Event;
import com.eatsleeppong.ubipong.model.Game;
import com.eatsleeppong.ubipong.model.RoundRobinCell;
import com.eatsleeppong.ubipong.model.challonge.*;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequestLineItem;
import com.eatsleeppong.ubipong.repo.ChallongeMatchRepository;
import com.eatsleeppong.ubipong.repo.ChallongeParticipantRepository;
import com.eatsleeppong.ubipong.repo.ChallongeTournamentRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventManager {
    private ChallongeTournamentRepository tournamentRepository;
    private ChallongeParticipantRepository participantRepository;
    private ChallongeMatchRepository matchRepository;

    public EventManager(
        ChallongeTournamentRepository tournamentRepository,
        ChallongeParticipantRepository participantRepository,
        ChallongeMatchRepository matchRepository
    ) {
        this.tournamentRepository = tournamentRepository;
        this.participantRepository = participantRepository;
        this.matchRepository = matchRepository;
    }

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

    public List<ChallongeParticipant> unwrapChallongeParticipantWrapperArray(
        ChallongeParticipantWrapper[] participantWrapperArray
    ) {
        return Arrays.stream(participantWrapperArray)
            .map(ChallongeParticipantWrapper::getParticipant)
            .collect(Collectors.toList());
    }

    /**
     * given a list of integers, form a map so that
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
     * Create a map of player ID to player name
     */
    public Map<Integer, String> createPlayerNameMap(List<ChallongeParticipant> playerList) {
        return new HashMap<Integer, String>() { {
            playerList.forEach(p -> {
                put(p.getId(), p.getDisplayName());
            });
        } };
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

    private boolean isMatchResultValid(ChallongeMatch match) {
        return "complete".equals(match.getState());
    }

    private RoundRobinCell createRoundRobinCell(ChallongeMatch match) {
        Integer player1 = match.getPlayer1Id();
        Integer player2 = match.getPlayer2Id();
        Integer winner = match.getWinnerId();

        RoundRobinCell cell = new RoundRobinCell();
        if (isMatchResultValid(match)) {
            cell.setType(RoundRobinCell.TYPE_MATCH_COMPLETE);
            cell.setWinForPlayer1(isWinForPlayer1(player1, player2, winner));

            List<Game> gameList = createGameList(match.getScoresCsv());
            cell.setGameList(gameList);

            updateCellContent(cell);
        }

        return cell;
    }

    /**
     * Given a cell of W 1 2 3, produce the inverse L -1 -2 -3
     *
     * @param cell
     * @return
     */
    public RoundRobinCell createInverseCell(RoundRobinCell cell) {
        // inverse is only possible for completed matches
        if (cell.getType() != RoundRobinCell.TYPE_MATCH_COMPLETE) {
            return cell;
        }

        RoundRobinCell result = new RoundRobinCell();

        result.setWinForPlayer1(!cell.isWinForPlayer1());
        result.setWinByDefault(cell.isWinByDefault());

        result.setGameList(cell.getGameList().stream()
            .map(g -> {
                Game inverseGame = new Game();
                inverseGame.setWinForPlayer1(!g.isWinForPlayer1());
                inverseGame.setPlayer1Score(g.getPlayer2Score());
                inverseGame.setPlayer2Score(g.getPlayer1Score());

                return inverseGame;
            })
            .collect(Collectors.toList()));

        updateCellContent(result);
        return result;
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
    private RoundRobinCell[][] createRoundRobinGrid(
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

            RoundRobinCell cell = createRoundRobinCell(match);
            innerGrid[row][col] = cell;
            innerGrid[col][row] = createInverseCell(cell);
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
            secondColumn.setContent(playerList.get(i).getDisplayName());
            columnList.add(secondColumn);

            // add remaining cells
            columnList.addAll(Arrays.asList(row));
            resultAsList.add(columnList.toArray(new RoundRobinCell[0]));
        }

        return resultAsList.toArray(new RoundRobinCell[0][0]);
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
     * @param eventName
     */
    public RoundRobinCell[][] createRoundRobinGrid(String eventName) {
        List<ChallongeParticipant> participantList =
            unwrapChallongeParticipantWrapperArray(
                participantRepository.getParticipantList(eventName));
        List<ChallongeMatch> matchList =
            unwrapChallongeMatchWrapperArray(
                matchRepository.getMatchList(eventName));

        return createRoundRobinGrid(matchList, participantList);
    }

    public Event findEvent(String eventName) {
        ChallongeTournament challongeTournament = tournamentRepository
            .getTournament(eventName)
            .getTournament();

        Event result = new Event();
        result.setChallongeTournament(challongeTournament);
        result.setId(challongeTournament.getId());
        result.setName(challongeTournament.getUrl());

        return result;
    }

    public TournamentResultRequestLineItem[] createTournamentResultList(final String eventName) {
        final Event event = findEvent(eventName);
        final List<ChallongeParticipant> participantList = unwrapChallongeParticipantWrapperArray(
                participantRepository.getParticipantList(eventName));
        final List<ChallongeMatch> matchList = unwrapChallongeMatchWrapperArray(matchRepository.getMatchList(eventName));

        final Map<Integer, String> playerNameMap = createPlayerNameMap(participantList);

        return matchList.parallelStream()
                .filter(this::isMatchResultValid)
                .map(m -> {
                    final Integer player1 = m.getPlayer1Id();
                    final Integer player2 = m.getPlayer2Id();
                    final Integer winner = m.getWinnerId();
                    final String player1Name = playerNameMap.get(player1);
                    final String player2Name = playerNameMap.get(player2);

                    final TournamentResultRequestLineItem tournamentResultRequestLineItem =
                            new TournamentResultRequestLineItem();
                    tournamentResultRequestLineItem.setEventTitle(event.getChallongeTournament().getName());

                    final RoundRobinCell roundRobinCell = createRoundRobinCell(m);
                    tournamentResultRequestLineItem.setResultString(convertToGameSummary(roundRobinCell.getGameList()));

                    if (isWinForPlayer1(player1, player2, winner)) {
                        tournamentResultRequestLineItem.setWinner(player1Name);
                        tournamentResultRequestLineItem.setLoser(player2Name);
                        tournamentResultRequestLineItem.setResultString(convertToGameSummary(roundRobinCell.getGameList()));
                    } else {
                        final RoundRobinCell inverseCell = createInverseCell(roundRobinCell);
                        tournamentResultRequestLineItem.setWinner(player2Name);
                        tournamentResultRequestLineItem.setLoser(player1Name);
                        tournamentResultRequestLineItem.setResultString(convertToGameSummary(inverseCell.getGameList()));
                    }

                    return tournamentResultRequestLineItem;
                })
                .toArray(TournamentResultRequestLineItem[]::new);
    }
}
