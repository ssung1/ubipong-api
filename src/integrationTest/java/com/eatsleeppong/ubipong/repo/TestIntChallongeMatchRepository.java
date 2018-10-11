package com.eatsleeppong.ubipong.repo;

import com.eatsleeppong.ubipong.model.challonge.ChallongeMatch;
import com.eatsleeppong.ubipong.model.challonge.ChallongeMatchWrapper;
import com.eatsleeppong.ubipong.repo.ChallongeMatchRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration-test")
public class TestIntChallongeMatchRepository {
    private String tournament = "integration_test_rr";

    @Autowired
    private ChallongeMatchRepository fixture;

    @Test
    public void testGetMatchListWithParticipantId() {
        ChallongeMatchWrapper[] matchList =
            fixture.getMatchList(tournament, 83173696);
        assertThat(matchList, arrayWithSize(3));
    }

    @Test
    public void testGetMatchList() {
        ChallongeMatchWrapper[] matchList =
            fixture.getMatchList(tournament);
        assertThat(matchList, arrayWithSize(6));
        assertThat(matchList[0].getMatch(), notNullValue());
    }
}
