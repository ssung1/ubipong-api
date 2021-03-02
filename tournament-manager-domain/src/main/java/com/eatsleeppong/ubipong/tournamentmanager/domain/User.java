package com.eatsleeppong.ubipong.tournamentmanager.domain;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class User {
    /**
     * unique, externally visible identifier
     */
    String email;
}
