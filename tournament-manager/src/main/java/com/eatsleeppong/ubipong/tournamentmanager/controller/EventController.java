package com.eatsleeppong.ubipong.tournamentmanager.controller;

import com.eatsleeppong.ubipong.tournamentmanager.dto.MatchSheetDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.RoundRobinCellDto;
import com.eatsleeppong.ubipong.tournamentmanager.usecase.UseCaseTournamentHost;
import com.eatsleeppong.ubipong.ratingmanager.dto.MatchResultDto;
import com.eatsleeppong.ubipong.tournamentmanager.controller.mapper.EventMapper;
import com.eatsleeppong.ubipong.tournamentmanager.controller.mapper.MatchResultMapper;
import com.eatsleeppong.ubipong.tournamentmanager.controller.mapper.MatchSheetMapper;
import com.eatsleeppong.ubipong.tournamentmanager.controller.mapper.RoundRobinCellMapper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.RoundRobinCell;
import com.eatsleeppong.ubipong.tournamentmanager.dto.EventDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/rest/v0/events")
@Slf4j
public class EventController {

    private final EventMapper eventMapper;
    private final MatchResultMapper matchResultMapper;
    private final MatchSheetMapper matchSheetMapper;
    private final RoundRobinCellMapper roundRobinCellMapper;
    private final UseCaseTournamentHost useCaseTournamentHost;

    @ApiOperation(value = "Round Robin Grid", notes = "This creates a grid of the contents that is useful for " +
        "displaying the draw and results of a round robin event. The response is a 2-dimensional JSON array.  " +
        "Each component in the array should be displayed as a table cell in the UI.  It looks like " +
        "<pre>" +
        "|     |           | A  | B         | C   |\n" +
        "| A   | SpongeBob | -- | W 9 8 9   |     |\n" +
        "| B   | Patrick   |    | --        |     |\n" +
        "| C   | Squidward |    |           | --  |\n" +
        "</pre>")
    @GetMapping(value = "/{id}/roundRobinGrid",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public RoundRobinCellDto[][] getRoundRobinGrid(
        @PathVariable("id") Integer id
    ) {
        final List<List<RoundRobinCell>> roundRobinGrid = useCaseTournamentHost.getRoundRobinGrid(id);
        return roundRobinGrid.stream()
            .map(row -> row.stream()
                .map(roundRobinCellMapper::mapRoundRobinCellToRoundRobinCellDto)
                .toArray(RoundRobinCellDto[]::new))
            .toArray(RoundRobinCellDto[][]::new);
    }

    @ApiOperation(value = "Event", notes = "Get event details.")
    @GetMapping(value = "/{id}")
    public EventDto getEvent(
        @PathVariable("id") Integer id
    ) {
        return eventMapper.mapEventToEventDto(useCaseTournamentHost.getEvent(id));
    }

    @ApiOperation(value = "Event Result", notes = "This generates the results of an event.  It is like " +
        "/rest/v0/tournaments/{id}/result, but it contains only the results of an event.  It is " +
        "unsuitable for submission to the rating authority because rating must be calculated from all the" +
        "results in a tournament, together, in a single batch.")
    @GetMapping(value = "/{challongeUrl}/result")
    public List<MatchResultDto> getResult(@PathVariable("challongeUrl") String challongeUrl) {
        return useCaseTournamentHost.getMatchResultList(challongeUrl).stream()
            .map(matchResultMapper::mapMatchResultToMatchResultDto)
            .collect(Collectors.toUnmodifiableList());
    }

    @ApiOperation(value = "Round Robin Match List", notes = "This returns all the matches of a round robin " +
        "event.  It is useful for generating match sheets, but the UI is responsible for all formatting.")
    @GetMapping(value = "/{challongeUrl}/roundRobinMatchList")
    public List<MatchSheetDto> getRoundRobinMatchList(
        @ApiParam(value = "The URL of the challonge tournament") @PathVariable("challongeUrl") String challongeUrl
    ) {
        return useCaseTournamentHost.getRoundRobinMatchList(challongeUrl).stream()
            .map(matchSheetMapper::mapMatchSheetToMatchSheetDto)
            .collect(Collectors.toUnmodifiableList());
    }

    @ApiOperation(value = "Event", notes = "Add a new event.")
    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto addEvent(@RequestBody @Valid final EventDto eventDto) {
        log.info("Adding event: {}", eventDto);
        return eventMapper.mapEventToEventDto(
            useCaseTournamentHost.addEvent(eventMapper.mapEventDtoToEvent(eventDto))
        );
    }

    @ApiOperation(value = "Event List", notes = "This returns a list of events of a given tournament ID.")
    @GetMapping(value = "/search/find-by-tournament-id")
    @ResponseStatus(HttpStatus.OK)
    public List<EventDto> findByTournamentId(@RequestParam("tournament-id") Integer tournamentId) {
        return useCaseTournamentHost.findEventByTournamentId(tournamentId).stream()
            .map(eventMapper::mapEventToEventDto)
            .collect(Collectors.toUnmodifiableList());
    }

    @ApiOperation(value = "Event", notes = "Update new event.")
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto addEvent(@PathVariable("id") Integer id, @RequestBody @Valid final EventDto eventDto) {
        log.info("Updating event: id = {}, {}", id, eventDto);
        return eventMapper.mapEventToEventDto(
            useCaseTournamentHost.updateEvent(eventMapper.mapEventDtoToEvent(eventDto))
        );
    }
}
