package com.eatsleeppong.ubipong.repo;

import com.eatsleeppong.ubipong.model.challonge.ChallongeMatchWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChallongeMatchRepository {
    @Value("${challonge.host}")
    private String host;

    @Value("${challonge.api-key}")
    private String apiKey;

    /**
     * Get list of matches; only returns non-empty if the tournament has been started.
     * This is a challonge.com issue.
     */
    public ChallongeMatchWrapper[] getMatchList(String tournament) {
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
            ChallongeMatchWrapper[].class);
    }

    public ChallongeMatchWrapper[] getMatchList(String tournament,
        Integer participantId) {
        RestTemplate rs = new RestTemplate();

        Map<String, String> uriMap = new HashMap<>();
        uriMap.put("tournament", tournament);

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .scheme("https").host(host)
            .path("/v1")
            .path("/tournaments")
            .path("/{tournament}")
            .path("/matches.json")
            .queryParam("participant_id", String.valueOf(participantId))
            .queryParam("api_key", apiKey)
            .build();

        return rs.getForObject(uriComponents.expand(uriMap).toUri(),
            ChallongeMatchWrapper[].class);
    }
}
