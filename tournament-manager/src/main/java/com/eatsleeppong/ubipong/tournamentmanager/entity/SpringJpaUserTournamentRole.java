package com.eatsleeppong.ubipong.tournamentmanager.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity(name = "userTournamentRole")
@Table(name = "user_tournament_role", indexes = {
    @Index(name = "user_id_tournament_id_role_idx", columnList = "userId, tournamentId, role", unique = true)
})
public class SpringJpaUserTournamentRole {
    private String userId;
    private Integer tournamentId;
    private String role;
}
