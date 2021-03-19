package com.eatsleeppong.ubipong.tournamentmanager.mapper;

import java.util.UUID;

import com.eatsleeppong.ubipong.tournamentmanager.domain.User;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserExternalReference;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserExternalReference mapAuthenticationToUserExternalReference(final Authentication authentication) {
        return UserExternalReference.builder()
            .userReference(authentication.getName())
            .build();
    }

    public User mapAuthenticationToUser(final Authentication authentication) {
        return User.builder()
            .externalReference(UserExternalReference.builder()
                .userReference(authentication.getName())
                .build()
            )
            .id(UUID.randomUUID().toString())
            .build();
    }
}

