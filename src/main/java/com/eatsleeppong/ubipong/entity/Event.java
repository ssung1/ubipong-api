package com.eatsleeppong.ubipong.entity;

import com.eatsleeppong.ubipong.model.challonge.ChallongeTournament;
import lombok.Data;

import java.util.List;

import com.eatsleeppong.ubipong.model.challonge.ChallongeMatch;

import javax.persistence.*;

@Data
@Entity
// the events with a tournament must have unique names
@Table(indexes = {
    @Index(name = "tournament_id_name_idx", columnList = "tournamentId, name", unique = true)
})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq")
    private Integer eventId;
    /**
     * for now, this is the "url" of the tournament on challonge.com
     *
     * naming scheme:  ecs_{yyyyMM}_{event}_{type}_{groupNumber}
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
    private String name;

    /**
     * References Tournament, not ChallongeTournament (which is equavalent to Event in this application)
     */
    private Integer tournamentId;

    @Transient
    private ChallongeTournament challongeTournament;
}
