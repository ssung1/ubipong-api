package com.eatsleeppong.ubipong.tournamentmanager.repository;

import com.eatsleeppong.ubipong.tournamentmanager.domain.Tournament;
import com.eatsleeppong.ubipong.tournamentmanager.domain.TournamentRepository;
import com.eatsleeppong.ubipong.tournamentmanager.mapper.TournamentMapper;

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

    @Override
    public Tournament getOne(final Integer id) {
        return tournamentMapper.mapSpringJpaTournamentToTournament(springJpaTournamentRepository.getOne(id));
    }
}
