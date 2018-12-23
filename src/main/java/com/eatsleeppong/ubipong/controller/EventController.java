package com.eatsleeppong.ubipong.controller;

import com.eatsleeppong.ubipong.manager.EventManager;
import com.eatsleeppong.ubipong.model.Event;
import com.eatsleeppong.ubipong.model.RoundRobinCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/v0/event/{eventUrl}")
public class EventController {

    private EventManager eventManager;

    public EventController(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @GetMapping(value = "/roundRobinGrid",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public RoundRobinCell[][] getRoundRobinGrid(
        @PathVariable("eventUrl") String eventUrl
    ) {
        return eventManager.createRoundRobinGrid(eventUrl);
    }

    @GetMapping(value = "")
    public Event getEvent(
        @PathVariable("eventUrl") String eventUrl
    ) {
        return eventManager.findEvent(eventUrl);
    }
}
