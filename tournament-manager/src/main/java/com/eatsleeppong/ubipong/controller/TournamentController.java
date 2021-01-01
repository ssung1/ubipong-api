package com.eatsleeppong.ubipong.controller;

import com.eatsleeppong.ubipong.manager.TournamentManager;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequest;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequestLineItem;
import com.eatsleeppong.ubipong.tournamentmanager.dto.request.TournamentRequest;
import com.eatsleeppong.ubipong.tournamentmanager.dto.response.TournamentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/v0/tournaments")
public class TournamentController {
    private final TournamentManager tournamentManager;

    public TournamentController(final TournamentManager tournamentManager) {
        this.tournamentManager = tournamentManager;
    }

    @GetMapping(value = "/{tournamentId}/result")
    public TournamentResultRequest getResult(@PathVariable("tournamentId") final Integer tournamentId) {
        return tournamentManager.createTournamentResultRequest(tournamentId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TournamentResponse addTournament(@RequestBody TournamentRequest tournamentRequest) {
        // somehow build a Tournament out of this request
        // call the "repo" to save a tournament
        // translate tournament to tournamentresponse

        // is all of this necessary(?)
        return TournamentResponse.builder()
            .name(tournamentRequest.getName())
            .tournamentDate(tournamentRequest.getTournamentDate())
            .build();
    }
}
