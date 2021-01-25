package com.eatsleeppong.ubipong.controller;

import com.eatsleeppong.ubipong.tournamentmanager.dto.EventDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.response.Match;
import com.eatsleeppong.ubipong.model.challonge.*;
import com.eatsleeppong.ubipong.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.tournamentmanager.repository.ChallongeMatchRepository;
import com.eatsleeppong.ubipong.tournamentmanager.repository.ChallongeParticipantRepository;
import com.eatsleeppong.ubipong.tournamentmanager.repository.ChallongeTournamentRepository;
import com.eatsleeppong.ubipong.tournamentmanager.dto.response.RoundRobinCell;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class TestEventController {
    final String challongeUrl = "bikiniBottomOpen-RoundRobin-Group-3";
    final String eventName = "Round Robin Group 3";
    final Integer matchId = 10101;
    final Integer player1Id = 1;
    final Integer player2Id = 2;
    final String player1Name = "spongebob";
    final String player2Name = "patrick";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ChallongeMatchRepository mockMatchRepository;

    @MockBean
    ChallongeParticipantRepository mockParticipantRepository;

    @MockBean
    ChallongeTournamentRepository mockTournamentRepository;

    private ChallongeTournamentWrapper getTournamentWrapper1() {
        ChallongeTournament t1 = new ChallongeTournament();
        t1.setName(eventName);
        t1.setDescription("an event is called a tournament on challonge.com");
        t1.setUrl(challongeUrl);

        ChallongeTournamentWrapper tw1 = new ChallongeTournamentWrapper();
        tw1.setTournament(t1);

        return tw1;
    }

    private EventDto createEvent() {
        return EventDto.builder()
            .challongeUrl(challongeUrl)
            .name(eventName)
            .tournamentId(1)
            .build();
    }

    @BeforeEach
    public void setupMocks() {
        //                A     B
        // A  player1          11-9
        // B  player2    9-11
        ChallongeMatch m = new ChallongeMatch();
        m.setId(matchId);
        m.setPlayer1Id(player1Id);
        m.setPlayer2Id(player2Id);
        m.setWinnerId(player1Id);
        m.setScoresCsv("11-9");
        m.setState(ChallongeMatch.STATE_COMPLETE);

        ChallongeMatchWrapper mw = new ChallongeMatchWrapper();
        mw.setMatch(m);

        when(mockMatchRepository.getMatchList(challongeUrl)).thenReturn(
            new ChallongeMatchWrapper[] { mw } );

        ChallongeParticipant p1 = new ChallongeParticipant();
        p1.setId(player1Id);
        p1.setDisplayName(player1Name);
        ChallongeParticipant p2 = new ChallongeParticipant();
        p2.setId(player2Id);
        p2.setDisplayName(player2Name);

        when(mockParticipantRepository.getParticipantList(challongeUrl))
            .thenReturn(Stream.of(p1, p2).map(p -> {
                ChallongeParticipantWrapper pw =
                    new ChallongeParticipantWrapper();
                pw.setParticipant(p);
                return pw; })
                .toArray(ChallongeParticipantWrapper[]::new));

        ChallongeTournament t1 = new ChallongeTournament();
        t1.setName(eventName);

        when(mockTournamentRepository.getTournament(challongeUrl))
            .thenReturn(getTournamentWrapper1());
    }

    public SpringJpaEvent addEvent(EventDto event) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();

        final MvcResult mvcResult = mockMvc.perform(
            post("/rest/v0/events")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").value(not(is(0))))
            .andExpect(jsonPath("name").value(is(event.getName())))
            .andReturn();

        final String responseContent = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(responseContent, SpringJpaEvent.class);
    }

    @Test
    public void testGetRoundRobinGrid() throws Exception {
        mockMvc.perform(
            get("/rest/v0/events/" + challongeUrl + "/roundRobinGrid")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON))
            // multiple rows
            .andExpect(jsonPath("$").isArray())

            // first row is header, with (blank) (blank) A B C ...
            .andExpect(jsonPath("[0]").isArray())
            .andExpect(jsonPath("[0][0].type").value(is(RoundRobinCell.TYPE_EMPTY)))
            .andExpect(jsonPath("[0][0].content").value(is("")))
            .andExpect(jsonPath("[0][1].type").value(is(RoundRobinCell.TYPE_EMPTY)))
            .andExpect(jsonPath("[0][1].content").value(is("")))
            .andExpect(jsonPath("[0][2].type").value(is(RoundRobinCell.TYPE_TEXT)))
            .andExpect(jsonPath("[0][2].content").value(is("A")))
            .andExpect(jsonPath("[0][3].type").value(is(RoundRobinCell.TYPE_TEXT)))
            .andExpect(jsonPath("[0][3].content").value(is("B")))

            // second row is for scores, with A spongebob score score ...
            .andExpect(jsonPath("[1]").isArray())
            .andExpect(jsonPath("[1][0].type").value(is(RoundRobinCell.TYPE_TEXT)))
            .andExpect(jsonPath("[1][0].content").value(is("A")))
            .andExpect(jsonPath("[1][1].type").value(is(RoundRobinCell.TYPE_TEXT)))
            .andExpect(jsonPath("[1][1].content").value(is(player1Name)))
            .andExpect(jsonPath("[1][2].type").value(is(RoundRobinCell.TYPE_EMPTY)))
            .andExpect(jsonPath("[1][2].content").value(is("")))
            .andExpect(jsonPath("[1][3].type").value(is(RoundRobinCell.TYPE_MATCH_COMPLETE)))
            .andExpect(jsonPath("[1][3].content").value(is("W 9")))
            .andExpect(
                jsonPath("[1][3].gameList[0].player1Score").value(is(11)))
            .andExpect(
                jsonPath("[1][3].gameList[0].player2Score").value(is(9)))
            .andExpect(
                jsonPath("[1][3].gameList[0].winForPlayer1").value(is(true)))

            // second row is for scores, with B patrick score score ...
            .andExpect(jsonPath("[2]").isArray())
            .andExpect(jsonPath("[2][0].type").value(is(RoundRobinCell.TYPE_TEXT)))
            .andExpect(jsonPath("[2][0].content").value(is("B")))
            .andExpect(jsonPath("[2][1].type").value(is(RoundRobinCell.TYPE_TEXT)))
            .andExpect(jsonPath("[2][1].content").value(is(player2Name)))
            .andExpect(jsonPath("[2][2].type").value(is(RoundRobinCell.TYPE_EMPTY)))
            .andExpect(jsonPath("[2][2].content").value(is("L -9")))
            .andExpect(
                jsonPath("[2][2].gameList[0].player1Score").value(is(9)))
            .andExpect(
                jsonPath("[2][2].gameList[0].player2Score").value(is(11)))
            .andExpect(
                jsonPath("[2][2].gameList[0].winForPlayer1").value(is(false)))
            .andExpect(jsonPath("[2][3].type").value(is(RoundRobinCell.TYPE_EMPTY))) // this is incorrect: issue submitted
            .andExpect(jsonPath("[2][3].content").value(is("")));
    }

    @Test
    @DisplayName("should be able to get an existing event by ID")
    public void testGetEvent() throws Exception {
        final EventDto event = createEvent();
        final SpringJpaEvent addedEvent = addEvent(event);

        mockMvc.perform(
            get("/rest/v0/events/" + addedEvent.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("id").value(is(addedEvent.getId())))
            .andExpect(jsonPath("name").value(is(eventName)))
            .andExpect(jsonPath("challongeUrl").value(is(challongeUrl)));
    }

    @Test
    @DisplayName("should be able to add an event")
    public void testAddEvent() throws Exception {
        final EventDto event = createEvent();
        final SpringJpaEvent addedEvent = addEvent(event);

        assertThat(addedEvent.getId(), not(is(0)));
        assertThat(addedEvent.getName(), is(eventName));
        assertThat(addedEvent.getChallongeUrl(), is(challongeUrl));
    }

    @Test
    public void testRoundRobinMatchList() throws Exception {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .path("/rest/v0/events/{challongeUrl}/roundRobinMatchList")
            .build();
        Map<String, String> uriMap = Stream.of(new String[][] {
            { "challongeUrl", challongeUrl },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        mockMvc.perform(
            get(uriComponents.expand(uriMap).toUri())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].player1Id").value(is(player1Id)))
            .andExpect(jsonPath("$[0].player1Name").value(is(player1Name)))
            .andExpect(jsonPath("$[0].player1Seed").value(is("A")))
            .andExpect(jsonPath("$[0].player2Id").value(is(player2Id)))
            .andExpect(jsonPath("$[0].player2Name").value(is(player2Name)))
            .andExpect(jsonPath("$[0].player2Seed").value(is("B")))
            .andExpect(jsonPath("$[0].matchId").value(is(matchId)))
            .andExpect(jsonPath("$[0].status").value(is(Match.STATUS_COMPLETE)))
            .andExpect(jsonPath("$[0].resultCode").value(is(Match.RESULT_CODE_WIN_BY_PLAYING)));
    }

    @Test
    @DisplayName("should generate the event result report")
    public void testEventResultList() throws Exception {
        addEvent(createEvent());

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .path("/rest/v0/events/{challongeUrl}/result")
            .build();
        Map<String, String> uriMap = Stream.of(new String[][] {
            { "challongeUrl", challongeUrl },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        mockMvc.perform(
            get(uriComponents.expand(uriMap).toUri())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].winner").value(is(player1Name)))
            .andExpect(jsonPath("$[0].loser").value(is(player2Name)))
            .andExpect(jsonPath("$[0].eventName").value(is(eventName)))
            .andExpect(jsonPath("$[0].resultString").value(is("9"))); // 9 because player1 won 11-9 
    }
}
