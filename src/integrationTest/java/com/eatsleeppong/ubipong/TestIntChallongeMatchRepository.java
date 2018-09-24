package com.eatsleeppong.ubipong;

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

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration-test")
public class TestIntChallongeMatchRepository {
    private String tournament = "ecs_2018_rr_pg_1";

    @Autowired
    private ChallongeMatchRepository fixture;

    @Test
    public void testGetMatchListWithParticipantId() {
        System.out.println(fixture.getMatchList(tournament, 82304119));
    }

    @Test
    public void testGetMatchList() {
        System.out.println(fixture.getMatchList(tournament));
    }
}
