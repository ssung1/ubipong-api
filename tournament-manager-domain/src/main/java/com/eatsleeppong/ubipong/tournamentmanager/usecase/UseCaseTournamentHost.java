package com.eatsleeppong.ubipong.tournamentmanager.usecase;

import java.util.Set;

import com.eatsleeppong.ubipong.tournamentmanager.domain.Role;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Tournament;
import com.eatsleeppong.ubipong.tournamentmanager.domain.TournamentRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.User;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRole;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
@AllArgsConstructor
@Service
public class UseCaseTournamentHost {
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;

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
}
