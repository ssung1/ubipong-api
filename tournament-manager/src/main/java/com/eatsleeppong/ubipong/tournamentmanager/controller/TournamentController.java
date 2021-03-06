package com.eatsleeppong.ubipong.tournamentmanager.controller;

import com.eatsleeppong.ubipong.tournamentmanager.controller.mapper.TournamentMapper;
import com.eatsleeppong.ubipong.tournamentmanager.controller.mapper.TournamentResultMapper;
import com.eatsleeppong.ubipong.tournamentmanager.controller.mapper.UserMapper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Role;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Tournament;
import com.eatsleeppong.ubipong.tournamentmanager.domain.TournamentRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.User;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserExternalReference;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRole;
import com.eatsleeppong.ubipong.tournamentmanager.dto.TournamentDto;
import com.eatsleeppong.ubipong.tournamentmanager.usecase.UseCaseTournamentHost;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.eatsleeppong.ubipong.ratingmanager.dto.TournamentResultDto;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/rest/v0/tournaments")
public class TournamentController {
    private final TournamentResultMapper tournamentResultMapper;
    private final UserMapper userMapper;
    private final TournamentMapper tournamentMapper;
    private final UseCaseTournamentHost useCaseTournamentHost;

    @ApiOperation(value = "Tournament Result", notes = "This is used to generate the tournament report to the rating " +
        "authority after the tournament has ended.  It contains all the matches in the tournament in one big list.")
    @GetMapping(value = "/{id}/result")
    public TournamentResultDto getResult(@PathVariable("id") final Integer id) {
        return tournamentResultMapper.mapTournamentResultToTournamentResultDto(
            useCaseTournamentHost.getTournamentResult(id)
        );
    }

    @ApiOperation(value = "Tournament Result", notes = "This is used to generate the tournament report to the USATT " +
        "after the tournament has ended.  It contains all the matches in the tournament in one big list.")
    @GetMapping(value = "/{id}/usatt-result", produces = "text/csv")
    public String getUsattResult(@PathVariable("id") final Integer id) {
        return tournamentResultMapper.mapTournamentResultToUsattCsv(
            useCaseTournamentHost.getTournamentResult(id)
        );
    }

    @ApiOperation(value = "Tournament", notes = "Add a new tournament.")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TournamentDto addTournament(@RequestBody final TournamentDto tournamentDto) {
        final User user = userMapper.mapAuthenticationToUser(
            SecurityContextHolder.getContext().getAuthentication());

        final Tournament tournament = tournamentMapper.mapTournamentDtoToTournament(tournamentDto);

        return tournamentMapper.mapTournamentToTournamentDto(
            useCaseTournamentHost.addTournament(tournament, user));
    }

    @ApiOperation(value = "Tournament", notes = "Get a list of tournaments.")
    @GetMapping()
    public PagedModel<EntityModel<Tournament>> getTournamentList(Pageable pageable,
        PagedResourcesAssembler<Tournament> assembler) {
        return assembler.toModel(useCaseTournamentHost.getTournamentList(pageable));
    }

    @ApiOperation(value = "Tournament", notes = "Get tournament details.")
    @GetMapping("/{id}")
    public TournamentDto getTournament(
        @PathVariable("id") final Integer id
    ) {
        return tournamentMapper.mapTournamentToTournamentDto(useCaseTournamentHost.getTournament(id));
    }
}
