package com.eatsleeppong.ubipong.tournamentmanager.controller;

import com.eatsleeppong.ubipong.tournamentmanager.domain.Tournament;
import com.eatsleeppong.ubipong.tournamentmanager.domain.TournamentRepository;
import com.eatsleeppong.ubipong.tournamentmanager.mapper.TournamentResultMapper;

import com.eatsleeppong.ubipong.ratingmanager.dto.TournamentResultDto;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/rest/v0/tournaments")
public class TournamentController {
    private final TournamentRepository tournamentRepository;
    private final TournamentResultMapper tournamentResultMapper;

    @ApiOperation(value = "Tournament Result", notes = "This is used to generate the tournament report to the rating " +
        "authority after the tournament has ended.  It contains all the matches in the tournament in one big list.")
    @GetMapping(value = "/{id}/result")
    public TournamentResultDto getResult(@PathVariable("id") final Integer id) {
        return tournamentResultMapper.mapTournamentResultToTournamentResultDto(
            tournamentRepository.getOne(id).getResult()
        );
    }

    @ApiOperation(value = "Tournament Result", notes = "This is used to generate the tournament report to the USATT " +
        "after the tournament has ended.  It contains all the matches in the tournament in one big list.")
    @GetMapping(value = "/{id}/usatt-result", produces = "text/csv")
    public String getUsattResult(@PathVariable("id") final Integer id) {
        return tournamentResultMapper.mapTournamentResultToUsattCsv(
            tournamentRepository.getOne(id).getResult()
        );
    }

    @ApiOperation(value = "Tournament", notes = "Add a new tournament")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Tournament addTournament(@RequestBody Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    @ApiOperation(value = "Tournament", notes = "Get tournament details")
    @GetMapping("/{id}")
    public Tournament getTournament(
        @PathVariable("id") final Integer id
    ) {
        return tournamentRepository.getOne(id);
    }
}
