package com.eatsleeppong.ubipong.tournamentmanager.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Player {
    private final Integer id;
    private final String name;
    private final Integer usattNumber;
    private final Integer rating;
}