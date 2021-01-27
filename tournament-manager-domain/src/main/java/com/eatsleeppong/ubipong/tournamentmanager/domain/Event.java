package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.Value;

// this is work in progress
// meant to replace SpringJpaEvent
@Value
@Builder
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
     * References Tournament, (not ChallongeTournament, which is equivalent to Event in this application)
     */
    Integer tournamentId;

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
    public Map<Integer, Integer> getPlayerIndexMap() {

        final List<Player> playerList = playerRepository.findByChallongeUrl(challongeUrl);

        Map<Integer, Integer> result = new HashMap<>();
        for (int index = 0; index < playerList.size(); ++index) {
            result.put(playerList.get(index).getId(), index);
        }

        return result;
    }

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
}
