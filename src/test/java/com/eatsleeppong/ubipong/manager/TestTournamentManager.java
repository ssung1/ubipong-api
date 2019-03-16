package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.entity.Event;
import com.eatsleeppong.ubipong.entity.Tournament;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequest;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequestLineItem;
import com.eatsleeppong.ubipong.repo.EventRepository;
import com.eatsleeppong.ubipong.repo.TournamentRepository;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class TestTournamentManager {
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    private final Integer tournamentId = 1234987;
    private final String tournamentName = "Eat Sleep Pong Open 2019";
    private final String tournamentDate = "2019-03-15T00:00:00-0500";
    private final String event1Name = "pr_gr_1";
    private final String event2Name = "ca";
    private final String event1Title = "Preliminary Group 1";
    private final String event2Title = "Class A";

    private final TournamentResultRequestLineItem event1Game1 = new TournamentResultRequestLineItem();
    private final TournamentResultRequestLineItem event1Game2 = new TournamentResultRequestLineItem();
    private final TournamentResultRequestLineItem event2Game1 = new TournamentResultRequestLineItem();
    private final TournamentResultRequestLineItem event2Game2 = new TournamentResultRequestLineItem();

    private final EventRepository mockEventRepository = mock(EventRepository.class);
    private final EventManager mockEventManager = mock(EventManager.class);
    private final TournamentRepository mockTournamentRepository = mock(TournamentRepository.class);

    private final TournamentManager tournamentManager = new TournamentManager(mockEventManager, mockEventRepository,
            mockTournamentRepository);

    @Before
    public void setupMocks() throws Exception {
        // because Lombok creates the equals method, each TournamentResultRequestLineItem needs to have different
        // content in order to be considered different (cannot use referential equality)
        event1Game1.setEventTitle(event1Title);
        event1Game1.setResultString("event1game1result");
        event1Game2.setEventTitle(event1Title);
        event1Game2.setResultString("event1game2result");
        event2Game1.setEventTitle(event2Title);
        event2Game1.setResultString("event2game1result");
        event2Game2.setEventTitle(event2Title);
        event2Game2.setResultString("event2game2result");

        final Event event1 = new Event();
        event1.setTitle(event1Title);
        event1.setName(event1Name);

        final Event event2 = new Event();
        event2.setTitle(event2Title);
        event2.setName(event2Name);
        when(mockEventRepository.findByTournamentId(tournamentId)).thenReturn(Arrays.asList(event1, event2));

        when(mockEventManager.createTournamentResultList(event1Name)).thenReturn(
                new TournamentResultRequestLineItem[] { event1Game1, event1Game2 });
        when(mockEventManager.createTournamentResultList(event2Name)).thenReturn(
                new TournamentResultRequestLineItem[] { event2Game1, event2Game2 });

        final Tournament tournament = new Tournament();
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
