package com.eatsleeppong.ubipong.controller;

import com.eatsleeppong.ubipong.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.entity.SpringJpaTournament;
import com.eatsleeppong.ubipong.model.challonge.*;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import com.eatsleeppong.ubipong.tournamentmanager.dto.EventDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.request.TournamentRequest;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class TestTournamentController {
    final String eventName = "Preliminary Group 1";
    final String challongeUrl = "bb_201906_pg_rr_1";
    final String tournamentContext = "/rest/v0/tournaments";
    final Integer matchId = 10101;
    final Integer player1Id = 1;
    final Integer player2Id = 2;
    final String player1Name = "spongebob";
    final String player2Name = "patrick";

    final TournamentRequest tournamentRequest = TournamentRequest.builder()
        .name("Eat Sleep Pong Open 2020")
        .tournamentDate("2020-12-31")
        .build();

    private ObjectMapper objectMapper = new ObjectMapper();

    // this is just a helper to set up tests
    @Autowired
    SpringJpaTournamentRepository springJpaTournamentRepository;

    // this is just a helper to set up tests
    @Autowired
    EventRepositoryImpl eventRepositoryImpl;

    @MockBean
    ChallongeTournamentRepository mockChallongeTournamentRepository;

    @MockBean
    ChallongeParticipantRepository mockChallongeParticipantRepository;

    @MockBean
    ChallongeMatchRepository mockChallongeMatchRepository;

    @Autowired
    MockMvc mockMvc;

    private SpringJpaTournament createTournament() throws Exception {
        final SpringJpaTournament springJpaTournament = new SpringJpaTournament();
        springJpaTournament.setName("Bikini Bottom Open 2019");
        springJpaTournament.setTournamentDate(Date.from(Instant.parse("2019-06-23T00:00:00Z")));

        return springJpaTournament;
    }

    private Event createEvent(final Integer tournamentId) {
        return Event.builder()
            .name(eventName)
            .challongeUrl(challongeUrl)
            .tournamentId(tournamentId)
            .build();
    }

    private ChallongeTournamentWrapper createChallongeTournamentWrapper() {
        ChallongeTournamentWrapper tw1 = new ChallongeTournamentWrapper();
        tw1.setTournament(createChallongeTournament());

        return tw1;
    }

    private ChallongeTournament createChallongeTournament() {
        ChallongeTournament t1 = new ChallongeTournament();
        t1.setName(eventName);
        t1.setDescription("an event is called a tournament on challonge.com");
        t1.setUrl(challongeUrl);

        return t1;
    }

    @BeforeEach
    public void setupMocks() {
        when(mockChallongeTournamentRepository.createTournament(any())).thenReturn(createChallongeTournamentWrapper());
        when(mockChallongeTournamentRepository.getTournament(any())).thenReturn(createChallongeTournamentWrapper());

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

        when(mockChallongeMatchRepository.getMatchList(challongeUrl)).thenReturn(
            new ChallongeMatchWrapper[] { mw } );

        ChallongeParticipant p1 = new ChallongeParticipant();
        p1.setId(player1Id);
        p1.setDisplayName(player1Name);
        ChallongeParticipant p2 = new ChallongeParticipant();
        p2.setId(player2Id);
        p2.setDisplayName(player2Name);

        when(mockChallongeParticipantRepository.getParticipantList(challongeUrl))
            .thenReturn(Stream.of(p1, p2).map(p -> {
                ChallongeParticipantWrapper pw =
                    new ChallongeParticipantWrapper();
                pw.setParticipant(p);
                return pw; })
                .toArray(ChallongeParticipantWrapper[]::new));
    }

    @Test
    @DisplayName("Create a tournament")
    @Disabled("This is part of using domain -- we are not sure if it is worth the effort")
    public void testAddTournament() throws Exception {
        mockMvc.perform(
            post(tournamentContext)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tournamentRequest)))
            .andExpect(status().is(HttpStatus.CREATED.value()))
            .andExpect(jsonPath("id").exists());
    }

    @Test
    @DisplayName("should generate the event result report")
    public void testTournamentResultList() throws Exception {
        final SpringJpaTournament springJpaTournamentToAdd = createTournament();
        final SpringJpaTournament addedSpringJpaTournament = springJpaTournamentRepository.save(springJpaTournamentToAdd);

        final Integer tournamentId = addedSpringJpaTournament.getId();
        final Event eventToAdd = createEvent(tournamentId);
        final Event addedEvent = eventRepositoryImpl.save(eventToAdd);

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .path(tournamentContext)
            .path("/{tournamentId}/result")
            .build();
        Map<String, String> uriMap = Stream.of(new String[][] {
            { "tournamentId", addedSpringJpaTournament.getId().toString() },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        mockMvc.perform(
            get(uriComponents.expand(uriMap).toUri())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tournamentName").value(is(springJpaTournamentToAdd.getName())))
            .andExpect(jsonPath("$.tournamentDate").value(containsString("2019-06-23")))
            .andExpect(jsonPath("$.tournamentResultList[0].winner").value(is(player1Name)))
            .andExpect(jsonPath("$.tournamentResultList[0].loser").value(is(player2Name)))
            .andExpect(jsonPath("$.tournamentResultList[0].eventName").value(is(eventName)))
            .andExpect(jsonPath("$.tournamentResultList[0].resultString").value(is("9"))); // 9 because player1 won 11-9
    }
}
