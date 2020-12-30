package com.eatsleeppong.ubipong.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.With;

import javax.persistence.*;
import java.util.Date;

/**
 * The main purpose of this is to contain a set of events, since Challonge does not have the feature
 */
@Value
@Builder
@Entity
@With
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tournament_seq")
    private Integer tournamentId;

    @Column(nullable = false)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date tournamentDate;
}
