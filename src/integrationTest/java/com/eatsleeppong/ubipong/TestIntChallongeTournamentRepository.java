package com.eatsleeppong.ubipong;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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
