package com.eatsleeppong.ubipong.entity;

import lombok.Data;

import java.util.List;

import com.eatsleeppong.ubipong.model.challonge.ChallongeMatch;

import javax.persistence.*;

@Data
@Entity
// the events with a tournament must have unique names
@Table(indexes = {
    @Index(name = "tournamentid_name_idx", columnList = "tournamentId, name", unique = true)
})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq")
    private Integer id;
    private String title;

    /**
     * for now, this is the "url" of the tournament on challonge.com
     */
    private String name;

    private String description;

    /**
     * References Tournament, not ChallongeTournament (which is really just an event)
     */
    private Integer tournamentId;
}
