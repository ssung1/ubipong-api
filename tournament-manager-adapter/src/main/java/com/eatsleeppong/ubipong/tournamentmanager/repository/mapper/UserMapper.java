package com.eatsleeppong.ubipong.tournamentmanager.repository.mapper;

import com.eatsleeppong.ubipong.tournamentmanager.domain.User;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserExternalReference;
import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaUser;

import org.springframework.stereotype.Component;

@Component("repositoryUserMapper")
public class UserMapper {
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
