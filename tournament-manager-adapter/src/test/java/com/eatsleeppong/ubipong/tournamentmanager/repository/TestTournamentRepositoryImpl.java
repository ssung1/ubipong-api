package com.eatsleeppong.ubipong.tournamentmanager.repository;

import java.util.Set;

import com.eatsleeppong.ubipong.tournamentmanager.AdapterContextConfiguration;
import com.eatsleeppong.ubipong.tournamentmanager.AdapterTestConfiguration;
import com.eatsleeppong.ubipong.tournamentmanager.TestHelper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Tournament;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRole;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = AdapterContextConfiguration.class)
@Import(AdapterTestConfiguration.class)
@ActiveProfiles("test")
@Transactional
public class TestTournamentRepositoryImpl {
    @Autowired
    private TournamentRepositoryImpl tournamentRepository;

    private Tournament addTournament(final Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    @Test
    @DisplayName("should return a tournament by tournament ID")
    public void testGetOne() {
        final UserRole userRole = TestHelper.createUserRole();
        final Tournament addedTournament = addTournament(
            TestHelper.createTournament().withUserRoleSet(Set.of(userRole))
        );

        final Integer tournamentId = addedTournament.getId();

        final Tournament tournament = tournamentRepository.getOne(tournamentId);

        assertThat(tournament, notNullValue());
        assertThat(tournament.getName(), is(addedTournament.getName()));
        assertThat(tournament.getTournamentDate(), is(addedTournament.getTournamentDate()));
        assertThat(tournament.getUserRoleSet(), hasItem(userRole));
    }
}
