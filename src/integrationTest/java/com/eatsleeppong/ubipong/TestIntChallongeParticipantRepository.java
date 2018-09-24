package com.eatsleeppong.ubipong;

import com.eatsleeppong.ubipong.model.challonge.ChallongeParticipantWrapper;
import com.eatsleeppong.ubipong.repo.ChallongeMatchRepository;
import com.eatsleeppong.ubipong.repo.ChallongeParticipantRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration-test")
public class TestIntChallongeParticipantRepository {
    private String tournament = "ecs_2018_rr_pg_1";

    @Autowired
    private ChallongeParticipantRepository fixture;

    @Test
    public void testGetParticipantList() {
        Arrays.stream(fixture.getParticipantList(tournament))
            .map(p -> p.getParticipant().getName())
            .forEach(System.out::println);
    }
}
