package com.eatsleeppong.ubipong.repo;

import com.eatsleeppong.ubipong.model.challonge.ChallongeMatchWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration-test")
public class TestIntChallongeMatchRepository {
    private final String tournament = "integration_test_rr";

    @Autowired
    private ChallongeMatchRepository subject;

    @Test
    public void testGetMatchListWithParticipantId() {
        ChallongeMatchWrapper[] matchList =
            subject.getMatchList(tournament, 83173696);
        assertThat(matchList, arrayWithSize(3));
    }

    @Test
    public void testGetMatchList() {
        ChallongeMatchWrapper[] matchList =
            subject.getMatchList(tournament);
        assertThat(matchList, arrayWithSize(6));
        assertThat(matchList[0].getMatch(), notNullValue());
    }
}
