package com.eatsleeppong.ubipong.controller;

import com.eatsleeppong.ubipong.manager.TournamentManager;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequest;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequestLineItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
