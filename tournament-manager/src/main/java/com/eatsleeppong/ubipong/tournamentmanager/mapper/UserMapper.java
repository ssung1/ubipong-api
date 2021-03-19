package com.eatsleeppong.ubipong.tournamentmanager.mapper;

import java.util.UUID;

import com.eatsleeppong.ubipong.tournamentmanager.domain.User;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserExternalReference;
import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaUser;

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

