package com.eatsleeppong.ubipong.tournamentmanager.dto;

import java.util.Set;

import java.time.Instant;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // replace with @Jacksonized after VSCode is fixed
@AllArgsConstructor // remove after VSCode is fixed
public class TournamentDto {
    Integer id;

    @Size(max = 60)
    String name;

    Instant tournamentDate;

    Set<UserRoleDto> userRoleSet;
}
