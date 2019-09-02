package com.eatsleeppong.ubipong.controller;

import com.eatsleeppong.ubipong.manager.EventManager;
import com.eatsleeppong.ubipong.entity.Event;
import com.eatsleeppong.ubipong.model.RoundRobinCell;
import com.eatsleeppong.ubipong.model.RoundRobinMatch;
import com.eatsleeppong.ubipong.rating.model.TournamentResultRequestLineItem;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/v0/events/{challongeUrl}")
public class EventController {

    private EventManager eventManager;

    public EventController(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @GetMapping(value = "/roundRobinGrid",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public RoundRobinCell[][] getRoundRobinGrid(
        @PathVariable("challongeUrl") String challongeUrl
    ) {
        return eventManager.createRoundRobinGrid(challongeUrl);
    }

    @GetMapping(value = "")
    public Event getEvent(
        @PathVariable("challongeUrl") String challongeUrl
    ) {
        return eventManager.findEvent(challongeUrl);
    }

    @GetMapping(value = "/result")
    public TournamentResultRequestLineItem[] getResult(@PathVariable("challongeUrl") String challongeUrl) {
        return eventManager.createTournamentResultList(challongeUrl);
    }

    @GetMapping(value = "/roundRobinMatchList")
    public List<RoundRobinMatch> getRoundRobinMatchList(
            @PathVariable("challongeUrl") String challongeUrl
    ) {
        return eventManager.createRoundRobinMatchList(challongeUrl);
    }
}
