package com.eatsleeppong.ubipong.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * The main purpose of this is to contain a set of events, since Challonge does not have the feature
 */
@Data
@Entity
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tournament_seq")
    private Integer tournamentId;

    @Column(nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date tournamentDate;
}
