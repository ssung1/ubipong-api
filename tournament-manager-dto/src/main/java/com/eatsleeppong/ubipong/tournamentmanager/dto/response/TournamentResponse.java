package com.eatsleeppong.ubipong.tournamentmanager.dto.response;

import com.eatsleeppong.ubipong.tournamentmanager.dto.common.TournamentCommon;
import com.eatsleeppong.ubipong.tournamentmanager.dto.request.TournamentRequest;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Value
@SuperBuilder
@EqualsAndHashCode(callSuper=true)
@Jacksonized
public class TournamentResponse extends TournamentCommon {
    private Integer id;
}
