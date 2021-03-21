package com.eatsleeppong.ubipong.tournamentmanager.domain;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.With;

/**
 * A tournament contains a number of events.  It is basic unit of rating; that is,
 * a player's rating may change after a tournament but not during.
 */
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
    EventRepository eventRepository;

    Integer id;
    @Size(max = 60)
    String name;
    Instant tournamentDate;

    // input is ignored for now...
    Set<UserRole> userRoleSet;

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
