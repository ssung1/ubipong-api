package com.eatsleeppong.ubipong.model.challonge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

import java.util.Date;


@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ChallongeMatch {
    // match has result, but may not be actually complete
    public static final String STATE_COMPLETE = "complete";
    // match has no result (yet)
    public static final String STATE_OPEN = "open";

    private Integer id;
    private Integer tournamentId;
    private String state;
    private Integer player1Id;
    private Integer player2Id;
    private Integer player1PrereqMatchId;
    private Integer player2PrereqMatchId;
    private Boolean player1IsPrereqMatchLoser;
    private Boolean player2IsPrereqMatchLoser;
    private Integer winnerId;
    private Integer loserId;
    private Date startedAt;
    private Date createdAt;
    private Date updatedAt;
    private String identifier;
    private Boolean hasAttachment;
    private Integer round;
    private Boolean player1Votes;
    private Boolean player2Votes;
    private Integer groupId;
    private Integer attachmentCount;
    private Date scheduledTime;
    private String location;
    // if non-null, then the match is currently in progress
    private Date underwayAt;
    private Boolean optional;
    private Integer rushbId;
    private Date completedAt;
    private Integer suggestedPlayOrder;
    private Boolean forfeited;
    private String prerequisiteMatchIdsCsv;
    private String scoresCsv;
}
