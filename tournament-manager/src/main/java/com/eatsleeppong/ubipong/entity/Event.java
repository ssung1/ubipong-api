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
    /**
     * Overall round robin event, which contains all the players in the event
     */
    public static final String EVENT_TYPE_ROUND_ROBIN = "round robin";
    /**
     * Often there are too many players entered in the round robin event.  To limit the number of matches, we need
     * to group players into smaller round robin events.
     */
    public static final String EVENT_TYPE_ROUND_ROBIN_GROUP = "round robin group";
    public static final String EVENT_TYPE_SINGLE_ELIMINATION = "single elimination";
    public static final String EVENT_TYPE_DOUBLE_ELIMINATION = "double elimination";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq")
    private Integer eventId;

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
    private String challongeUrl;

    /**
     * Name of the event, which needs to be unique within a tournament
     */
    private String name;

    /**
     * References Tournament, (not ChallongeTournament, which is equivalent to Event in this application)
     */
    private Integer tournamentId;

    @Transient
    private ChallongeTournament challongeTournament;
}
