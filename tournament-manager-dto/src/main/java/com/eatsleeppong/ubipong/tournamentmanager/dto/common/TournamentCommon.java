package com.eatsleeppong.ubipong.tournamentmanager.dto.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.SuperBuilder;

/**
 * Try to see if it makes sense to put common parts in here
 */
@SuperBuilder
@Getter
@EqualsAndHashCode
public class TournamentCommon {
    private String name;
    private String tournamentDate;
}
