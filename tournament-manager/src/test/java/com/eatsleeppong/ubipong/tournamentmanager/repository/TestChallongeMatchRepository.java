package com.eatsleeppong.ubipong.tournamentmanager.repository;

import com.eatsleeppong.ubipong.model.challonge.ChallongeMatch;
import com.eatsleeppong.ubipong.model.challonge.ChallongeMatchWrapper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Game;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Match;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

import com.eatsleeppong.ubipong.tournamentmanager.mapper.MatchMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.client.RestTemplate;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class TestChallongeMatchRepository {
    private final Integer spongebobId = 1;
    private final String spongebobName = "spongebob";
    private final Integer patrickId = 2;
    private final String patrickName = "patrick";
    private final Integer squidwardId = 3;
    private final String squidwardName = "squidward";
    private final String challongeUrl = "esp_201903_pg_rr_1";
    private final String host = "api.challonge.com";
    private final String apiKey = "this-is-api-key-from-challonge.com";
    private final MatchMapper matchMapper = new MatchMapper();
    private final RestTemplate mockRestTemplate = mock(RestTemplate.class);

    private final ChallongeMatchRepository challongeMatchRepository = 
        new ChallongeMatchRepository(host, apiKey, matchMapper, mockRestTemplate);

    /**
     * <pre>
     *                   A           B           C                 Place
     *  A spongebob                  W 4 5 6
     *  B patrick        L -4 -5 -6              L -9 8 -6 -5
     *  C squidward                  W 9 -8 6 5
     *
     * </pre>
     */
    private ChallongeMatchWrapper[] createMatchWrapperList() {
        ChallongeMatch m1 = new ChallongeMatch();
        m1.setPlayer1Id(spongebobId);
        m1.setPlayer2Id(patrickId);
        m1.setState(ChallongeMatch.STATE_COMPLETE);
        m1.setWinnerId(spongebobId);
        m1.setScoresCsv("11-4,11-5,11-6");

        ChallongeMatch m2 = new ChallongeMatch();
        m2.setPlayer1Id(patrickId);
        m2.setPlayer2Id(squidwardId);
        m2.setState(ChallongeMatch.STATE_COMPLETE);
        m2.setWinnerId(squidwardId);
        m2.setScoresCsv("9-11,11-8,6-11,5-11");

        ChallongeMatch m3 = new ChallongeMatch();
        m3.setPlayer1Id(spongebobId);
        m3.setPlayer2Id(squidwardId);
        m3.setState(ChallongeMatch.STATE_OPEN);

        return Stream.of(m1, m2, m3)
            .map(m -> {
                ChallongeMatchWrapper mw = new ChallongeMatchWrapper();
                mw.setMatch(m);
                return mw;
            })
            .toArray(ChallongeMatchWrapper[]::new);
    }

    @Test
    @DisplayName("should be able to find a list of players by challongeUrl")
    public void testFindByChallongeUrl() throws Exception {
        final ArgumentCaptor<URI> argument = ArgumentCaptor.forClass(URI.class);
        when(mockRestTemplate.getForObject(argument.capture(),
            eq(ChallongeMatchWrapper[].class)))
            .thenReturn(createMatchWrapperList());

        List<Match> matchList = 
            challongeMatchRepository.findByChallongeUrl(challongeUrl);

        assertThat(argument.getValue().toString(), containsString(host));

        assertThat(matchList, hasSize(3));

        assertThat(matchList.get(0).getStatus(), is(Match.STATUS_COMPLETE));
        assertThat(matchList.get(0).getPlayer1Id(), is(spongebobId));
        assertThat(matchList.get(0).getPlayer2Id(), is(patrickId));
        assertThat(matchList.get(0).getWinnerId(), is(spongebobId));
        assertThat(matchList.get(0).getResultCode(), is(Match.RESULT_CODE_WIN_BY_PLAYING));
        assertThat(matchList.get(0).getGame(0).getPlayer1Score(), is(11));
        assertThat(matchList.get(0).getGame(0).getPlayer2Score(), is(4));
        assertThat(matchList.get(0).getGame(0).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(0).getGame(0).isWinForPlayer1(), is(true));
        assertThat(matchList.get(0).getGame(1).getPlayer1Score(), is(11));
        assertThat(matchList.get(0).getGame(1).getPlayer2Score(), is(5));
        assertThat(matchList.get(0).getGame(1).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(0).getGame(1).isWinForPlayer1(), is(true));
        assertThat(matchList.get(0).getGame(2).getPlayer1Score(), is(11));
        assertThat(matchList.get(0).getGame(2).getPlayer2Score(), is(6));
        assertThat(matchList.get(0).getGame(2).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(0).getGame(2).isWinForPlayer1(), is(true));

        assertThat(matchList.get(1).getStatus(), is(Match.STATUS_COMPLETE));
        assertThat(matchList.get(1).getPlayer1Id(), is(patrickId));
        assertThat(matchList.get(1).getPlayer2Id(), is(squidwardId));
        assertThat(matchList.get(1).getWinnerId(), is(squidwardId));
        assertThat(matchList.get(1).getResultCode(), is(Match.RESULT_CODE_WIN_BY_PLAYING));
        assertThat(matchList.get(1).getGame(0).getPlayer1Score(), is(9));
        assertThat(matchList.get(1).getGame(0).getPlayer2Score(), is(11));
        assertThat(matchList.get(1).getGame(0).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(1).getGame(0).isWinForPlayer1(), is(false));
        assertThat(matchList.get(1).getGame(1).getPlayer1Score(), is(11));
        assertThat(matchList.get(1).getGame(1).getPlayer2Score(), is(8));
        assertThat(matchList.get(1).getGame(1).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(1).getGame(1).isWinForPlayer1(), is(true));
        assertThat(matchList.get(1).getGame(2).getPlayer1Score(), is(6));
        assertThat(matchList.get(1).getGame(2).getPlayer2Score(), is(11));
        assertThat(matchList.get(1).getGame(2).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(1).getGame(2).isWinForPlayer1(), is(false));
        assertThat(matchList.get(1).getGame(3).getPlayer1Score(), is(5));
        assertThat(matchList.get(1).getGame(3).getPlayer2Score(), is(11));
        assertThat(matchList.get(1).getGame(3).getStatus(), is(Game.STATUS_COMPLETE));
        assertThat(matchList.get(1).getGame(3).isWinForPlayer1(), is(false));

        assertThat(matchList.get(2).getStatus(), is(Match.STATUS_INCOMPLETE));
        assertThat(matchList.get(2).getPlayer1Id(), is(spongebobId));
        assertThat(matchList.get(2).getPlayer2Id(), is(squidwardId));
        assertThat(matchList.get(2).getWinnerId(), nullValue());
        assertThat(matchList.get(2).getResultCode(), nullValue());
    }
}