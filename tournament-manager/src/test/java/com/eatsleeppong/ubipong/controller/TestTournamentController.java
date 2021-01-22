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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
}
