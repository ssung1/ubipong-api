package com.eatsleeppong.ubipong.repo;

import com.eatsleeppong.ubipong.entity.Tournament;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TestTournamentRepository {
    private final Integer tournamentId = 1234987;
    private final String tournamentName = "Eat Sleep Pong Open 2019";
    private final String tournamentDate = "2019-03-15T00:00:00-05:00";

    @Autowired
    TournamentRepository tournamentRepository;

    @Test
    public void testUpdateTournament() throws Exception {
        final String newTournamentName = "new name";
        final Tournament tournament = new Tournament();
        tournament.setName(tournamentName);
        tournament.setTournamentDate(Date.from(OffsetDateTime.parse(tournamentDate).toInstant()));

        final Tournament savedTournament = tournamentRepository.save(tournament);

        assertNotNull(savedTournament);
        assertNotNull(savedTournament.getId());

        final Integer tournamentId = savedTournament.getId();
        assertThat(savedTournament.getName(), is(tournamentName));

        final Tournament updatedTournament = savedTournament.withName(newTournamentName);
        tournamentRepository.save(updatedTournament);

        final Tournament savedUpdatedTournament = tournamentRepository.getOne(tournamentId);

        assertThat(savedUpdatedTournament.getId(), is(tournamentId));
        assertThat(savedUpdatedTournament.getName(), is(newTournamentName));
    }
}