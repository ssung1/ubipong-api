package com.eatsleeppong.ubipong;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

public class TestIntChallongeMatch {
    @Test
    public void testGetMatchList() {
        RestTemplate rs = new RestTemplate();

        Map<String, String> uriMap = new HashMap<>();
        uriMap.put("tournament", "ecs_2018_rr_pg_1");

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .scheme("https").host("api.challonge.com")
            .path("/v1")
            .path("/tournaments")
            .path("/{tournament}")
            .path("/matches.json")
            .queryParam("api_key", "")
            .build();

        String response = rs.getForObject(uriComponents.expand(uriMap).toUri(),
            String.class);

        System.out.println(response);
    }
}
