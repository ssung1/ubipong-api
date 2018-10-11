package com.eatsleeppong.ubipong.repo;

import com.eatsleeppong.ubipong.model.challonge.ChallongeParticipantWrapper;
import com.eatsleeppong.ubipong.model.challonge.ChallongeTournamentWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChallongeTournamentRepository {
    @Value("${challonge.host}")
    private String host;

    @Value("${challonge.api-key}")
    private String apiKey;

    public ChallongeTournamentWrapper getTournament(
        String tournament) {
        RestTemplate rs = new RestTemplate();

        Map<String, String> uriMap = new HashMap<>();
        uriMap.put("tournament", tournament);

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .scheme("https").host(host)
            .path("/v1")
            .path("/tournaments")
            .path("/{tournament}.json")
            .queryParam("api_key", apiKey)
            .build();

            return rs.getForObject(uriComponents.expand(uriMap).toUri(),
            ChallongeTournamentWrapper.class);
    }
}
