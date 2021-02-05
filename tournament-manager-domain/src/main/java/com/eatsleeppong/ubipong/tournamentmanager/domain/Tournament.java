package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
// We are experimenting with using only annotations to allow Tournament
// to act as a TournamentDto.
//
// The advantage is we don't need to build another class.
// The disadvantage is we can't expose the Dto as a separate library.
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // replace with @Jacksonized after VSCode is fixed
@AllArgsConstructor // remove after VSCode is fixed
public class Tournament {
    @JsonIgnore
    private final EventRepository eventRepository;

    private final Integer id;
    private final String name;
    private final Instant tournamentDate;

    @JsonIgnore
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

    @JsonIgnore
    public List<Event> getEventList() {
        return eventRepository.findByTournamentId(id);
    }
}
