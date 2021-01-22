package com.eatsleeppong.ubipong.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * The main purpose of this is to contain a set of events, since Challonge does not have the feature
 */
@Data
@Entity(name = "tournament")
@Table(name = "tournament")
@AllArgsConstructor
@NoArgsConstructor
@With
public class SpringJpaTournament {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tournament_seq")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date tournamentDate;
}
