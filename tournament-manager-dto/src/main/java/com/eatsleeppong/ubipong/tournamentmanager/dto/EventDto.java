package com.eatsleeppong.ubipong.tournamentmanager.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // replace with @Jacksonized after VSCode is fixed
@AllArgsConstructor // remove after VSCode is fixed
public class EventDto {
    private Integer id;
    private String challongeUrl;
    private String name;
    private Integer tournamentId;
}
