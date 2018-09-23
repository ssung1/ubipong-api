package com.eatsleeppong.ubipong.repo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChallongeMatch {
    @Value("${challonge.host}")
    private String host;

    @Value("${challonge.api-key}")
    private String apiKey;

    public String getMatchList(String tournament) {
        RestTemplate rs = new RestTemplate();

        Map<String, String> uriMap = new HashMap<>();
        uriMap.put("tournament", tournament);

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .scheme("https").host(host)
            .path("/v1")
            .path("/tournaments")
            .path("/{tournament}")
            .path("/matches.json")
            .queryParam("api_key", apiKey)
            .build();

        return rs.getForObject(uriComponents.expand(uriMap).toUri(),
            String.class);
    }
}