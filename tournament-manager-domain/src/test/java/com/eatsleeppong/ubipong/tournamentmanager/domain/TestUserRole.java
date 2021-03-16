package com.eatsleeppong.ubipong.tournamentmanager.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestUserRole {
    @Test
    public void testEqualIfUserIdAndRoleAreEqual() {
        final UserRole userRole1 = UserRole.builder()
            .userId("id1")
            .role(Role.EVERYONE)
            .build();
        final UserRole userRole2 = UserRole.builder()
            .userId("id1")
            .role(Role.EVERYONE)
            .build();

        assertTrue(userRole1.equals(userRole2));
    }

    @Test
    public void testNotEqualIfUserIdsAreNotEqual() {
        final UserRole userRole1 = UserRole.builder()
            .userId("id1")
            .role(Role.EVERYONE)
            .build();
        final UserRole userRole2 = UserRole.builder()
            .userId("id2")
            .role(Role.EVERYONE)
            .build();

        assertFalse(userRole1.equals(userRole2));
    }

    @Test
    public void testNotEqualIfRolessAreNotEqual() {
        final UserRole userRole1 = UserRole.builder()
            .userId("id1")
            .role(Role.EVERYONE)
            .build();
        final UserRole userRole2 = UserRole.builder()
            .userId("id1")
            .role(Role.TOURNAMENT_ADMIN)
            .build();

        assertFalse(userRole1.equals(userRole2));
    }
}
