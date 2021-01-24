package com.eatsleeppong.ubipong.tournamentmanager.dto;

import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
public class EventDto {
    private Integer id;
    private String challongeUrl;
    private String name;
    private Integer tournamentId;
}
