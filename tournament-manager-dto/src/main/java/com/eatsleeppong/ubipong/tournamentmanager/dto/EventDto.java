package com.eatsleeppong.ubipong.tournamentmanager.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.Builder.Default;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // replace with @Jacksonized after VSCode is fixed
@AllArgsConstructor // remove after VSCode is fixed
public class EventDto {
    @ApiModelProperty(value = "ID of the event; not required if creating a new event")
    private Integer id;
    private Integer tournamentId;
    private String challongeUrl;
    private String name;
    
    @ApiModelProperty(value = "Status of the event; ignored if creating a new event")
    @Default
    private EventStatusDto status = EventStatusDto.CREATED;

    private Instant startTime;
}
