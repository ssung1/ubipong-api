package com.eatsleeppong.ubipong;

import com.eatsleeppong.ubipong.repo.ChallongeMatch;
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
public class TestIntChallongeParticipant {
    final String apiKey = "";

    @Autowired
    private ChallongeMatch fixture;

    @Test
    public void testGetParticipantList() {
        RestTemplate rs = new RestTemplate();

        Map<String, String> uriMap = new HashMap<>();
        uriMap.put("tournament", "ecs_2018_rr_pg_1");

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .scheme("https").host("api.challonge.com")
            .path("/v1")
            .path("/tournaments")
            .path("/{tournament}")
            .path("/participants.json")
            .queryParam("api_key", apiKey)
            .build();

        String response = rs.getForObject(uriComponents.expand(uriMap).toUri(),
            String.class);

        System.out.println(response);
    }
}
