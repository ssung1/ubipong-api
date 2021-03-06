package com.eatsleeppong.ubipong.tournamentmanager.repository;

import com.eatsleeppong.ubipong.model.challonge.ChallongeParticipantWrapper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Player;
import com.eatsleeppong.ubipong.tournamentmanager.domain.PlayerRepository;
import com.eatsleeppong.ubipong.tournamentmanager.repository.mapper.PlayerMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChallongeParticipantRepository implements PlayerRepository {
    private String host;
    private String apiKey;
    private PlayerMapper playerMapper;
    private RestTemplate restTemplate;

    public ChallongeParticipantRepository(
        @Value("${challonge.host}") final String host,
        @Value("${challonge.api-key}") final String apiKey,
        final PlayerMapper playerMapper,
        final RestTemplate restTemplate) {
        this.host = host;
        this.apiKey = apiKey;
        this.playerMapper = playerMapper;
        this.restTemplate = restTemplate;
    }

    public ChallongeParticipantWrapper[] getParticipantList(
        String tournament) {

        Map<String, String> uriMap = new HashMap<>();
        uriMap.put("tournament", tournament);

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .scheme("https").host(host)
            .path("/v1")
            .path("/tournaments")
            .path("/{tournament}")
            .path("/participants.json")
            .queryParam("api_key", apiKey)
            .build();

            return restTemplate.getForObject(uriComponents.expand(uriMap).toUri(),
            ChallongeParticipantWrapper[].class);
    }

    @Override
    public List<Player> findByChallongeUrl(String challongeUrl) {
        return Arrays.stream(getParticipantList(challongeUrl))
            .map(playerMapper::mapChallongeParticipantWrapperToPlayer)
            .collect(Collectors.toUnmodifiableList());
    }
}
