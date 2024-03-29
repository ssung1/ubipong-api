package com.eatsleeppong.ubipong.tournamentmanager.usecase;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import com.eatsleeppong.ubipong.tournamentmanager.domain.EventRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.MatchResult;
import com.eatsleeppong.ubipong.tournamentmanager.domain.MatchSheet;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Role;
import com.eatsleeppong.ubipong.tournamentmanager.domain.RoundRobinCell;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Tournament;
import com.eatsleeppong.ubipong.tournamentmanager.domain.TournamentRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.TournamentResult;
import com.eatsleeppong.ubipong.tournamentmanager.domain.User;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRole;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
@AllArgsConstructor
@Service
public class UseCaseTournamentHost {
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public Tournament addTournament(final Tournament tournament, final User tournamentAdmin) {
        final User existingUser = userRepository.findByExternalReference(
            tournamentAdmin.getExternalReference()).orElseGet(() -> userRepository.save(tournamentAdmin));
    
        // the owner is the admin of the tournament
        final UserRole adminRole = UserRole.builder()
            .userId(existingUser.getId())
            .role(Role.TOURNAMENT_ADMIN)
            .build();

        return tournamentRepository.save(tournament.withUserRoleSet(Set.of(adminRole)));
    }

    public Page<Tournament> getTournamentList(final Pageable pageable) {
        return tournamentRepository.findAll(pageable);
    }

    public Tournament getTournament(final Integer id) {
        return tournamentRepository.getOne(id);
    }

    public TournamentResult getTournamentResult(final Integer id) {
        return getTournament(id).getResult();
    }

    public Event addEvent(final Event event) {
        return eventRepository.save(event.withId(null));
    }

    public Event updateEvent(final Event event) {
        return eventRepository.save(event);
    }

    public Event getEvent(final Integer id) {
        return eventRepository.getOne(id);
    }

    public List<Event> findEventByTournamentId(final Integer tournamentId) {
        return eventRepository.findByTournamentId(tournamentId);
    }

    public List<List<RoundRobinCell>> getRoundRobinGrid(final Integer eventId) {
        return eventRepository.getOne(eventId).getRoundRobinGrid();
    }

    public List<MatchResult> getMatchResultList(final String challongeUrl) {
        return eventRepository.getOneByChallongeUrl(challongeUrl).getMatchResultList();
    }

    public List<MatchSheet> getRoundRobinMatchList(final String challongeUrl) {
        return eventRepository.getOneByChallongeUrl(challongeUrl).getMatchSheetList();
    }
}
