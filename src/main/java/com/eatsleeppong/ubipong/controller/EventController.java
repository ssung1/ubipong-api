package com.eatsleeppong.ubipong.controller;

import com.eatsleeppong.ubipong.manager.EventManager;
import com.eatsleeppong.ubipong.model.RoundRobinCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/v0/event")
public class EventController {

    private EventManager eventManager;

    public EventController(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @GetMapping(value = "/roundRobinGrid/{eventId}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public RoundRobinCell[][] roundRobinGrid(
        @PathVariable("eventId") String eventId
    ) {
        return eventManager.createRoundRobinGrid(eventId);
    }
}
