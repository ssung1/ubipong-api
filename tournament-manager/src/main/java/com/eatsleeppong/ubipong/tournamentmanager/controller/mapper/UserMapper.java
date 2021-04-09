package com.eatsleeppong.ubipong.tournamentmanager.controller.mapper;

import java.util.UUID;

import com.eatsleeppong.ubipong.tournamentmanager.domain.User;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserExternalReference;
import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaUser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
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

    public SpringJpaUser mapUserToSpringJpaUser(final User user) {
        final SpringJpaUser springJpaUser = new SpringJpaUser();

        springJpaUser.setId(user.getId());
        // for now, persisted external reference is just the external user reference
        springJpaUser.setExternalReference(user.getExternalReference().getUserReference());

        return springJpaUser;
    }

    public User mapSpringJpaUserToUser(final SpringJpaUser springJpaUser) {
        return User.builder()
            .id(springJpaUser.getId())
            // for now, persisted external reference is just the external user reference
            .externalReference(UserExternalReference.builder()
                .userReference(springJpaUser.getExternalReference())
                .build())
            .build();
    }
}

