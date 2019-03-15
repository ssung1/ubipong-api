package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.entity.Event;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequest;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequestLineItem;
import com.eatsleeppong.ubipong.repo.EventRepository;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private final String eventName1 = "pr_gr_1";
    private final String eventName2 = "ca";
    private final String eventTitle1 = "Preliminary Group 1";
    private final String eventTitle2 = "Class A";

    private final TournamentResultRequestLineItem event1Game1 = new TournamentResultRequestLineItem();
    private final TournamentResultRequestLineItem event1Game2 = new TournamentResultRequestLineItem();
    private final TournamentResultRequestLineItem event2Game1 = new TournamentResultRequestLineItem();
    private final TournamentResultRequestLineItem event2Game2 = new TournamentResultRequestLineItem();

    private final EventRepository mockEventRepository = mock(EventRepository.class);

    private final TournamentManager tournamentManager = new TournamentManager();

    @Before
    public void setupMocks() {
        // because Lombok creates the equals method, each TournamentResultRequestLineItem needs to have different
        // content in order to be considered different (cannot use referential equality)
        event1Game1.setEventTitle(eventTitle1);
        event1Game1.setResultString("event1game1result");
        event1Game2.setEventTitle(eventTitle1);
        event1Game2.setResultString("event1game2result");
        event2Game1.setEventTitle(eventTitle2);
        event2Game1.setResultString("event2game1result");
        event2Game2.setEventTitle(eventTitle2);
        event2Game2.setResultString("event2game2result");

        final Event event1 = new Event();
        event1.setTitle(eventTitle1);
        event1.setName(eventName1);

        final Event event2 = new Event();
        event2.setTitle(eventTitle2);
        event2.setName(eventName2);
        when(mockEventRepository.findByTournamentId(tournamentId)).thenReturn(Arrays.asList(event1, event2));
    }

    @Test
    public void testCreateTournamentResultRequest() throws Exception {
        final TournamentResultRequest tournamentResultRequest =
                tournamentManager.createTournamentResultRequest(tournamentId);
        assertThat(tournamentResultRequest.getTournamentName(), is(tournamentName));
        assertThat(tournamentResultRequest.getTournamentDate(), is(df.parse(tournamentDate)));
        assertThat(tournamentResultRequest.getTournamentResultList(), arrayWithSize(12));
        
        final TournamentResultRequestLineItem[] tournamentResultRequestLineItem = 
                tournamentResultRequest.getTournamentResultList();

        assertThat(tournamentResultRequestLineItem, hasItemInArray(event1Game1));
        assertThat(tournamentResultRequestLineItem, hasItemInArray(event1Game2));
        assertThat(tournamentResultRequestLineItem, hasItemInArray(event2Game1));
        assertThat(tournamentResultRequestLineItem, hasItemInArray(event2Game2));
        // if the above works, we don't need the stuff below
//
//        final List<String> eventTitleList = Arrays.stream(tournamentResultRequestLineItem)
//                .map(TournamentResultRequestLineItem::getEventTitle)
//                .collect(Collectors.toList());
//
//        assertThat(eventTitleList, hasItem(eventTitle1));
//        assertThat(eventTitleList, hasItem(eventTitle2));
//
//        final List<String> resultStringList = Arrays.stream(tournamentResultRequestLineItem)
//                .map(TournamentResultRequestLineItem::getResultString)
//                .collect(Collectors.toList());
//
//        assertThat(resultStringList, hasItem(eventTitle1));
//        assertThat(resultStringList, hasItem(eventTitle2));
    }
}
