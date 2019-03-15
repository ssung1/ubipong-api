package com.eatsleeppong.ubipong.model;

import lombok.Data;

import java.util.List;

import com.eatsleeppong.ubipong.model.challonge.ChallongeMatch;

@Data
public class Event {
    private Integer id;
    private String title;
    private String name;
    private String description;
    /**
     * References Tournament, not ChallongeTournament (which is really just an event)
     */
    private Integer tournamentId;
}
