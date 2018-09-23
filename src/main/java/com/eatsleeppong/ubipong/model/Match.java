package com.eatsleeppong.ubipong.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Match {
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
    private Date updatedAtAt;
    private String identifier;
    private Boolean hasAttachment;
    private Integer round;
    private Boolean player1Votes;
    private Boolean player2Votes;
    private Integer groupId;
    private Integer attachmentCount;
    private Date scheduledTime;
    private String location;
    private Date underwayAt;
    private Boolean optional;
    private Integer rushbId;
    private Date completedAt;
    private Integer suggestedPlayOrder;
    private Boolean forfeited;
    private String prerequisiteMatchIdsCsv;
    private String scoresCsv;
}