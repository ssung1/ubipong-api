package com.eatsleeppong.ubipong.tournamentmanager.controller.mapper;

import com.eatsleeppong.ubipong.tournamentmanager.domain.User;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRole;
import com.eatsleeppong.ubipong.tournamentmanager.dto.RoleDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.UserRoleDto;

import org.springframework.stereotype.Component;

@Component("controllerUserRoleMapper")
public class UserRoleMapper {
    public UserRoleDto mapUserRoleAndUserToUserRoleDto(final UserRole userRole, final User user) {
        return UserRoleDto.builder()
            .user(user.getExternalReference().getUserReference())
            .role(RoleDto.valueOf(userRole.getRole().name()))
            .build();
    }
}
