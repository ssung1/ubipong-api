package com.eatsleeppong.ubipong.tournamentmanager.domain;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class Player {
    private final Integer id;
    private final String name;
    private final Integer usattNumber;
    private final Integer rating;
    /**
     * Event seed is the position of the player within an event.
     * Usually the best player would have a seed of 0,
     * second best a seed of 1, and so on
     */
    private final Integer eventSeed;

    /**
     * Instead of number, report the seed within the event as
     * letters of the alphabet, A being 0, B being 1, and so on
     */
    public String getEventSeedAsAlphabet() {
        return Character.toString((char)('A' + eventSeed));
    }
}