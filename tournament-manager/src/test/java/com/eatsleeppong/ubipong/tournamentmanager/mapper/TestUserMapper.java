package com.eatsleeppong.ubipong.tournamentmanager.mapper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.eatsleeppong.ubipong.tournamentmanager.controller.mapper.UserMapper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserExternalReference;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

public class TestUserMapper {
    private final UserMapper userMapper = new UserMapper(true);

    @Test
    @DisplayName("should map an Authentication object to an external reference")
    public void testMapAuthenticationToExternalReference() {
        final String userName = "spongebob";
        final Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getName()).thenReturn(userName);

        final UserExternalReference userExternalReference = userMapper.mapAuthenticationToExternalReference(
            mockAuthentication);
            
        assertThat(userExternalReference.getUserReference(), is(userName));
    }

    @Test
    @DisplayName("should map an Authentication object to a test external reference when security is disabled")
    public void testMapAuthenticationToTestExternalReferenceWhenSecurityIsDisabled() {
        final UserMapper securityDisabledUserMapper = new UserMapper(false);
        final UserExternalReference externalReference = securityDisabledUserMapper
            .mapAuthenticationToExternalReference(null);
        assertThat(externalReference, is(UserMapper.TEST_EXTERNAL_REFERENCE));
    }
}
