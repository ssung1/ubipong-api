package com.eatsleeppong.ubipong.tournamentmanager.repository;

import java.util.List;
import java.util.Optional;

import com.eatsleeppong.ubipong.tournamentmanager.TestHelper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.User;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TestUserRepositoryImpl {
    @Autowired
    private UserRepositoryImpl userRepository;

    private User addUser(final User user) {
        return userRepository.save(user);
    }

    @Test
    @DisplayName("should return an user by user ID")
    public void testGetOne() {
        final User addedUser = addUser(TestHelper.createUser());

        final String userId = addedUser.getId();

        final User user = userRepository.getOne(userId);

        assertThat(user, notNullValue());
        assertThat(user.getExternalReference(), is(addedUser.getExternalReference()));
    }

    @Test
    @DisplayName("should return an user by external reference")
    public void testGetOneByExternalReference() {
        final User addedUser = addUser(TestHelper.createUser());

        final Optional<User> user = userRepository.findByExternalReference(addedUser.getExternalReference());

        assertTrue(user.isPresent());
        assertThat(user.get().getId(), is(addedUser.getId()));
    }
}

