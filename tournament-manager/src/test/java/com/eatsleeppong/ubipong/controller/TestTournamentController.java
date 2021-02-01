package com.eatsleeppong.ubipong.controller;

import com.eatsleeppong.ubipong.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.entity.SpringJpaTournament;
import com.eatsleeppong.ubipong.model.challonge.*;
import com.eatsleeppong.ubipong.tournamentmanager.TestHelper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Game;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Match;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Player;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Tournament;
import com.eatsleeppong.ubipong.tournamentmanager.domain.TournamentResult;
import com.eatsleeppong.ubipong.tournamentmanager.dto.EventDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.request.TournamentRequest;
import com.eatsleeppong.ubipong.tournamentmanager.dto.response.TournamentResponse;
import com.eatsleeppong.ubipong.tournamentmanager.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class TestTournamentController {
    private final String tournamentContext = "/rest/v0/tournaments";


    private final String eventName = "Preliminary Group 1";
    private final String challongeUrl = "bb_201906_pg_rr_1";
    private final Integer matchId = 10101;
    private final Integer player1Id = 1;
    private final Integer player2Id = 2;
    private final String player1Name = "spongebob";
    private final String player2Name = "patrick";

    private final Player spongebob = TestHelper.createPlayerSpongebob();
    private final Player patrick = TestHelper.createPlayerPatrick();
    private final Player squidward = TestHelper.createPlayerSquidward();

    final TournamentRequest tournamentRequest = TournamentRequest.builder()
        .name("Eat Sleep Pong Open 2020")
        .tournamentDate("2020-12-31")
        .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    // this is just a helper to set up tests
    @Autowired
    private SpringJpaTournamentRepository springJpaTournamentRepository;

    // this is just a helper to set up tests
    @Autowired
    private EventRepositoryImpl eventRepositoryImpl;

    @MockBean
    private ChallongeTournamentRepository mockChallongeTournamentRepository;

    @MockBean
    private ChallongeParticipantRepository mockChallongeParticipantRepository;

    @MockBean
    private ChallongeMatchRepository mockChallongeMatchRepository;

    @Autowired
    private MockMvc mockMvc;

    private SpringJpaTournament addTournament(final Tournament tournament) throws Exception {
        final SpringJpaTournament springJpaTournament = new SpringJpaTournament();
        springJpaTournament.setName(tournament.getName());
        springJpaTournament.setTournamentDate(Date.from(tournament.getTournamentDate()));

        return springJpaTournamentRepository.save(springJpaTournament);
    }

    private Event createEvent(final Integer tournamentId) {
        return Event.builder()
            .name(eventName)
            .challongeUrl(challongeUrl)
            .tournamentId(tournamentId)
            .build();
    }

    private List<Player> createPlayerList() {
        final Player p1 = Player.builder()
            .id(player1Id)
            .name(player1Name)
            .build();

        final Player p2 = Player.builder()
            .id(player2Id)
            .name(player2Name)
            .build();

        return List.of(p1, p2);
    }

    //                A     B
    // A  player1          11-9
    // B  player2    9-11
    private List<Match> createMatchList() {
        return List.of(Match.builder()
            .id(matchId)
            .player1Id(player1Id)
            .player2Id(player2Id)
            .winnerId(player1Id)
            .status(Match.STATUS_COMPLETE)
            .gameList(List.of(
                Game.builder().player1Score(11).player2Score(9).status(Game.STATUS_COMPLETE).build()
            ))
            .build()
        );
    }

    @BeforeEach
    public void setupMocks() {
        when(mockChallongeMatchRepository.findByChallongeUrl(TestHelper.CHALLONGE_URL))
            .thenReturn(List.of(TestHelper.createMatch1(), TestHelper.createMatch2()));

        when(mockChallongeParticipantRepository.findByChallongeUrl(TestHelper.CHALLONGE_URL))
            .thenReturn(List.of(
                TestHelper.createPlayerPatrick(),
                TestHelper.createPlayerSpongebob(),
                TestHelper.createPlayerSquidward()));
    }

    @Test
    @DisplayName("should generate tournament result report")
    public void testTournamentResultList() throws Exception {
        final Tournament tournament = TestHelper.createTournament();
        final SpringJpaTournament springJpaTournament = addTournament(tournament);

        final Integer tournamentId = springJpaTournament.getId();
        final Event event = TestHelper.createEvent().withTournamentId(tournamentId);
        eventRepositoryImpl.save(event);

        final UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .path(tournamentContext)
            .path("/{tournamentId}/result")
            .build();
        final Map<String, String> uriMap = Stream.of(new String[][] {
            { "tournamentId", springJpaTournament.getId().toString() },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        mockMvc.perform(
            get(uriComponents.expand(uriMap).toUri())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tournamentName").value(is(springJpaTournament.getName())))
            .andExpect(jsonPath("$.tournamentDate").value(is(tournament.getTournamentDate().toString())))
            .andExpect(jsonPath("$.tournamentResultList[0].winner").value(is(patrick.getName())))
            .andExpect(jsonPath("$.tournamentResultList[0].loser").value(is(spongebob.getName())))
            .andExpect(jsonPath("$.tournamentResultList[0].eventName").value(is(event.getName())))
            .andExpect(jsonPath("$.tournamentResultList[0].resultString").value(is("3 5 1"))) // 3 5 1 because patrick won 11-3 11-5 11-1
            .andExpect(jsonPath("$.tournamentResultList[1].winner").value(is(spongebob.getName())))
            .andExpect(jsonPath("$.tournamentResultList[1].loser").value(is(squidward.getName())))
            .andExpect(jsonPath("$.tournamentResultList[1].eventName").value(is(event.getName())))
            .andExpect(jsonPath("$.tournamentResultList[1].resultString").value(is("11 -5 9 9")));
    }
    
    @Test
    @DisplayName("should generate tournament result report for USATT")
    public void testUsattTournamentResultList() throws Exception {
        final SpringJpaTournament springJpaTournament = addTournament(TestHelper.createTournament());

        final Integer tournamentId = springJpaTournament.getId();
        final Event event = TestHelper.createEvent().withTournamentId(tournamentId);
        eventRepositoryImpl.save(event);

        final UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .path(tournamentContext)
            .path("/{tournamentId}/usatt-result")
            .build();
        final  Map<String, String> uriMap = Stream.of(new String[][] {
            { "tournamentId", springJpaTournament.getId().toString() },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        final MvcResult mvcResult = mockMvc.perform(
            get(uriComponents.expand(uriMap).toUri())
                .accept("*/*"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith("text/csv"))
            .andReturn();

        final String responseContent = mvcResult.getResponse().getContentAsString();

        final String[] matchResult = responseContent.split("\n");

        assertThat(matchResult[0].trim(), is("id?,patrick,spongebob,\"3,5,1\",Preliminary Group 1"));
        assertThat(matchResult[1].trim(), is("id?,spongebob,squidward,\"11,-5,9,9\",Preliminary Group 1"));
    }
}
