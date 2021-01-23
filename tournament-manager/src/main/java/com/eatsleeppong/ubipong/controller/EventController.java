package com.eatsleeppong.ubipong.controller;

import com.eatsleeppong.ubipong.manager.EventManager;
import com.eatsleeppong.ubipong.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.tournamentmanager.dto.response.RoundRobinMatch;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequestLineItem;
import com.eatsleeppong.ubipong.tournamentmanager.dto.EventDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.response.RoundRobinCell;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/v0/events")
public class EventController {

    private EventManager eventManager;

    public EventController(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @ApiOperation(value = "Round Robin Grid", notes = "This creates a grid of the contents that is useful for " +
        "displaying the draw and results of a round robin event. The response is a 2-dimensional JSON array.  " +
        "Each component in the array should be displayed as a table cell in the UI.  It looks like " +
        "<pre>" +
        "|     |           | A  | B         | C   |\n" +
        "| A   | SpongeBob | -- | W 9 8 9   |     |\n" +
        "| B   | Patrick   |    | --        |     |\n" +
        "| C   | Squidward |    |           | --  |\n" +
        "</pre>")
    @GetMapping(value = "/{challongeUrl}/roundRobinGrid",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public RoundRobinCell[][] getRoundRobinGrid(
        @PathVariable("challongeUrl") String challongeUrl
    ) {
        return eventManager.createRoundRobinGrid(challongeUrl);
    }

    @ApiOperation(value = "Event", notes = "This is mainly used to get details that are only on challonge.com, " +
        "such as the event name.  For database-only event, use /crud/events")
    @GetMapping(value = "/{id}")
    public SpringJpaEvent getEvent(
        @PathVariable("id") Integer id
    ) {
        return eventManager.findEvent(id);
    }

    @ApiOperation(value = "Event Result", notes = "This generates the results of an event.  It is like " +
        "/rest/v0/tournaments/{id}/result, but it contains only the results of an event.  It is " +
        "unsuitable for submission to the rating authority because rating must be calculated from all the" +
        "results in a tournament, together, in a single batch.")
    @GetMapping(value = "/{challongeUrl}/result")
    public TournamentResultRequestLineItem[] getResult(@PathVariable("challongeUrl") String challongeUrl) {
        return eventManager.createTournamentResultList(challongeUrl);
    }

    @ApiOperation(value = "Round Robin Match List", notes = "This returns all the matches of a round robin " +
        "event.  It is useful for generating match sheets, but the UI is responsible for all formatting.")
    @GetMapping(value = "/{challongeUrl}/roundRobinMatchList")
    public List<RoundRobinMatch> getRoundRobinMatchList(
        @PathVariable("challongeUrl") String challongeUrl
    ) {
        return eventManager.createRoundRobinMatchList(challongeUrl);
    }

    @ApiOperation(value = "Event", notes = "This creates an event both on the database and on challonge.com.  " +
        "This endpoint should be used instead of /crud/events.")
    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    public SpringJpaEvent addEvent(@RequestBody SpringJpaEvent event) {
        return eventManager.addEvent(event);
    }
}
