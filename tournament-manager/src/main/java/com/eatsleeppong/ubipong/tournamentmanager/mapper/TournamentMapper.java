package com.eatsleeppong.ubipong.tournamentmanager.mapper;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaTournament;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Tournament;
import com.eatsleeppong.ubipong.tournamentmanager.domain.User;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRepository;
import com.eatsleeppong.ubipong.tournamentmanager.dto.TournamentDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.UserRoleDto;
import com.eatsleeppong.ubipong.tournamentmanager.domain.EventRepository;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TournamentMapper {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserRoleMapper userRoleMapper;

    public Tournament mapSpringJpaTournamentToTournament(final SpringJpaTournament springJpaTournament) {
        return Tournament.builder()
            .id(springJpaTournament.getId())
            .name(springJpaTournament.getName())
            .tournamentDate(springJpaTournament.getTournamentDate().toInstant())
            .eventRepository(eventRepository)
            .build();
    }

    public SpringJpaTournament mapTournamentToSpringJpaTournament(final Tournament tournament) {
        final SpringJpaTournament springJpaTournament = new SpringJpaTournament();
        springJpaTournament.setId(tournament.getId());
        springJpaTournament.setName(tournament.getName());
        springJpaTournament.setTournamentDate(Date.from(tournament.getTournamentDate()));

        return springJpaTournament;
    }

    public TournamentDto mapTournamentToTournamentDto(final Tournament tournament) {
        final Set<UserRoleDto> userRoleSet = tournament.getUserRoleSet().stream()
            .map(userRole -> {
                final User user = userRepository.getOne(userRole.getUserId());
                return userRoleMapper.mapUserRoleAndUserToUserRoleDto(userRole, user);
            })
            .collect(Collectors.toUnmodifiableSet());
        return TournamentDto.builder()
            .id(tournament.getId())
            .name(tournament.getName())
            .tournamentDate(tournament.getTournamentDate())
            .userRoleSet(userRoleSet)
            .build();
    }
}
