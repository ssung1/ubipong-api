package com.eatsleeppong.ubipong.tournamentmanager.domain;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

/**
 * Tournament Setup:
 *
 * Bikini Bottom Open 2019       Jun 23, 2019
 * Event: Preliminary Group 1 (ID: 100)
 * Players: spongebob (ID: 1), patrick (ID: 2), and squidward (ID: 3)
 * Scores:
 * spongebob vs patrick: patrick wins 3 5 1 (ID: 100)
 * spongebob vs squidward: spongebob wins 11 -5 9 9 (ID: 101)
 * patrick vs squidward: not played yet (ID: 102)
 */
public class TestHelper {
    public static final int TOURNAMENT_ID = 10;
    public static final int SPONGEBOB_ID = 1;
    public static final int PATRICK_ID = 2;
    public static final int SQUIDWARD_ID = 3;
    public static final int SPONGEBOB_USATT = 10001;
    public static final int PATRICK_USATT = 10002;
    public static final int SQUIDWARD_USATT = 10003;
    public static final String CHALLONGE_URL = "esp_201903_pg_rr_1";
    public static final String EVENT_NAME = "Preliminary Group 1";

    public static Tournament createTournament() {
        final EventRepository mockEventRepository = mock(EventRepository.class);
        final Event event = createEvent();
        when(mockEventRepository.findByTournamentId(TOURNAMENT_ID)).thenReturn(List.of(
            event
        ));
        return Tournament.builder()
            .id(TOURNAMENT_ID)
            .eventRepository(mockEventRepository)
            .name("Eat Sleep Pong Open 2019")
            .tournamentDate(Instant.parse("2019-03-16T12:00:00Z"))
            .build();
    }

    public static Event createEvent() {
        final PlayerRepository mockPlayerRepository = mock(PlayerRepository.class);
        final MatchRepository mockMatchRepository = mock(MatchRepository.class);

        when(mockPlayerRepository.findByChallongeUrl(CHALLONGE_URL)).thenReturn(List.of(
            createPlayerSpongebob(), createPlayerPatrick(), createPlayerSquidward()
        ));

        when(mockMatchRepository.findByChallongeUrl(CHALLONGE_URL)).thenReturn(List.of(
            createMatch1(), createMatch2(), createMatch3()
        ));

        return Event.builder()
            .id(100)
            .name("Preliminary Group 1")
            .challongeUrl(CHALLONGE_URL)
            .playerRepository(mockPlayerRepository)
            .matchRepository(mockMatchRepository)
            .tournamentId(TOURNAMENT_ID)
            .build();
    }

    public static Player createPlayerSpongebob() {
        return Player.builder()
            .id(SPONGEBOB_ID)
            .name("spongebob")
            .usattNumber(SPONGEBOB_USATT)
            .build();
    }

    public static Player createPlayerPatrick() {
        return Player.builder()
            .id(PATRICK_ID)
            .name("patrick")
            .usattNumber(PATRICK_USATT)
            .build();
    }

    public static Player createPlayerSquidward() {
        return Player.builder()
            .id(SQUIDWARD_ID)
            .name("squidward")
            .usattNumber(SQUIDWARD_USATT)
            .build();
    }

    public static Match createMatch1() {
        return Match.builder()
            .id(1000)
            .player1Id(SPONGEBOB_ID)
            .player2Id(PATRICK_ID)
            .resultCode(Match.RESULT_CODE_WIN_BY_PLAYING)
            .winnerId(PATRICK_ID)
            .gameList(List.of(
                Game.builder().scores("3-11").build(),
                Game.builder().scores("5-11").build(),
                Game.builder().scores("1-11").build()
            ))
            .build();
    }

    public static Match createMatch2() {
        return Match.builder()
            .id(1001)
            .player1Id(SPONGEBOB_ID)
            .player2Id(SQUIDWARD_ID)
            .resultCode(Match.RESULT_CODE_WIN_BY_PLAYING)
            .winnerId(SPONGEBOB_ID)
            .gameList(List.of(
                Game.builder().scores("13-11").build(),
                Game.builder().scores("5-11").build(),
                Game.builder().scores("11-9").build(),
                Game.builder().scores("11-9").build()
            ))
            .build();
    }

    public static Match createMatch3() {
        return Match.builder()
            .id(1001)
            .player1Id(PATRICK_ID)
            .player2Id(SQUIDWARD_ID)
            .build();
    }
}