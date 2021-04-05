package com.eatsleeppong.ubipong.tournamentmanager.dto;

import java.util.Collections;
import java.util.List;

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
public class RoundRobinCellDto {
    @Builder.Default
    RoundRobinCellTypeDto type = RoundRobinCellTypeDto.EMPTY;

    @Builder.Default
    String content = "";

    boolean winForPlayer1;
    boolean winByDefault;

    @Builder.Default
    List<GameDto> gameList = Collections.emptyList();
}
