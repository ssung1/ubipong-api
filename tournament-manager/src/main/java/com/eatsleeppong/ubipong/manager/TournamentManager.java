package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.entity.SpringJpaTournament;
import com.eatsleeppong.ubipong.ratingmanager.dto.MatchResultDto;
import com.eatsleeppong.ubipong.ratingmanager.dto.TournamentResultDto;
import com.eatsleeppong.ubipong.tournamentmanager.repository.SpringJpaEventRepository;
import com.eatsleeppong.ubipong.tournamentmanager.repository.SpringJpaTournamentRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Manages tournaments.  Should not be called by EventManager
 */
@Service
public class TournamentManager {
    private final EventManager eventManager;
    private final SpringJpaEventRepository eventRepository;
    private final SpringJpaTournamentRepository tournamentRepository;

    public TournamentManager(final EventManager eventManager, final SpringJpaEventRepository eventRepository,
            final SpringJpaTournamentRepository tournamentRepository) {
        this.eventManager = eventManager;
        this.eventRepository = eventRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public TournamentResultDto createTournamentResultRequest(final Integer id) {
        final List<SpringJpaEvent> eventList = eventRepository.findByTournamentId(id);

        final MatchResultDto[] matchResultDtoList = eventList.stream()
            .map(SpringJpaEvent::getChallongeUrl)
            .map(eventManager::createTournamentResultList)
            .flatMap(Arrays::stream)
            .toArray(MatchResultDto[]::new);

        final SpringJpaTournament tournament = tournamentRepository.getOne(id);

        return TournamentResultDto.builder()
            .tournamentName(tournament.getName())
            .tournamentDate(tournament.getTournamentDate().toInstant())
            .tournamentResultList(matchResultDtoList)
            .build();
    }
}
