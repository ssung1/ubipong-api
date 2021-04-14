package com.eatsleeppong.ubipong.tournamentmanager.repository;

import java.util.Set;

import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaUserTournamentRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.query.Param;

@RepositoryRestResource(collectionResourceRel = "userTournamentRolls", path = "userTournamentRoles")
public interface SpringJpaUserTournamentRoleRepository extends JpaRepository<SpringJpaUserTournamentRole, String> {
    Set<SpringJpaUserTournamentRole> findByUserId(@Param("userId") String userId);
    Set<SpringJpaUserTournamentRole> findByTournamentId(@Param("tournamentId") Integer tournamentId);
}
