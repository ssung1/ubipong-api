package com.eatsleeppong.ubipong.tournamentmanager.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Date;

import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaTournament;
import com.eatsleeppong.ubipong.tournamentmanager.TestHelper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Tournament;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TestTournamentRepositoryImpl {

    /**
     * just a helper to insert SpringJpaTournament into database
     */
    @Autowired
    private SpringJpaTournamentRepository springJpaTournamentRepository;

    @Autowired
    private TournamentRepositoryImpl tournamentRepository;

    /**
     * remove later when we do not use SpringJpaTournament directly
     */
    private SpringJpaTournament createSpringJpaTournament() {
        final Tournament tournament = TestHelper.createTournament();
        final SpringJpaTournament springJpaTournament = new SpringJpaTournament();
        springJpaTournament.setName(tournament.getName());
        springJpaTournament.setTournamentDate(Date.from(tournament.getTournamentDate()));
        return springJpaTournament;
    }

    @Test
    @DisplayName("should return a tournament by tournament ID")
    public void testGetOne() {
        final SpringJpaTournament springJpaTournamentToAdd = createSpringJpaTournament();
        final SpringJpaTournament addedSpringJpaTournament =
            springJpaTournamentRepository.save(springJpaTournamentToAdd);
        final Integer tournamentId = addedSpringJpaTournament.getId();

        final Tournament tournament = tournamentRepository.getOne(tournamentId);

        assertThat(tournament, notNullValue());
        assertThat(tournament.getName(), is(springJpaTournamentToAdd.getName()));
        assertThat(tournament.getTournamentDate(), is(springJpaTournamentToAdd.getTournamentDate().toInstant()));
    }
}
