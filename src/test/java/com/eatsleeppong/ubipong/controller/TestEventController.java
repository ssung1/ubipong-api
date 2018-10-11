package com.eatsleeppong.ubipong.controller;

import com.eatsleeppong.ubipong.model.challonge.ChallongeMatch;
import com.eatsleeppong.ubipong.model.challonge.ChallongeMatchWrapper;
import com.eatsleeppong.ubipong.model.challonge.ChallongeParticipant;
import com.eatsleeppong.ubipong.model.challonge.ChallongeParticipantWrapper;
import com.eatsleeppong.ubipong.repo.ChallongeMatchRepository;
import com.eatsleeppong.ubipong.repo.ChallongeParticipantRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TestEventController {
    final String eventId = "bikiniBottomOpen-RoundRobin-Group-3";
    final Integer player1Id = 1;
    final Integer player2Id = 2;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ChallongeMatchRepository mockMatchRepository;

    @MockBean
    ChallongeParticipantRepository mockParticipantRepository;

    @Before
    public void setupMocks() {
        //                A     B
        // A  player1          11-9
        // B  player2    9-11
        ChallongeMatch m = new ChallongeMatch();
        m.setPlayer1Id(player1Id);
        m.setPlayer2Id(player2Id);
        m.setWinnerId(player1Id);
        m.setScoresCsv("11-9");
        m.setState(ChallongeMatch.STATE_COMPLETE);

        ChallongeMatchWrapper mw = new ChallongeMatchWrapper();
        mw.setMatch(m);

        when(mockMatchRepository.getMatchList(eventId)).thenReturn(
            new ChallongeMatchWrapper[] { mw } );

        ChallongeParticipant p1 = new ChallongeParticipant();
        p1.setId(player1Id);
        p1.setDisplayName("player1");
        ChallongeParticipant p2 = new ChallongeParticipant();
        p2.setId(player2Id);
        p2.setDisplayName("player2");

        when(mockParticipantRepository.getParticipantList(eventId))
            .thenReturn(Stream.of(p1, p2).map(p -> {
                ChallongeParticipantWrapper pw =
                    new ChallongeParticipantWrapper();
                pw.setParticipant(p);
                return pw; })
                .toArray(ChallongeParticipantWrapper[]::new));
    }

    @Test
    public void testGetRoundRobinGrid() throws Exception {
        mockMvc.perform(
            get("/rest/v0/event/" + eventId + "/roundRobinGrid")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON))
            .andExpect(
                jsonPath("$").isArray())
            .andDo(print())
            .andExpect(
                jsonPath("[0]").isArray())
            .andExpect(
                jsonPath("[0][2].content").value(is("A")))
            .andExpect(
                jsonPath("[1][3].gameList[0].player1Score").value(is(11)));
    }
}
