package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.entity.SpringJpaTournament;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequest;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequestLineItem;
import com.eatsleeppong.ubipong.repo.SpringJpaEventRepository;
import com.eatsleeppong.ubipong.repo.SpringJpaTournamentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.when;

public class TestTournamentManager {
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    private final Integer tournamentId = 1234987;
    private final String tournamentName = "Eat Sleep Pong Open 2019";
    private final String tournamentDate = "2019-03-15T00:00:00-0500";
    private final String event1ChallongeUrl = "pr_gr_1";
    private final String event2ChallongeUrl = "ca";
    private final String event1Name = "Preliminary Group 1";
    private final String event2Name = "Class A";

    private final TournamentResultRequestLineItem event1Game1 = new TournamentResultRequestLineItem();
    private final TournamentResultRequestLineItem event1Game2 = new TournamentResultRequestLineItem();
    private final TournamentResultRequestLineItem event2Game1 = new TournamentResultRequestLineItem();
    private final TournamentResultRequestLineItem event2Game2 = new TournamentResultRequestLineItem();

    private final SpringJpaEventRepository mockEventRepository = mock(SpringJpaEventRepository.class);
    private final EventManager mockEventManager = mock(EventManager.class);
    private final SpringJpaTournamentRepository mockTournamentRepository = mock(SpringJpaTournamentRepository.class);

    private final TournamentManager tournamentManager = new TournamentManager(mockEventManager, mockEventRepository,
            mockTournamentRepository);

    @BeforeEach
    public void setupMocks() throws Exception {
        // because Lombok creates the equals method, each TournamentResultRequestLineItem needs to have different
        // content in order to be considered different (cannot use referential equality)
        event1Game1.setEventName(event1Name);
        event1Game1.setResultString("event1game1result");
        event1Game2.setEventName(event1Name);
        event1Game2.setResultString("event1game2result");
        event2Game1.setEventName(event2Name);
        event2Game1.setResultString("event2game1result");
        event2Game2.setEventName(event2Name);
        event2Game2.setResultString("event2game2result");

        final SpringJpaEvent event1 = new SpringJpaEvent();
        event1.setChallongeUrl(event1ChallongeUrl);

        final SpringJpaEvent event2 = new SpringJpaEvent();
        event2.setChallongeUrl(event2ChallongeUrl);
        when(mockEventRepository.findByTournamentId(tournamentId)).thenReturn(Arrays.asList(event1, event2));

        when(mockEventManager.createTournamentResultList(event1ChallongeUrl)).thenReturn(
                new TournamentResultRequestLineItem[] { event1Game1, event1Game2 });
        when(mockEventManager.createTournamentResultList(event2ChallongeUrl)).thenReturn(
                new TournamentResultRequestLineItem[] { event2Game1, event2Game2 });

        final SpringJpaTournament tournament = new SpringJpaTournament();
        tournament.setName(tournamentName);
        tournament.setTournamentDate(df.parse(tournamentDate));

        when(mockTournamentRepository.getOne(tournamentId)).thenReturn(tournament);
    }

    @Test
    public void testCreateTournamentResultRequest() throws Exception {
        final TournamentResultRequest tournamentResultRequest =
                tournamentManager.createTournamentResultRequest(tournamentId);
        assertThat(tournamentResultRequest.getTournamentName(), is(tournamentName));
        assertThat(tournamentResultRequest.getTournamentDate(), is(df.parse(tournamentDate)));
        assertThat(tournamentResultRequest.getTournamentResultList(), arrayWithSize(4));
        
        final TournamentResultRequestLineItem[] tournamentResultRequestLineItem = 
                tournamentResultRequest.getTournamentResultList();

        assertThat(tournamentResultRequestLineItem, hasItemInArray(event1Game1));
        assertThat(tournamentResultRequestLineItem, hasItemInArray(event1Game2));
        assertThat(tournamentResultRequestLineItem, hasItemInArray(event2Game1));
        assertThat(tournamentResultRequestLineItem, hasItemInArray(event2Game2));
    }
}
