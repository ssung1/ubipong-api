package com.eatsleeppong.ubipong.tournamentmanager.controller.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.eatsleeppong.ubipong.tournamentmanager.domain.Tournament;
import com.eatsleeppong.ubipong.tournamentmanager.domain.User;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRepository;
import com.eatsleeppong.ubipong.tournamentmanager.dto.TournamentDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.UserRoleDto;
import com.eatsleeppong.ubipong.tournamentmanager.domain.EventRepository;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component("controllerTournamentMapper")
@AllArgsConstructor
public class TournamentMapper {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserRoleMapper userRoleMapper;

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

    public Tournament mapTournamentDtoToTournament(final TournamentDto tournamentDto) {
        return Tournament.builder()
            .eventRepository(eventRepository)
            .name(tournamentDto.getName())
            .tournamentDate(tournamentDto.getTournamentDate())
            .build();
    }
}
