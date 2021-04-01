package com.eatsleeppong.ubipong.tournamentmanager.mapper;

import java.util.UUID;

import com.eatsleeppong.ubipong.tournamentmanager.domain.Role;
import com.eatsleeppong.ubipong.tournamentmanager.domain.User;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserExternalReference;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRole;
import com.eatsleeppong.ubipong.tournamentmanager.dto.RoleDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.UserRoleDto;
import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaUserTournamentRole;

import org.springframework.stereotype.Component;

@Component
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

    public UserRoleDto mapUserRoleAndUserToUserRoleDto(final UserRole userRole, final User user) {
        return UserRoleDto.builder()
            .user(user.getExternalReference().getUserReference())
            .role(RoleDto.valueOf(userRole.getRole().name()))
            .build();
    }
}
