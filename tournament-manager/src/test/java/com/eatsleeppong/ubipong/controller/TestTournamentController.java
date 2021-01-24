package com.eatsleeppong.ubipong.controller;

import com.eatsleeppong.ubipong.tournamentmanager.dto.request.TournamentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class TestTournamentController {
    final TournamentRequest tournamentRequest = TournamentRequest.builder()
        .name("Eat Sleep Pong Open 2020")
        .tournamentDate("2020-12-31")
        .build();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Create a tournament")
    @Disabled("This is part of using logic unit -- we are not sure if it is worth the effort")
    public void testAddTournament() throws Exception {
        mockMvc.perform(
            post("/rest/v0/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tournamentRequest)))
            .andExpect(status().is(HttpStatus.CREATED.value()))
            .andExpect(jsonPath("id").exists());
    }

    @Test
    @DisplayName("should generate the event result report")
    public void testTournamentResultList() throws Exception {
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
