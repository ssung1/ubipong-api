package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.util.List;

import lombok.Builder;
import lombok.Value;

/**
 * MatchResult is different from Match because
 * <ol>
 * <li>MatchResult is always complete</li>
 * <li>MatchResult is reported by winner/loser using exposed ID</li>
 * <li>MatchResult result string is from the winner's point of view</li>
 * <li>MatchResult has an event name</li>
 * </ol>
 */
@Value
@Builder
public class MatchResult {
    private String eventName;
    private String winnerName;
    private String winnerReferenceId;
    private String loserName;
    private String loserReferenceId;
    private List<Integer> scoreSummary;
}