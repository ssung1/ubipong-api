package com.eatsleeppong.ubipong.tournamentmanager.dto.request;

import com.eatsleeppong.ubipong.tournamentmanager.dto.common.TournamentCommon;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * A tournament request; suitable for POST or PUT
 */
@Value
@SuperBuilder
@EqualsAndHashCode(callSuper=true)
@Jacksonized
public class TournamentRequest extends TournamentCommon {
}
