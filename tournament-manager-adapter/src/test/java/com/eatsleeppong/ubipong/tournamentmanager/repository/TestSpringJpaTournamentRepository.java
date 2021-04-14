package com.eatsleeppong.ubipong.tournamentmanager.repository;

import com.eatsleeppong.ubipong.tournamentmanager.AdapterContextConfiguration;
import com.eatsleeppong.ubipong.tournamentmanager.AdapterTestConfiguration;
import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaTournament;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = AdapterContextConfiguration.class)
@Import(AdapterTestConfiguration.class)
@Transactional
@ActiveProfiles("test")
class TestSpringJpaTournamentRepository {
    private final String tournamentName = "Eat Sleep Pong Open 2019";
    private final String tournamentDate = "2019-03-15T00:00:00-05:00";

    @Autowired
    SpringJpaTournamentRepository tournamentRepository;

    @Test
    public void testUpdateTournament() throws Exception {
        final String newTournamentName = "new name";
        final SpringJpaTournament tournament = new SpringJpaTournament();
        tournament.setName(tournamentName);
        tournament.setTournamentDate(Date.from(OffsetDateTime.parse(tournamentDate).toInstant()));

        final SpringJpaTournament savedTournament = tournamentRepository.save(tournament);

        assertNotNull(savedTournament);
        assertNotNull(savedTournament.getId());

        final Integer tournamentId = savedTournament.getId();
        assertThat(savedTournament.getName(), is(tournamentName));

        final SpringJpaTournament updatedTournament = savedTournament.withName(newTournamentName);
        tournamentRepository.save(updatedTournament);

        final SpringJpaTournament savedUpdatedTournament = tournamentRepository.getOne(tournamentId);

        assertThat(savedUpdatedTournament.getId(), is(tournamentId));
        assertThat(savedUpdatedTournament.getName(), is(newTournamentName));
    }
}