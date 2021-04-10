package com.eatsleeppong.ubipong.tournamentmanager.repository.mapper;

import java.util.UUID;

import com.eatsleeppong.ubipong.tournamentmanager.domain.Role;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRole;
import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaUserTournamentRole;

import org.springframework.stereotype.Component;

@Component("repositoryUserRoleMapper")
public class UserRoleMapper {
    public SpringJpaUserTournamentRole mapUserRoleToSpringJpaUserTournamentRole(
        final UserRole userRole, final Integer tournamentId) {

        final SpringJpaUserTournamentRole springJpaUserTournamentRole = new SpringJpaUserTournamentRole();

        springJpaUserTournamentRole.setId(UUID.randomUUID().toString());
        springJpaUserTournamentRole.setTournamentId(tournamentId);
        springJpaUserTournamentRole.setUserId(userRole.getUserId());
        springJpaUserTournamentRole.setRole(userRole.getRole().name());

        return springJpaUserTournamentRole;
    }

    public UserRole mapSpringJpaUserTournamentRoleToUserRole(
        final SpringJpaUserTournamentRole springJpaUserTournamentRole) {
        
        return UserRole.builder()
            .userId(springJpaUserTournamentRole.getUserId())
            .role(Role.valueOf(springJpaUserTournamentRole.getRole()))
            .build();
    }
}
