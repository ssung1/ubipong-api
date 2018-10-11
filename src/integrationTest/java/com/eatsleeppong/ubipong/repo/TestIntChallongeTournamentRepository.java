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

        System.out.println(tournamentWrapper);

        ChallongeTournament tournamentInfo =
                tournamentWrapper.getTournament();
        assertThat(tournamentInfo.getName(), is(tournamentName));
    }
}
