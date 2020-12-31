package com.eatsleeppong.ubipong.tournamentmanager.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TournamentResponse {
    private Integer id;
    private String name;
    private String tournamentDate;
}
