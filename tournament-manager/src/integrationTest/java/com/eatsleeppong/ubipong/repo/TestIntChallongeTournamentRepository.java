package com.eatsleeppong.ubipong.repo;

import com.eatsleeppong.ubipong.model.challonge.ChallongeTournament;
import com.eatsleeppong.ubipong.model.challonge.ChallongeTournamentWrapper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration-test")
public class TestIntChallongeTournamentRepository {
    private final String tournament = "integration_test_rr";
    private final String tournamentName =
            "round robin tournament for integration test";

    @Autowired
    private ChallongeTournamentRepository subject;

    @Test
    public void testGetTournament() {
        ChallongeTournamentWrapper tournamentWrapper =
                subject.getTournament(tournament);

        ChallongeTournament tournamentInfo =
                tournamentWrapper.getTournament();
        assertThat(tournamentInfo.getName(), is(tournamentName));
    }

    @Test
    public void testCreateTournament() throws Exception {
        final ChallongeTournament challongeTournament = new ChallongeTournament();

        challongeTournament.setUrl("5zefby63x");
        challongeTournament.setName("new round robin tournament");
        challongeTournament.setGameName("Table Tennis");
        challongeTournament.setTournamentType("single elimination");

        final ChallongeTournamentWrapper challongeTournamentWrapper = new ChallongeTournamentWrapper();
        challongeTournamentWrapper.setTournament(challongeTournament);

        try{
            final ChallongeTournamentWrapper resultWrapper =
                    subject.createTournament(challongeTournamentWrapper);

            final ChallongeTournament result = resultWrapper.getTournament();

            assertThat(result.getName(), is("new round robin tournament"));
        } catch (HttpClientErrorException ex) {
            System.out.println(ex);
            throw ex;
        }
    }
}
