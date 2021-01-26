package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import com.eatsleeppong.ubipong.tournamentmanager.domain.EventRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Player;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Match;
import com.eatsleeppong.ubipong.tournamentmanager.dto.response.RoundRobinMatch;
import com.eatsleeppong.ubipong.model.challonge.*;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequestLineItem;
import com.eatsleeppong.ubipong.tournamentmanager.repository.ChallongeMatchRepository;
import com.eatsleeppong.ubipong.tournamentmanager.repository.ChallongeParticipantRepository;
import com.eatsleeppong.ubipong.tournamentmanager.repository.ChallongeTournamentRepository;
import com.eatsleeppong.ubipong.tournamentmanager.repository.EventMapper;
import com.eatsleeppong.ubipong.tournamentmanager.repository.SpringJpaEventRepository;
import com.eatsleeppong.ubipong.tournamentmanager.dto.EventDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.response.Game;
import com.eatsleeppong.ubipong.tournamentmanager.dto.response.RoundRobinCell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventManager {
    private final ChallongeMatchRepository challongeMatchRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

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
        Event event) {

        List<Player> playerList = event.getPlayerList();

        int size = playerList.size();
        RoundRobinCell[][] innerGrid = new RoundRobinCell[size][size];

        // create an empty grid
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                innerGrid[i][j] = new RoundRobinCell();
            }
        }

        final Map<Integer, Integer> playerMap = event.getPlayerIndexMap();

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
            secondColumn.setContent(playerList.get(i).getName());
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
     * @param challongeUrl
     */
    public RoundRobinCell[][] createRoundRobinGrid(String challongeUrl) {
        Event event = eventRepository.getOneByChallongeUrl(challongeUrl);
        List<ChallongeMatch> matchList =
            unwrapChallongeMatchWrapperArray(
                challongeMatchRepository.getMatchList(challongeUrl));

        return createRoundRobinGrid(matchList, event);
    }

    public SpringJpaEvent addEvent(EventDto eventDto) {
        final Event eventToAdd = eventMapper.mapEventDtoToEvent(eventDto);
        final Event addedEvent = eventRepository.save(eventToAdd);
        return eventMapper.mapEventToSpringJpaEvent(addedEvent);
    }

    public TournamentResultRequestLineItem[] createTournamentResultList(final String challongeUrl) {
        final Event event = eventRepository.getOneByChallongeUrl(challongeUrl);

        final List<Match> matchList = event.getMatchList();

        final Map<Integer, String> playerNameMap = event.getPlayerNameMap();

        return matchList.stream()
            .filter(Match::isResultValid)
            .map(m -> {
                final Integer player1 = m.getPlayer1Id();
                final Integer player2 = m.getPlayer2Id();
                final Integer winner = m.getWinnerId();
                final String player1Name = playerNameMap.get(player1);
                final String player2Name = playerNameMap.get(player2);

                final TournamentResultRequestLineItem tournamentResultRequestLineItem =
                        new TournamentResultRequestLineItem();
                tournamentResultRequestLineItem.setEventName(event.getName());

                tournamentResultRequestLineItem.setResultString(m.getScoreSummary());

                if (isWinForPlayer1(player1, player2, winner)) {
                    tournamentResultRequestLineItem.setWinner(player1Name);
                    tournamentResultRequestLineItem.setLoser(player2Name);
                    tournamentResultRequestLineItem.setResultString(m.getScoreSummary());
                } else {
                    tournamentResultRequestLineItem.setWinner(player2Name);
                    tournamentResultRequestLineItem.setLoser(player1Name);
                    tournamentResultRequestLineItem.setResultString(m.transpose().getScoreSummary());
                }

                return tournamentResultRequestLineItem;
            })
            .toArray(TournamentResultRequestLineItem[]::new);
    }

    public List<RoundRobinMatch> createRoundRobinMatchList(final String challongeUrl) {
        final Event event = eventRepository.getOneByChallongeUrl(challongeUrl);
        final List<ChallongeMatch> matchList = unwrapChallongeMatchWrapperArray(challongeMatchRepository.getMatchList(challongeUrl));

        final Map<Integer, Integer> playerIndexMap = event.getPlayerIndexMap();
        final Map<Integer, String> playerNameMap = event.getPlayerNameMap();

        return matchList.stream().map(m -> {
            final RoundRobinMatch roundRobinMatch = new RoundRobinMatch();
            final Integer player1Id = m.getPlayer1Id();
            final Integer player2Id = m.getPlayer2Id();

            roundRobinMatch.setMatchId(m.getId());

            if (ChallongeMatch.STATE_COMPLETE.equals(m.getState())) {
                roundRobinMatch.setStatus(Match.STATUS_COMPLETE);
                // currently there is no way to record default
                roundRobinMatch.setResultCode(Match.RESULT_CODE_WIN_BY_PLAYING);
            } else {
                roundRobinMatch.setStatus(Match.STATUS_INCOMPLETE);
            }

            roundRobinMatch.setPlayer1Id(player1Id);
            roundRobinMatch.setPlayer2Id(player2Id);

            roundRobinMatch.setPlayer1Name(playerNameMap.get(player1Id));
            roundRobinMatch.setPlayer2Name(playerNameMap.get(player2Id));

            roundRobinMatch.setPlayer1Seed(numberToLetter(playerIndexMap.get(player1Id)));
            roundRobinMatch.setPlayer2Seed(numberToLetter(playerIndexMap.get(player2Id)));

            return roundRobinMatch;
        }).collect(Collectors.toList());
    }
}
