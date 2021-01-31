package com.eatsleeppong.ubipong.tournamentmanager.controller;

import com.eatsleeppong.ubipong.tournamentmanager.domain.TournamentRepository;
import com.eatsleeppong.ubipong.tournamentmanager.mapper.TournamentResultMapper;

import com.eatsleeppong.ubipong.ratingmanager.dto.TournamentResultDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.request.TournamentRequest;
import com.eatsleeppong.ubipong.tournamentmanager.dto.response.TournamentResponse;
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
    @GetMapping(value = "/{id}/usatt-result")
    public String getUsattResult(@PathVariable("id") final Integer id) {
        return tournamentResultMapper.mapTournamentResultToUsattCsv(
            tournamentRepository.getOne(id).getResult()
        );
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
