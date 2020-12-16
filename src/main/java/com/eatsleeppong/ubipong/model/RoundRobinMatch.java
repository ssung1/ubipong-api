package com.eatsleeppong.ubipong.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoundRobinMatch extends Match {
    /**
     * an integer indicating the ranking of the player within a round robin.  the higher ranked player has a lower
     * seed.  in our case, we are seeding our players using integers starting with player A = 1.
     */
    private String player1Seed;
    /**
     * an integer indicating the ranking of the player within a round robin.  the higher ranked player has a lower
     * seed.  in our case, we are seeding our players using integers starting with player A = 1.
     */
    private String player2Seed;

    private String player1Name;
    private String player2Name;
}
