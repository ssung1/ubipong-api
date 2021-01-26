package com.eatsleeppong.ubipong.tournamentmanager.repository;

import com.eatsleeppong.ubipong.model.challonge.ChallongeMatchWrapper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Match;
import com.eatsleeppong.ubipong.tournamentmanager.domain.MatchRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChallongeMatchRepository implements MatchRepository {
    private String host;
    private String apiKey;
    private MatchMapper matchMapper;
    private RestTemplate restTemplate;

    public ChallongeMatchRepository(
        @Value("${challonge.host}") final String host,
        @Value("${challonge.api-key}") final String apiKey,
        final MatchMapper matchMapper,
        final RestTemplate restTemplate) {
        this.host = host;
        this.apiKey = apiKey;
        this.matchMapper = matchMapper;
        this.restTemplate = restTemplate;
    }

    /**
     * Get list of matches; only returns non-empty if the tournament has been started.
     * This is a challonge.com issue.
     */
    public ChallongeMatchWrapper[] getMatchList(String tournament) {
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

        return restTemplate.getForObject(uriComponents.expand(uriMap).toUri(),
            ChallongeMatchWrapper[].class);
    }

    public ChallongeMatchWrapper[] getMatchList(String tournament,
        Integer participantId) {
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

        return restTemplate.getForObject(uriComponents.expand(uriMap).toUri(),
            ChallongeMatchWrapper[].class);
    }

    @Override
    public List<Match> findByChallongeUrl(String challongeUrl) {
        return Arrays.stream(getMatchList(challongeUrl))
            .map(matchMapper::mapChallongeMatchWrapperToMatch)
            .collect(Collectors.toUnmodifiableList());
    }
}
