package com.eatsleeppong.ubipong.ratingmanager.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UsattMatchResultDto {
    /**
     * Not sure what this means to USATT, but it's in the sample file
     */
    private Integer eventId;
    private Integer winnerUsattNumber;
    private Integer loserUsattNumber;
    /**
     * Simplified score in the winner's point of view
     */
    private String score;
    /**
     * Event name.  Cannot contain comma
     */
    private String division;
}