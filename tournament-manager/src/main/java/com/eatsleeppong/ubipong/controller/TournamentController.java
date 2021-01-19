package com.eatsleeppong.ubipong.controller;

import com.eatsleeppong.ubipong.manager.TournamentManager;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequest;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequestLineItem;
import com.eatsleeppong.ubipong.tournamentmanager.dto.request.TournamentRequest;
import com.eatsleeppong.ubipong.tournamentmanager.dto.response.TournamentResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/v0/tournaments")
public class TournamentController {
    private final TournamentManager tournamentManager;

    public TournamentController(final TournamentManager tournamentManager) {
        this.tournamentManager = tournamentManager;
    }

    @ApiOperation(value = "Tournament Result", notes = "This is used to generate the tournament report to the rating " +
        "authority after the tournament has ended.  It contains all the matches in the tournament in one big list.")
    @GetMapping(value = "/{id}/result")
    public TournamentResultRequest getResult(@PathVariable("id") final Integer id) {
        return tournamentManager.createTournamentResultRequest(id);
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
