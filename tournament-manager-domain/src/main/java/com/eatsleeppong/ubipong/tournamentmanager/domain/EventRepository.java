package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.util.Optional;

// this is work in progress
// we want to see if we can build interface to access event
// (but how to create new event???)
public interface EventRepository {
    Event save(Event event);
    Event getOne(Integer id);
}
