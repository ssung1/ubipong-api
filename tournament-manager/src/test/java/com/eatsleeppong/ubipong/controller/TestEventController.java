package com.eatsleeppong.ubipong.controller;

import com.eatsleeppong.ubipong.tournamentmanager.domain.Player;
import com.eatsleeppong.ubipong.tournamentmanager.dto.EventDto;
import com.eatsleeppong.ubipong.tournamentmanager.TestHelper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Game;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Match;
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

import java.util.List;
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
    private final String challongeUrl = "bb_201906_pg_rr_1";
    private final String eventName = "Preliminary Group 1";

    private final Integer matchId1 = 101;
    private final Integer matchId2 = 102;
    private final Integer matchId3 = 103;

    private final Integer spongebobId = 123;
    private final Integer patrickId = 234;
    private final Integer squidwardId = 345;

    private final String spongebobName = "spongebob";
    private final String patrickName = "patrick";
    private final String squidwardName = "squidward";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ChallongeMatchRepository mockChallongeMatchRepository;

    @MockBean
    ChallongeParticipantRepository mockChallongeParticipantRepository;

    @MockBean
    ChallongeTournamentRepository mockChallongeTournamentRepository;

    private EventDto createEvent() {
        return EventDto.builder()
            .challongeUrl(challongeUrl)
            .name(eventName)
            .tournamentId(1)
            .build();
    }

    private List<Player> createPlayerList() {
        final Player spongebob = Player.builder()
            .id(spongebobId)
            .name(spongebobName)
            .build();

        final Player patrick = Player.builder()
            .id(patrickId)
            .name(patrickName)
            .build();

        final Player squidward = Player.builder()
            .id(squidwardId)
            .name(squidwardName)
            .build();

        return List.of(spongebob, patrick, squidward);
    }

    /**
     * <pre>
     *                   A           B           C                 Place
     *  A spongebob                  W 4 5 6
     *  B patrick        L -4 -5 -6              L -9 8 -6 -5
     *  C squidward                  W 9 -8 6 5
     *
     * </pre>
     */
    private List<Match> createMatchList() {
        Integer complete = Game.STATUS_COMPLETE;
        Game.GameBuilder gameBuilder = Game.builder();
        final Match m1 = Match.builder()
            .id(matchId1)
            .player1Id(spongebobId)
            .player2Id(patrickId)
            .status(Match.STATUS_COMPLETE)
            .winnerId(spongebobId)
            .gameList(List.of(
                gameBuilder.player1Score(11).player2Score(4).status(complete).build(),
                gameBuilder.player1Score(11).player2Score(5).status(complete).build(),
                gameBuilder.player1Score(11).player2Score(6).status(complete).build()
            ))
            .build();
        final Match m2 = Match.builder()
            .id(matchId2)
            .player1Id(patrickId)
            .player2Id(squidwardId)
            .status(Match.STATUS_COMPLETE)
            .winnerId(squidwardId)
            .gameList(List.of(
                gameBuilder.player1Score(9).player2Score(11).status(complete).build(),
                gameBuilder.player1Score(11).player2Score(8).status(complete).build(),
                gameBuilder.player1Score(6).player2Score(11).status(complete).build(),
                gameBuilder.player1Score(5).player2Score(11).status(complete).build()
            ))
            .build();
        final Match m3 = Match.builder()
            .id(matchId3)
            .player1Id(spongebobId)
            .player2Id(squidwardId)
            .status(Match.STATUS_INCOMPLETE)
            .build();

        return List.of(m1, m2, m3);
    }

    private SpringJpaEvent addEvent(EventDto event) throws Exception {
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

    @BeforeEach
    public void setupMocks() {
        when(mockChallongeMatchRepository.findByChallongeUrl(challongeUrl))
            .thenReturn(createMatchList());
        when(mockChallongeParticipantRepository.findByChallongeUrl(challongeUrl))
            .thenReturn(createPlayerList());
        when(mockChallongeTournamentRepository.getTournament(challongeUrl))
            .thenReturn(TestHelper.createChallongeTournamentWrapper());
    }

    @Test
    @DisplayName("should be able to get the bracket display for a round robin event")
    public void testGetRoundRobinGrid() throws Exception {
        addEvent(createEvent());
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
            .andExpect(jsonPath("[0][4].type").value(is(RoundRobinCell.TYPE_TEXT)))
            .andExpect(jsonPath("[0][4].content").value(is("C")))

            // second row is for scores, with A spongebob score score ...
            .andExpect(jsonPath("[1]").isArray())
            .andExpect(jsonPath("[1][0].type").value(is(RoundRobinCell.TYPE_TEXT)))
            .andExpect(jsonPath("[1][0].content").value(is("A")))
            .andExpect(jsonPath("[1][1].type").value(is(RoundRobinCell.TYPE_TEXT)))
            .andExpect(jsonPath("[1][1].content").value(is(spongebobName)))
            .andExpect(jsonPath("[1][2].type").value(is(RoundRobinCell.TYPE_EMPTY)))
            .andExpect(jsonPath("[1][2].content").value(is("")))
            .andExpect(jsonPath("[1][3].type").value(is(RoundRobinCell.TYPE_MATCH_COMPLETE)))
            .andExpect(jsonPath("[1][3].content").value(is("W 4 5 6")))
            .andExpect(
                jsonPath("[1][3].gameList[0].player1Score").value(is(11)))
            .andExpect(
                jsonPath("[1][3].gameList[0].player2Score").value(is(4)))
            .andExpect(
                jsonPath("[1][3].gameList[0].winForPlayer1").value(is(true)))
            .andExpect(jsonPath("[1][4].type").value(is(RoundRobinCell.TYPE_EMPTY)))
            .andExpect(jsonPath("[1][4].content").value(is("")))
    
            // third row is for scores, with B patrick score score ...
            .andExpect(jsonPath("[2]").isArray())
            .andExpect(jsonPath("[2][0].type").value(is(RoundRobinCell.TYPE_TEXT)))
            .andExpect(jsonPath("[2][0].content").value(is("B")))
            .andExpect(jsonPath("[2][1].type").value(is(RoundRobinCell.TYPE_TEXT)))
            .andExpect(jsonPath("[2][1].content").value(is(patrickName)))
            .andExpect(jsonPath("[2][2].type").value(is(RoundRobinCell.TYPE_MATCH_COMPLETE)))
            .andExpect(jsonPath("[2][2].content").value(is("L -4 -5 -6")))
            .andExpect(
                jsonPath("[2][2].gameList[0].player1Score").value(is(4)))
            .andExpect(
                jsonPath("[2][2].gameList[0].player2Score").value(is(11)))
            .andExpect(
                jsonPath("[2][2].gameList[0].winForPlayer1").value(is(false)))
            .andExpect(jsonPath("[2][3].type").value(is(RoundRobinCell.TYPE_EMPTY)))
            .andExpect(jsonPath("[2][3].content").value(is("")))
            .andExpect(jsonPath("[2][4].type").value(is(RoundRobinCell.TYPE_MATCH_COMPLETE)))
            .andExpect(jsonPath("[2][4].content").value(is("L -9 8 -6 -5")))

            // fourth row is for scores, with C squidward score score ...
            .andExpect(jsonPath("[3]").isArray())
            .andExpect(jsonPath("[3][0].type").value(is(RoundRobinCell.TYPE_TEXT)))
            .andExpect(jsonPath("[3][0].content").value(is("C")))
            .andExpect(jsonPath("[3][1].type").value(is(RoundRobinCell.TYPE_TEXT)))
            .andExpect(jsonPath("[3][1].content").value(is(squidwardName)))
            .andExpect(jsonPath("[3][2].type").value(is(RoundRobinCell.TYPE_EMPTY)))
            .andExpect(jsonPath("[3][2].content").value(is("")))
            .andExpect(jsonPath("[3][3].type").value(is(RoundRobinCell.TYPE_MATCH_COMPLETE))) // this is incorrect: issue submitted
            .andExpect(jsonPath("[3][3].content").value(is("W 9 -8 6 5")))
            .andExpect(jsonPath("[3][4].type").value(is(RoundRobinCell.TYPE_EMPTY)))
            .andExpect(jsonPath("[3][4].content").value(is("")));
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
    @DisplayName("should be able to return list of matches for round robin")
    public void testRoundRobinMatchList() throws Exception {
        addEvent(createEvent());
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
            .andExpect(jsonPath("$[0].player1Id").value(is(spongebobId)))
            .andExpect(jsonPath("$[0].player1Name").value(is(spongebobName)))
            .andExpect(jsonPath("$[0].player1Seed").value(is("A")))
            .andExpect(jsonPath("$[0].player2Id").value(is(patrickId)))
            .andExpect(jsonPath("$[0].player2Name").value(is(patrickName)))
            .andExpect(jsonPath("$[0].player2Seed").value(is("B")))
            .andExpect(jsonPath("$[0].matchId").value(is(matchId1)))
            .andExpect(jsonPath("$[0].status").value(is(Match.STATUS_COMPLETE)))
            .andExpect(jsonPath("$[0].resultCode").value(is(Match.RESULT_CODE_WIN_BY_PLAYING)))

            .andExpect(jsonPath("$[1].player1Id").value(is(patrickId)))
            .andExpect(jsonPath("$[1].player1Name").value(is(patrickName)))
            .andExpect(jsonPath("$[1].player1Seed").value(is("B")))
            .andExpect(jsonPath("$[1].player2Id").value(is(squidwardId)))
            .andExpect(jsonPath("$[1].player2Name").value(is(squidwardName)))
            .andExpect(jsonPath("$[1].player2Seed").value(is("C")))
            .andExpect(jsonPath("$[1].matchId").value(is(matchId2)))
            .andExpect(jsonPath("$[1].status").value(is(Match.STATUS_COMPLETE)))
            .andExpect(jsonPath("$[1].resultCode").value(is(Match.RESULT_CODE_WIN_BY_PLAYING)))

            .andExpect(jsonPath("$[2].player1Id").value(is(spongebobId)))
            .andExpect(jsonPath("$[2].player1Name").value(is(spongebobName)))
            .andExpect(jsonPath("$[2].player1Seed").value(is("A")))
            .andExpect(jsonPath("$[2].player2Id").value(is(squidwardId)))
            .andExpect(jsonPath("$[2].player2Name").value(is(squidwardName)))
            .andExpect(jsonPath("$[2].player2Seed").value(is("C")))
            .andExpect(jsonPath("$[2].matchId").value(is(matchId3)))
            .andExpect(jsonPath("$[2].status").value(is(Match.STATUS_INCOMPLETE)))
            .andExpect(jsonPath("$[2].resultCode").value(nullValue()));
    }

    @Test
    @DisplayName("should generate event result report")
    public void testEventResultList() throws Exception {
        // for this test, we cannot have any pending matches
        when(mockChallongeMatchRepository.findByChallongeUrl(challongeUrl))
            .thenReturn(createMatchList().subList(0, 2));
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
            .andExpect(jsonPath("$[0].winner").value(is(spongebobName)))
            .andExpect(jsonPath("$[0].loser").value(is(patrickName)))
            .andExpect(jsonPath("$[0].eventName").value(is(eventName)))
            .andExpect(jsonPath("$[0].resultString").value(is("4 5 6"))) // because patrick won 11-4 11-5 11-6 

            .andExpect(jsonPath("$[1].winner").value(is(squidwardName)))
            .andExpect(jsonPath("$[1].loser").value(is(patrickName)))
            .andExpect(jsonPath("$[1].eventName").value(is(eventName)))
            .andExpect(jsonPath("$[1].resultString").value(is("9 -8 6 5")));
    }
}
