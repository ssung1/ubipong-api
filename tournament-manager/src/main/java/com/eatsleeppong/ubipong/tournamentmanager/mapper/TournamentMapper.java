package com.eatsleeppong.ubipong.tournamentmanager.mapper;

import java.util.Date;

import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaTournament;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Tournament;
import com.eatsleeppong.ubipong.tournamentmanager.domain.EventRepository;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TournamentMapper {
    private final EventRepository eventRepository;

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
}
