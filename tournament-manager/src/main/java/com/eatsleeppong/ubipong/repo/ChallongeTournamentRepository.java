package com.eatsleeppong.ubipong.repo;

import com.eatsleeppong.ubipong.model.challonge.ChallongeParticipantWrapper;
import com.eatsleeppong.ubipong.model.challonge.ChallongeTournament;
import com.eatsleeppong.ubipong.model.challonge.ChallongeTournamentWrapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class ChallongeTournamentRepository {
    @Value("${challonge.host}")
    private String host;

    @Value("${challonge.api-key}")
    private String apiKey;

    public ChallongeTournamentWrapper getTournament(
        String tournament) {
        final RestTemplate rs = new RestTemplate();

        final Map<String, String> uriMap = new HashMap<>();
        uriMap.put("tournament", tournament);

        final UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https").host(host)
                .path("/v1")
                .path("/tournaments")
                .path("/{tournament}.json")
                .queryParam("api_key", apiKey)
                .build();

        return rs.getForObject(uriComponents.expand(uriMap).toUri(),
                ChallongeTournamentWrapper.class);
    }

    public ChallongeTournamentWrapper createTournament(ChallongeTournamentWrapper challongeTournamentWrapper) {
        final RestTemplate rs = new RestTemplate();

        final UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https").host(host)
                .path("/v1")
                .path("/tournaments.json")
                .queryParam("api_key", apiKey)
                .build();

        // create a special mapper that does not serialize nulls, because nulls will cause server error on
        // challonge.com
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // we need to apply our speial mapper to our RestTemplate
        final MappingJackson2HttpMessageConverter c = new MappingJackson2HttpMessageConverter();
        c.setObjectMapper(mapper);
        rs.setMessageConverters(Collections.singletonList(c));

        return rs.postForObject(uriComponents.expand().toUri(),challongeTournamentWrapper,
                ChallongeTournamentWrapper.class);
    }
}
