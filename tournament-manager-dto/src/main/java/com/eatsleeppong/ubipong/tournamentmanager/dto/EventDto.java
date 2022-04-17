package com.eatsleeppong.ubipong.tournamentmanager.dto;

import java.time.Instant;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.Builder.Default;

@Value
@Builder
@With
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // replace with @Jacksonized after VSCode is fixed
@AllArgsConstructor // remove after VSCode is fixed
public class EventDto {
    @ApiModelProperty(value = "ID of the event; not required if creating a new event")
    private Integer id;
    private Integer tournamentId;
    private String challongeUrl;
    @Size(max = 60)
    private String name;
    
    @ApiModelProperty(value = "Status of the event; ignored if creating a new event")
    @Default
    private EventStatusDto status = EventStatusDto.CREATED;

    @ApiModelProperty(value = "Starting time of the event, including date")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant startTime;
}
