package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.entity.SpringJpaTournament;
import com.eatsleeppong.ubipong.ratingmanager.dto.MatchResultDto;
import com.eatsleeppong.ubipong.ratingmanager.dto.TournamentResultDto;
import com.eatsleeppong.ubipong.tournamentmanager.repository.SpringJpaEventRepository;
import com.eatsleeppong.ubipong.tournamentmanager.repository.SpringJpaTournamentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.when;

public class TestTournamentManager {
    private final Integer tournamentId = 1234987;
    private final String tournamentName = "Eat Sleep Pong Open 2019";
    private final String tournamentDate = "2019-03-15T00:00:00-05:00";
    private final String event1ChallongeUrl = "pr_gr_1";
    private final String event2ChallongeUrl = "ca";
    private final String event1Name = "Preliminary Group 1";
    private final String event2Name = "Class A";

    private MatchResultDto event1Game1;
    private MatchResultDto event1Game2;
    private MatchResultDto event2Game1;
    private MatchResultDto event2Game2;

    private final SpringJpaEventRepository mockEventRepository = mock(SpringJpaEventRepository.class);
    private final EventManager mockEventManager = mock(EventManager.class);
    private final SpringJpaTournamentRepository mockTournamentRepository = mock(SpringJpaTournamentRepository.class);

    private final TournamentManager tournamentManager = new TournamentManager(mockEventManager, mockEventRepository,
            mockTournamentRepository);

    @BeforeEach
    public void setupMocks() throws Exception {
        // because Lombok creates the equals method, each TournamentResultRequestLineItem needs to have different
        // content in order to be considered different (cannot use referential equality)
        event1Game1 = MatchResultDto.builder()
            .eventName(event1Name)
            .resultString("event1game1result")
            .build();

        event1Game2 = MatchResultDto.builder()
            .eventName(event1Name)
            .resultString("event1game2result")
            .build();

        event2Game1 = MatchResultDto.builder()
            .eventName(event2Name)
            .resultString("event2game1result")
            .build();

        event2Game2 = MatchResultDto.builder()
            .eventName(event2Name)
            .resultString("event2game2result")
            .build();

        final SpringJpaEvent event1 = new SpringJpaEvent();
        event1.setChallongeUrl(event1ChallongeUrl);

        final SpringJpaEvent event2 = new SpringJpaEvent();
        event2.setChallongeUrl(event2ChallongeUrl);
        when(mockEventRepository.findByTournamentId(tournamentId)).thenReturn(Arrays.asList(event1, event2));

        when(mockEventManager.createTournamentResultList(event1ChallongeUrl)).thenReturn(
                new MatchResultDto[] { event1Game1, event1Game2 });
        when(mockEventManager.createTournamentResultList(event2ChallongeUrl)).thenReturn(
                new MatchResultDto[] { event2Game1, event2Game2 });

        final SpringJpaTournament tournament = new SpringJpaTournament();
        tournament.setName(tournamentName);
        tournament.setTournamentDate(Date.from(OffsetDateTime.parse(tournamentDate).toInstant()));

        when(mockTournamentRepository.getOne(tournamentId)).thenReturn(tournament);
    }

    @Test
    public void testCreateTournamentResultRequest() throws Exception {
        final TournamentResultDto tournamentResultDto =
            tournamentManager.createTournamentResultRequest(tournamentId);
        assertThat(tournamentResultDto.getTournamentName(), is(tournamentName));
        assertThat(tournamentResultDto.getTournamentDate(), is(OffsetDateTime.parse(tournamentDate).toInstant()));
        assertThat(tournamentResultDto.getTournamentResultList(), arrayWithSize(4));
        
        final MatchResultDto[] matchResultDtoList = 
            tournamentResultDto.getTournamentResultList();

        assertThat(matchResultDtoList, hasItemInArray(event1Game1));
        assertThat(matchResultDtoList, hasItemInArray(event1Game2));
        assertThat(matchResultDtoList, hasItemInArray(event2Game1));
        assertThat(matchResultDtoList, hasItemInArray(event2Game2));
    }
}
