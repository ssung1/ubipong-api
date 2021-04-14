package com.eatsleeppong.ubipong.tournamentmanager.controller.mapper;

import java.util.UUID;

import com.eatsleeppong.ubipong.tournamentmanager.domain.User;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserExternalReference;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("controllerUserMapper")
public class UserMapper {
    public static final UserExternalReference TEST_EXTERNAL_REFERENCE = UserExternalReference.builder()
        .userReference("testExternalReference")
        .build();

    private final boolean securityEnabled;

    public UserMapper(@Value("${spring.security.enabled}") final boolean securityEnabled) {
        this.securityEnabled = securityEnabled;
    }

    public UserExternalReference mapAuthenticationToExternalReference(final Authentication authentication) {
        if (securityEnabled) {
            return UserExternalReference.builder()
                .userReference(authentication.getName())
                .build();
        } else {
            return TEST_EXTERNAL_REFERENCE;
        }
    }

    public User mapExternalReferenceToUser(final UserExternalReference externalReference) {
        return User.builder()
            .externalReference(externalReference)
            .id(UUID.randomUUID().toString())
            .build();
    }

    public User mapAuthenticationToUser(final Authentication authentication) {
        return mapExternalReferenceToUser(mapAuthenticationToExternalReference(authentication));
    }
}

