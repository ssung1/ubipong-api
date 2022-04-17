package com.eatsleeppong.ubipong.tournamentmanager.controller;

import com.eatsleeppong.ubipong.tournamentmanager.domain.Player;
import com.eatsleeppong.ubipong.tournamentmanager.dto.EventDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.EventStatusDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.RoundRobinCellTypeDto;
import com.eatsleeppong.ubipong.tournamentmanager.TestHelper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Match;
import com.eatsleeppong.ubipong.tournamentmanager.repository.ChallongeMatchRepository;
import com.eatsleeppong.ubipong.tournamentmanager.repository.ChallongeParticipantRepository;
import com.eatsleeppong.ubipong.tournamentmanager.repository.ChallongeTournamentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@WithMockUser
public class TestEventController {
    private final String challongeUrl = TestHelper.CHALLONGE_URL;
    private final String eventName = TestHelper.EVENT_NAME;

    private final Player spongebob = TestHelper.createPlayerSpongebob();
    private final Player patrick = TestHelper.createPlayerPatrick();
    private final Player squidward = TestHelper.createPlayerSquidward();
    
    private final Match spongebobVsPatrick = TestHelper.createMatch1();
    private final Match spongebobVsSquidward = TestHelper.createMatch2();
    private final Match patrickVsSquidward = TestHelper.createMatch3();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ChallongeMatchRepository mockChallongeMatchRepository;

    @MockBean
    ChallongeParticipantRepository mockChallongeParticipantRepository;

    @MockBean
    ChallongeTournamentRepository mockChallongeTournamentRepository;

    final ObjectMapper objectMapper = new ObjectMapper();

    private EventDto addEvent(EventDto event) throws Exception {
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
        return objectMapper.readValue(responseContent, EventDto.class);
    }

    @BeforeEach
    public void setupObjectMapper() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void setupMocks() {
        when(mockChallongeMatchRepository.findByChallongeUrl(challongeUrl))
            .thenReturn(List.of(spongebobVsPatrick, spongebobVsSquidward, patrickVsSquidward));
        when(mockChallongeParticipantRepository.findByChallongeUrl(challongeUrl))
            .thenReturn(List.of(spongebob, patrick, squidward));
        when(mockChallongeTournamentRepository.getTournament(challongeUrl))
            .thenReturn(TestHelper.createChallongeTournamentWrapper());
    }

    @Test
    @DisplayName("should be able to get the bracket display for a round robin event")
    public void testGetRoundRobinGrid() throws Exception {
        //                           A              B              C
        //   A  spongebob                        L -3 -5 -1    W 11 -5 9 9
        //   B  patrick          W 3 5 1
        //   C  squidward        L -11 5 -9 -9
        addEvent(TestHelper.createEventDto());
        mockMvc.perform(
            get("/rest/v0/events/" + challongeUrl + "/roundRobinGrid")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            // multiple rows
            .andExpect(jsonPath("$").isArray())

            // first row is header, with (blank) (blank) A B C ...
            .andExpect(jsonPath("[0]").isArray())
            .andExpect(jsonPath("[0][0].type").value(is(RoundRobinCellTypeDto.EMPTY.getValue())))
            .andExpect(jsonPath("[0][0].content").value(is("")))
            .andExpect(jsonPath("[0][1].type").value(is(RoundRobinCellTypeDto.EMPTY.getValue())))
            .andExpect(jsonPath("[0][1].content").value(is("")))
            .andExpect(jsonPath("[0][2].type").value(is(RoundRobinCellTypeDto.TEXT.getValue())))
            .andExpect(jsonPath("[0][2].content").value(is("A")))
            .andExpect(jsonPath("[0][3].type").value(is(RoundRobinCellTypeDto.TEXT.getValue())))
            .andExpect(jsonPath("[0][3].content").value(is("B")))
            .andExpect(jsonPath("[0][4].type").value(is(RoundRobinCellTypeDto.TEXT.getValue())))
            .andExpect(jsonPath("[0][4].content").value(is("C")))

            // second row is for scores, with A spongebob score score ...
            .andExpect(jsonPath("[1]").isArray())
            .andExpect(jsonPath("[1][0].type").value(is(RoundRobinCellTypeDto.TEXT.getValue())))
            .andExpect(jsonPath("[1][0].content").value(is("A")))
            .andExpect(jsonPath("[1][1].type").value(is(RoundRobinCellTypeDto.NAME.getValue())))
            .andExpect(jsonPath("[1][1].content").value(is(spongebob.getName())))
            .andExpect(jsonPath("[1][2].type").value(is(RoundRobinCellTypeDto.EMPTY.getValue())))
            .andExpect(jsonPath("[1][2].content").value(is("")))
            .andExpect(jsonPath("[1][3].type").value(is(RoundRobinCellTypeDto.MATCH_COMPLETE.getValue())))
            .andExpect(jsonPath("[1][3].content").value(is("L -3 -5 -1")))
            .andExpect(jsonPath("[1][3].gameList[0].player1Score").value(is(3)))
            .andExpect(jsonPath("[1][3].gameList[0].player2Score").value(is(11)))
            .andExpect(jsonPath("[1][3].gameList[0].winForPlayer1").value(is(false)))
            .andExpect(jsonPath("[1][4].type").value(is(RoundRobinCellTypeDto.MATCH_COMPLETE.getValue())))
            .andExpect(jsonPath("[1][4].content").value(is("W 11 -5 9 9")))
            .andExpect(jsonPath("[1][4].gameList[0].player1Score").value(is(13)))
            .andExpect(jsonPath("[1][4].gameList[0].player2Score").value(is(11)))
            .andExpect(jsonPath("[1][4].gameList[0].winForPlayer1").value(is(true)))
    
            // third row is for scores, with B patrick score score ...
            .andExpect(jsonPath("[2]").isArray())
            .andExpect(jsonPath("[2][0].type").value(is(RoundRobinCellTypeDto.TEXT.getValue())))
            .andExpect(jsonPath("[2][0].content").value(is("B")))
            .andExpect(jsonPath("[2][1].type").value(is(RoundRobinCellTypeDto.NAME.getValue())))
            .andExpect(jsonPath("[2][1].content").value(is(patrick.getName())))
            .andExpect(jsonPath("[2][2].type").value(is(RoundRobinCellTypeDto.MATCH_COMPLETE.getValue())))
            .andExpect(jsonPath("[2][2].content").value(is("W 3 5 1")))
            .andExpect(jsonPath("[2][2].gameList[0].player1Score").value(is(11)))
            .andExpect(jsonPath("[2][2].gameList[0].player2Score").value(is(3)))
            .andExpect(jsonPath("[2][2].gameList[0].winForPlayer1").value(is(true)))
            .andExpect(jsonPath("[2][3].type").value(is(RoundRobinCellTypeDto.EMPTY.getValue())))
            .andExpect(jsonPath("[2][3].content").value(is("")))
            .andExpect(jsonPath("[2][4].type").value(is(RoundRobinCellTypeDto.MATCH_INCOMPLETE.getValue())))
            .andExpect(jsonPath("[2][4].content").value(is("")))

            // fourth row is for scores, with C squidward score score ...
            .andExpect(jsonPath("[3]").isArray())
            .andExpect(jsonPath("[3][0].type").value(is(RoundRobinCellTypeDto.TEXT.getValue())))
            .andExpect(jsonPath("[3][0].content").value(is("C")))
            .andExpect(jsonPath("[3][1].type").value(is(RoundRobinCellTypeDto.NAME.getValue())))
            .andExpect(jsonPath("[3][1].content").value(is(squidward.getName())))
            .andExpect(jsonPath("[3][2].type").value(is(RoundRobinCellTypeDto.MATCH_COMPLETE.getValue())))
            .andExpect(jsonPath("[3][2].content").value(is("L -11 5 -9 -9")))
            .andExpect(jsonPath("[3][3].type").value(is(RoundRobinCellTypeDto.MATCH_INCOMPLETE.getValue())))
            .andExpect(jsonPath("[3][3].content").value(is("")))
            .andExpect(jsonPath("[3][4].type").value(is(RoundRobinCellTypeDto.EMPTY.getValue())))
            .andExpect(jsonPath("[3][4].content").value(is("")));
    }

    @Test
    @DisplayName("should be able to get an existing event by ID")
    public void testGetEvent() throws Exception {
        final EventDto event = TestHelper.createEventDto();
        final EventDto addedEvent = addEvent(event);

        mockMvc.perform(
            get("/rest/v0/events/" + addedEvent.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("id").value(is(addedEvent.getId())))
            .andExpect(jsonPath("name").value(is(eventName)))
            .andExpect(jsonPath("challongeUrl").value(is(challongeUrl)))
            .andExpect(jsonPath("status").value(is(EventStatusDto.STARTED.getValue())));
    }

    @Test
    @DisplayName("should be able to add an event")
    public void testAddEvent() throws Exception {
        final EventDto event = TestHelper.createEventDto();
        final EventDto addedEvent = addEvent(event);

        assertThat(addedEvent.getId(), not(is(0)));
        assertThat(addedEvent.getName(), is(eventName));
        assertThat(addedEvent.getChallongeUrl(), is(challongeUrl));
        assertThat(addedEvent.getStatus(), is(EventStatusDto.CREATED));
    }

    @Test
    @DisplayName("should return client error if event name is too long")
    public void testAddEventClientErrorEventNameTooLong() throws Exception {
        final EventDto event = TestHelper.createEventDto().withName(
            "long long long long long long long long long long long long long long long long long long long long ");

        final ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(
            post("/rest/v0/events")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("should be able to return list of matche sheets for round robin")
    public void testRoundRobinMatchSheetList() throws Exception {
        addEvent(TestHelper.createEventDto());
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
            .andExpect(jsonPath("$[0].player1Id").value(is(spongebob.getId())))
            .andExpect(jsonPath("$[0].player1UsattNumber").value(is(spongebob.getUsattNumber())))
            .andExpect(jsonPath("$[0].player1Name").value(is(spongebob.getName())))
            .andExpect(jsonPath("$[0].player1SeedAsAlphabet").value(is("A")))
            .andExpect(jsonPath("$[0].player2Id").value(is(patrick.getId())))
            .andExpect(jsonPath("$[0].player2UsattNumber").value(is(patrick.getUsattNumber())))
            .andExpect(jsonPath("$[0].player2Name").value(is(patrick.getName())))
            .andExpect(jsonPath("$[0].player2SeedAsAlphabet").value(is("B")))
            .andExpect(jsonPath("$[0].matchId").value(is(spongebobVsPatrick.getId())))

            .andExpect(jsonPath("$[1].player1Id").value(is(spongebob.getId())))
            .andExpect(jsonPath("$[1].player1UsattNumber").value(is(spongebob.getUsattNumber())))
            .andExpect(jsonPath("$[1].player1Name").value(is(spongebob.getName())))
            .andExpect(jsonPath("$[1].player1SeedAsAlphabet").value(is("A")))
            .andExpect(jsonPath("$[1].player2Id").value(is(squidward.getId())))
            .andExpect(jsonPath("$[1].player2UsattNumber").value(is(squidward.getUsattNumber())))
            .andExpect(jsonPath("$[1].player2Name").value(is(squidward.getName())))
            .andExpect(jsonPath("$[1].player2SeedAsAlphabet").value(is("C")))
            .andExpect(jsonPath("$[1].matchId").value(is(spongebobVsSquidward.getId())))

            .andExpect(jsonPath("$[2].player1Id").value(is(patrick.getId())))
            .andExpect(jsonPath("$[2].player1UsattNumber").value(is(patrick.getUsattNumber())))
            .andExpect(jsonPath("$[2].player1Name").value(is(patrick.getName())))
            .andExpect(jsonPath("$[2].player1SeedAsAlphabet").value(is("B")))
            .andExpect(jsonPath("$[2].player2Id").value(is(squidward.getId())))
            .andExpect(jsonPath("$[2].player2UsattNumber").value(is(squidward.getUsattNumber())))
            .andExpect(jsonPath("$[2].player2Name").value(is(squidward.getName())))
            .andExpect(jsonPath("$[2].player2SeedAsAlphabet").value(is("C")))
            .andExpect(jsonPath("$[2].matchId").value(is(spongebobVsSquidward.getId())));
    }

    @Test
    @DisplayName("should be able to return match sheets, skipping matches whose players are not yet identified")
    public void testRoundRobinMatchSheetListSkipMatchesWithUnidentifiedPlayers() throws Exception {
        final EventDto event = TestHelper.createEventDto();
        final Match matchWithNoPlayers = Match.builder()
            .player1Id(null)
            .player2Id(null)
            .build();
        when(mockChallongeMatchRepository.findByChallongeUrl(challongeUrl))
            .thenReturn(List.of(spongebobVsPatrick, matchWithNoPlayers));

        addEvent(event);
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
            .andExpect(jsonPath("$[0].eventName").value(is(event.getName())))
            .andExpect(jsonPath("$[0].matchId").value(is(spongebobVsPatrick.getId())))
            .andExpect(jsonPath("$[0].player1Id").value(is(spongebob.getId())))
            .andExpect(jsonPath("$[0].player1UsattNumber").value(is(spongebob.getUsattNumber())))
            .andExpect(jsonPath("$[0].player1Name").value(is(spongebob.getName())))
            .andExpect(jsonPath("$[0].player1SeedAsAlphabet").value(is("A")))
            .andExpect(jsonPath("$[0].player2Id").value(is(patrick.getId())))
            .andExpect(jsonPath("$[0].player2UsattNumber").value(is(patrick.getUsattNumber())))
            .andExpect(jsonPath("$[0].player2Name").value(is(patrick.getName())))
            .andExpect(jsonPath("$[0].player2SeedAsAlphabet").value(is("B")))
            .andExpect(jsonPath("$[0].matchId").value(is(spongebobVsPatrick.getId())));
    }

    @Test
    @DisplayName("should generate event result report")
    public void testEventResultList() throws Exception {
        // for this test, we cannot have any pending matches
        when(mockChallongeMatchRepository.findByChallongeUrl(challongeUrl))
            .thenReturn(List.of(spongebobVsPatrick, spongebobVsSquidward));
        addEvent(TestHelper.createEventDto());

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
            .andExpect(jsonPath("$[0].winner").value(is(patrick.getName())))
            .andExpect(jsonPath("$[0].loser").value(is(spongebob.getName())))
            .andExpect(jsonPath("$[0].eventName").value(is(eventName)))
            .andExpect(jsonPath("$[0].resultString").value(is("3 5 1"))) // because patrick won 11-3 11-5 11-1 

            .andExpect(jsonPath("$[1].winner").value(is(spongebob.getName())))
            .andExpect(jsonPath("$[1].loser").value(is(squidward.getName())))
            .andExpect(jsonPath("$[1].eventName").value(is(eventName)))
            .andExpect(jsonPath("$[1].resultString").value(is("11 -5 9 9")));
    }

    @Test
    @DisplayName("should find list of events of a given tournament ID")
    public void testGetEventListByTournamentId() throws Exception {
        final EventDto event = TestHelper.createEventDto();
        final EventDto addedEvent = addEvent(event);

        mockMvc.perform(
            get("/rest/v0/events/search/find-by-tournament-id")
                .queryParam("tournament-id", String.valueOf(addedEvent.getTournamentId()))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].id").value(is(addedEvent.getId())))
            .andExpect(jsonPath("[0].name").value(is(eventName)))
            .andExpect(jsonPath("[0].challongeUrl").value(is(challongeUrl)))
            .andExpect(jsonPath("[0].status").value(is(EventStatusDto.STARTED.getValue())));
    }
}
