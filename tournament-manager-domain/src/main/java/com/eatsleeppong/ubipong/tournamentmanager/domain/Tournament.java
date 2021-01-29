package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Tournament {
    private final EventRepository eventRepository;

    private final Integer id;
    private final String name;
    private final Instant tournamentDate;

    public TournamentResult getResult() {
        return TournamentResult.builder()
            .tournamentName(getName())
            .tournamentDate(getTournamentDate())
            .matchResultList(getEventList().stream()
                .map(Event::getMatchResultList)
                .flatMap(List::stream)
                .collect(Collectors.toUnmodifiableList()))
            .build();
    }

    public List<Event> getEventList() {
        return eventRepository.findByTournamentId(id);
    }
}
