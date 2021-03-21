package com.eatsleeppong.ubipong.tournamentmanager.repository;

import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaTournament;
import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaUserTournamentRole;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.eatsleeppong.ubipong.tournamentmanager.domain.Tournament;
import com.eatsleeppong.ubipong.tournamentmanager.domain.TournamentRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRole;
import com.eatsleeppong.ubipong.tournamentmanager.mapper.TournamentMapper;
import com.eatsleeppong.ubipong.tournamentmanager.mapper.UserRoleMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Component
@Value
@AllArgsConstructor
@Slf4j
public class TournamentRepositoryImpl implements TournamentRepository {
    private final SpringJpaTournamentRepository springJpaTournamentRepository;
    private final TournamentMapper tournamentMapper;
    private final SpringJpaUserTournamentRoleRepository springJpaUserTournamentRoleRepository;
    private final UserRoleMapper userRoleMapper;

    private Set<UserRole> findUserRoleByTournamentId(final Integer tournamentId) {
        return springJpaUserTournamentRoleRepository.findByTournamentId(tournamentId).stream()
            .map(userRoleMapper::mapSpringJpaUserTournamentRoleToUserRole)
            .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Tournament getOne(final Integer id) {
        Set<UserRole> userRoleSet = findUserRoleByTournamentId(id);

        return tournamentMapper
            .mapSpringJpaTournamentToTournament(springJpaTournamentRepository.getOne(id))
            .withUserRoleSet(userRoleSet);
    }

    @Override
    public Tournament save(final Tournament tournament) {
        final SpringJpaTournament springJpaTournamentToAdd =
            tournamentMapper.mapTournamentToSpringJpaTournament(tournament);
        final SpringJpaTournament addedSpringJpaTournament =
            springJpaTournamentRepository.save(springJpaTournamentToAdd);

        tournament.getUserRoleSet().stream()
            .map(userRole -> {
                return userRoleMapper.mapUserRoleToSpringJpaUserTournamentRole(
                    userRole, addedSpringJpaTournament.getId());
            })
            .forEach(userRole -> springJpaUserTournamentRoleRepository.save(userRole));

        return tournamentMapper.mapSpringJpaTournamentToTournament(addedSpringJpaTournament)
            .withUserRoleSet(tournament.getUserRoleSet());
    }

    @Override
    public Page<Tournament> findAll(Pageable pageable) {
        final Page<SpringJpaTournament> springJpaTournamentPage =
            springJpaTournamentRepository.findAll(pageable);

        return springJpaTournamentPage.map(springJpaTournament -> {
            Set<UserRole> userRoleSet = findUserRoleByTournamentId(springJpaTournament.getId());
            return tournamentMapper.mapSpringJpaTournamentToTournament(springJpaTournament)
                .withUserRoleSet(userRoleSet);
        });
    }
}
