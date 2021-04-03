package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.Builder.Default;

@Value
@Builder
@With
public class Event {
    /**
     * Overall round robin event, which contains all the players in the event
     */
    public static final String BRACKET_TYPE_ROUND_ROBIN = "round robin";
    /**
     * Often there are too many players entered in the round robin event.  To limit the number of matches, we need
     * to group players into smaller round robin events.
     */
    // public static final String BRACKET_TYPE_ROUND_ROBIN_GROUP = "round robin group";
    public static final String BRACKET_TYPE_SINGLE_ELIMINATION = "single elimination";
    public static final String BRACKET_TYPE_DOUBLE_ELIMINATION = "double elimination";

    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;

    Integer id;

    /**
     * References Tournament, (not ChallongeTournament, which is equivalent to Event in this application)
     */
    Integer tournamentId;

    /**
     * naming scheme:  {orgName}_{yyyyMM}_{event}_{type}_{groupNumber}
     *
     * where type is
     *
     *     rr: round robin
     *     se: single elimination
     *
     * where event is
     *
     *     pg: preliminary group
     *
     *     ch: championship
     *     ca: class a
     *     cb: class b
     *     cc: class c
     *     cd: class d
     *
     * where type is
     *
     *     rr: round robin
     *     se: single elimination
     *
     * where group is a number, only applicable in round robin
     */
    String challongeUrl;

    /**
     * Name of the event, which needs to be unique within a tournament
     */
    String name;

    /**
     * Type of the bracket
     */
    BracketType bracketType;

    /**
     * Status of event
     */
    @Default
    EventStatus status = EventStatus.CREATED;

    Instant startTime;

    public List<Player> getPlayerList() {
        final List<Player> playerListWithoutSeed = playerRepository.findByChallongeUrl(challongeUrl);
        return IntStream.range(0, playerListWithoutSeed.size())
            .mapToObj(index -> playerListWithoutSeed.get(index).withEventSeed(index))
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * A map of players, keyed by ID
     * @return
     */
    public Map<Integer, Player> getPlayerMap() {
        final List<Player> playerListWithoutSeed = playerRepository.findByChallongeUrl(challongeUrl);
        return IntStream.range(0, playerListWithoutSeed.size())
            .mapToObj(index -> playerListWithoutSeed.get(index).withEventSeed(index))
            .collect(Collectors.toMap(Player::getId, player -> player));
    }

    public List<Match> getMatchList() {
        return matchRepository.findByChallongeUrl(challongeUrl);
    }

    private MatchResult mapMatchToMatchResult(final Match match) {
        // we want to arrange the match so that player 1 is the winner
        final Match matchForReporting = match.transposeIfWinForPlayer2();
        final Map<Integer, Player> playerMap = getPlayerMap();
        final Player player1 = playerMap.get(matchForReporting.getPlayer1Id());
        final Player player2 = playerMap.get(matchForReporting.getPlayer2Id());
        final String player1Name = player1.getName();
        final String player2Name = player2.getName();
        final Integer player1ReferenceId = player1.getUsattNumber();
        final Integer player2ReferenceId = player2.getUsattNumber();
        return MatchResult.builder()
            .eventName(getName())
            .winnerName(player1Name)
            .winnerReferenceId(String.valueOf(player1ReferenceId))
            .loserName(player2Name)
            .loserReferenceId(String.valueOf(player2ReferenceId))
            .scoreSummary(matchForReporting.getScoreSummary())
            .build();
    }

    public List<MatchResult> getMatchResultList() {
        return getMatchList().stream()
            .map(this::mapMatchToMatchResult)
            .collect(Collectors.toUnmodifiableList());
    }

    private MatchSheet mapMatchToMatchSheet(final Match match) {
        final Map<Integer, Player> playerMap = getPlayerMap();
        final Player player1 = playerMap.get(match.getPlayer1Id());
        final Player player2 = playerMap.get(match.getPlayer2Id());
        return MatchSheet.builder()
            .eventName(getName())
            .matchId(match.getId())
            .player1Id(player1.getId())
            .player1UsattNumber(player1.getUsattNumber())
            .player1Name(player1.getName())
            .player1Seed(player1.getEventSeed())
            .player2Id(player2.getId())
            .player2UsattNumber(player2.getUsattNumber())
            .player2Name(player2.getName())
            .player2Seed(player2.getEventSeed())
            .build();
    }

    public List<MatchSheet> getMatchSheetList() {
        return getMatchList().stream()
            .filter(Match::arePlayersValid)
            .map(this::mapMatchToMatchSheet)
            .collect(Collectors.toUnmodifiableList());
    }

    private Optional<Match> filterMatchByPlayerIdTransposed(List<Match> matchList, Integer player1Id,
        Integer player2Id) {

        return matchList.stream()
            .filter(match -> 
                player1Id.equals(match.getPlayer2Id()) && player2Id.equals(match.getPlayer1Id()))
            .findFirst()
            .map(Match::transpose);
    }

    private Optional<Match> filterMatchByPlayerId(List<Match> matchList, Integer player1Id, Integer player2Id) {
        return matchList.stream()
            .filter(match -> 
                player1Id.equals(match.getPlayer1Id()) && player2Id.equals(match.getPlayer2Id()))
            .findFirst()
            .or(() -> this.filterMatchByPlayerIdTransposed(matchList, player1Id, player2Id));
    }

    public List<List<RoundRobinCell>> getRoundRobinGrid() {
        final RoundRobinCell empty = RoundRobinCell.builder().type(RoundRobinCellType.EMPTY).build();
        final List<List<RoundRobinCell>> roundRobinGrid = new ArrayList<>();
        final List<Player> playerList = getPlayerList();
        final List<Match> matchList = getMatchList();

        // header or first row: blank, blank, A, B, C, ...
        final List<RoundRobinCell> topHeader = new ArrayList<>();

        topHeader.add(RoundRobinCell.builder().build());
        topHeader.add(RoundRobinCell.builder().build());
        topHeader.addAll(getPlayerList().stream()
            .map(Player::getEventSeedAsAlphabet)
            .map(seed -> RoundRobinCell.builder().type(RoundRobinCellType.TEXT).content(seed).build())
            .collect(Collectors.toUnmodifiableList()));

        // for each player: A, player, match, match, match, ...
        //                  B, player, match, match, match, ...
        //                  C, player, match, match, match, ...
        //                  ...
        final List<List<RoundRobinCell>> playerRowList = playerList.stream().map(player -> {
            final List<RoundRobinCell> playerRow = new ArrayList<>();
            playerRow.add(RoundRobinCell.builder()
                .type(RoundRobinCellType.TEXT)
                .content(player.getEventSeedAsAlphabet())
                .build());
            playerRow.add(RoundRobinCell.builder()
                .type(RoundRobinCellType.NAME)
                .content(player.getName())
                .build());

            final List<Player> opponentList = getPlayerList();
            final List<RoundRobinCell> cells = opponentList.stream()
                .map(opponent -> filterMatchByPlayerId(matchList, player.getId(), opponent.getId())
                    .map(Match::toRoundRobinCell).orElse(empty))
                .collect(Collectors.toUnmodifiableList());

            playerRow.addAll(cells);
            return Collections.unmodifiableList(playerRow);
        })
        .collect(Collectors.toUnmodifiableList());

        roundRobinGrid.add(topHeader);
        roundRobinGrid.addAll(playerRowList);
        return roundRobinGrid;
    }
}
