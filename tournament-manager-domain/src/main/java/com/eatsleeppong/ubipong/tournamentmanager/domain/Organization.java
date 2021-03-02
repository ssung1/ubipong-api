package com.eatsleeppong.ubipong.tournamentmanager.domain;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class Organization {
    String id;
    /**
     * unique, externally visible identifier
     */
    String name;
    String logo;
}
