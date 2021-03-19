package com.eatsleeppong.ubipong.tournamentmanager.domain;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
/**
 * This is an external reference to some kind of identity service.  For Okta,
 * we just have email, but later on, we may want to know if the user is logging
 * in using some other social medial account.
 */
public class UserExternalReference {
    String userReference;
}
