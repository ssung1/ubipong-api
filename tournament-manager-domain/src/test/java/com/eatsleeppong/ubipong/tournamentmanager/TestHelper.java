package com.eatsleeppong.ubipong.tournamentmanager;

import static org.mockito.Mockito.mock;

import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Player;
import com.eatsleeppong.ubipong.tournamentmanager.domain.PlayerRepository;

/**
 * Tournament Setup:
 *
 * Bikini Bottom Open 2019       Jun 23, 2019
 * Event: Preliminary Group 1 (ID: 100)
 * Players: spongebob (ID: 1), patrick (ID: 2), and squidward (ID: 3)
 * Scores:
 * spongebob vs patrick: patrick wins 3 5 1 (ID: 100)
 * spongebob vs squidward: spongebob wins 13 -5 9 9 (ID: 101)
 * patrick vs squidward: patrick wins 3 3 3 (ID: 102)
 */
public class TestHelper {
    public static Event createEvent() {
        return Event.builder()
            .id(100)
            .name("Preliminary Group 1")
            .challongeUrl("esp_201903_pg_rr_1")
            .playerRepository(mock(PlayerRepository.class))
            .build();
    }

    public static Player createPlayerSpongebob() {
        return Player.builder()
            .id(1)
            .name("spongebob")
            .build();
    }

    public static Player createPlayerPatrick() {
        return Player.builder()
            .id(2)
            .name("patrick")
            .build();
    }

    public static Player createPlayerSquidward() {
        return Player.builder()
            .id(3)
            .name("patrick")
            .build();
    }
}