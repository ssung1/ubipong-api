package com.eatsleeppong.ubipong.tournamentmanager.controller;

import com.eatsleeppong.ubipong.tournamentmanager.TestHelper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Player;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Tournament;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRole;
import com.eatsleeppong.ubipong.tournamentmanager.dto.RoleDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.TournamentDto;
import com.eatsleeppong.ubipong.tournamentmanager.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@WithMockUser
public class TestTournamentController {
    private final String tournamentContext = "/rest/v0/tournaments";
    // default user when using @WithMockUser
    private final String userId = "user";

    private final Player spongebob = TestHelper.createPlayerSpongebob();
    private final Player patrick = TestHelper.createPlayerPatrick();
    private final Player squidward = TestHelper.createPlayerSquidward();

    private final ObjectMapper objectMapper = new ObjectMapper();

    // this is just a helper to set up tests
    @Autowired
    private TournamentRepositoryImpl tournamentRepositoryImpl;

    // this is just a helper to set up tests
    @Autowired
    private EventRepositoryImpl eventRepositoryImpl;

    // this is just a helper to set up tests
    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @MockBean
    private ChallongeTournamentRepository mockChallongeTournamentRepository;

    @MockBean
    private ChallongeParticipantRepository mockChallongeParticipantRepository;

    @MockBean
    private ChallongeMatchRepository mockChallongeMatchRepository;

    @Autowired
    private MockMvc mockMvc;

    private Tournament addTournament(final Tournament tournament) throws Exception {
        final Set<UserRole> userRoleSet = Collections.singleton(TestHelper.createUserRole());
        return tournamentRepositoryImpl.save(tournament.withUserRoleSet(userRoleSet));
    }

    @BeforeEach
    public void setupObjectMapper() {
        userRepositoryImpl.save(TestHelper.createUser());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
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

        when(mockChallongeTournamentRepository.getTournament(TestHelper.CHALLONGE_URL))
            .thenReturn(TestHelper.createChallongeTournamentWrapper());
    }

    @Test
    @DisplayName("should be able to add a tournament")
    public void testAddTournament() throws Exception {
        final Tournament tournamentToAdd = TestHelper.createTournament().withId(null);

        final MvcResult mvcResult = mockMvc.perform(
            post(tournamentContext)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tournamentToAdd)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(notNullValue()))
            .andExpect(jsonPath("$.name").value(is(tournamentToAdd.getName())))
            .andExpect(jsonPath("$.tournamentDate").value(is(tournamentToAdd.getTournamentDate().toString())))
            .andReturn();

        final TournamentDto addedTournament = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
            TournamentDto.class);

        assertThat(addedTournament.getId(), notNullValue());
        assertThat(addedTournament.getName(), is(tournamentToAdd.getName()));
        assertThat(addedTournament.getTournamentDate(), is(tournamentToAdd.getTournamentDate()));
        assertThat(addedTournament.getUserRoleSet(), hasItem(hasProperty("role", is(RoleDto.TOURNAMENT_ADMIN))));
    }

    @Test
    @DisplayName("should get a list of tournaments")
    public void testGetTournamentList() throws Exception {
        final Tournament tournament = addTournament(TestHelper.createTournament());

        final UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .path(tournamentContext)
            .build();

        final String tournaments = "$._embedded.tournaments";
        final String pager = "$.page";
        mockMvc.perform(
            get(uriComponents.toUri()))
            .andExpect(status().isOk())
            .andExpect(jsonPath(tournaments + ".[0].id").value(is(tournament.getId())))
            .andExpect(jsonPath(tournaments + ".[0].name").value(is(tournament.getName())))
            .andExpect(jsonPath(tournaments + ".[0].tournamentDate").value(is(tournament.getTournamentDate().toString())))
            .andExpect(jsonPath(tournaments + ".[0].userRoleSet[0].userId").value(is(TestHelper.TOURNAMENT_OWNER_ID)))
            .andExpect(jsonPath(pager + ".size").isNumber())
            .andExpect(jsonPath(pager + ".totalElements").value(is(1)))
            .andExpect(jsonPath(pager + ".totalPages").value(is(1)))
            .andExpect(jsonPath(pager + ".number").value(is(0)));
    }

    @Test
    @DisplayName("should get a tournament by ID")
    public void testGetTournament() throws Exception {
        final Tournament tournament = addTournament(TestHelper.createTournament());

        final UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .path(tournamentContext)
            .path("/{tournamentId}")
            .build();
        final Map<String, String> uriMap = Stream.of(new String[][] {
            { "tournamentId", tournament.getId().toString() },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
    
        mockMvc.perform(
            get(uriComponents.expand(uriMap).toUri()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(is(tournament.getId())))
            .andExpect(jsonPath("$.name").value(is(tournament.getName())))
            .andExpect(jsonPath("$.tournamentDate").value(is(tournament.getTournamentDate().toString())))
            .andExpect(jsonPath("$.userRoleSet[0].user").value(is(TestHelper.TOURNAMENT_OWNER_REFERENCE)));
    }

    @Test
    @DisplayName("should generate tournament result report")
    public void testTournamentResultList() throws Exception {
        final Tournament tournament = addTournament(TestHelper.createTournament());

        final Integer tournamentId = tournament.getId();
        final Event event = TestHelper.createEvent().withTournamentId(tournamentId);
        eventRepositoryImpl.save(event);

        final UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .path(tournamentContext)
            .path("/{tournamentId}/result")
            .build();
        final Map<String, String> uriMap = Stream.of(new String[][] {
            { "tournamentId", tournament.getId().toString() },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        mockMvc.perform(
            get(uriComponents.expand(uriMap).toUri())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tournamentName").value(is(tournament.getName())))
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
        final Tournament tournament = addTournament(TestHelper.createTournament());

        final Integer tournamentId = tournament.getId();
        final Event event = TestHelper.createEvent().withTournamentId(tournamentId);
        eventRepositoryImpl.save(event);

        final UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .path(tournamentContext)
            .path("/{tournamentId}/usatt-result")
            .build();
        final  Map<String, String> uriMap = Stream.of(new String[][] {
            { "tournamentId", tournament.getId().toString() },
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
