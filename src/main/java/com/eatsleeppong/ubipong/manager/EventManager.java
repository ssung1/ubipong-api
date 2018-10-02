package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.model.Event;
import com.eatsleeppong.ubipong.model.challonge.ChallongeMatch;
import com.eatsleeppong.ubipong.model.challonge.ChallongeMatchWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventManager {
    public List<Integer> getPlayerList(Event event) {
        return new ArrayList<>();
    }

    public Map<Integer, List<ChallongeMatch>> groupByPlayer(Event event) {
        return null;
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
        return Arrays.asList(matchWrapperArray).stream()
            .map(ChallongeMatchWrapper::getChallongeMatch)
            .collect(Collectors.toList());
    }
}
