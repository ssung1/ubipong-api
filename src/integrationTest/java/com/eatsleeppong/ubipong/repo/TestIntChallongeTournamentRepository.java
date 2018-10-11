package com.eatsleeppong.ubipong.repo;

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

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration-test")
public class TestIntChallongeTournamentRepository {
    @Test
    public void testGetTournamentList() {
        RestTemplate rs = new RestTemplate();

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .scheme("https").host("api.challonge.com")
            .path("/v1")
            .path("/tournaments.json")
            .queryParam("api_key", "")
            .build();

        String response = rs.getForObject(uriComponents.toUri(),
            String.class);

        System.out.println(response);
    }
}
