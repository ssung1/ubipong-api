package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.model.Game;
import com.eatsleeppong.ubipong.model.RoundRobinCell;
import com.eatsleeppong.ubipong.model.challonge.ChallongeMatch;
import com.eatsleeppong.ubipong.model.challonge.ChallongeMatchWrapper;

import com.eatsleeppong.ubipong.model.challonge.ChallongeParticipant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * An event is represented as a list of matches in challonge.com
 */
public class TestEventManager {
    private EventManager subject = new EventManager();

    private Integer spongebobId = 123;
    private Integer patrickId = 234;
    private Integer squidwardId = 345;

    private List<ChallongeMatch> getTestSample1() {
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
        m3.setPlayer1Id(squidwardId);
        m3.setPlayer2Id(spongebobId);

        return Arrays.asList(m1, m2, m3);
    }

    private List<ChallongeParticipant> getPlayerList1() {
        ChallongeParticipant spongebob = new ChallongeParticipant();
        spongebob.setId(spongebobId);
        spongebob.setDisplayName("spongebob");

        ChallongeParticipant patrick = new ChallongeParticipant();
        patrick.setId(patrickId);
        patrick.setDisplayName("patrick");

        ChallongeParticipant squidward = new ChallongeParticipant();
        squidward.setId(squidwardId);
        squidward.setDisplayName("squidward");

        return Arrays.asList(spongebob, patrick, squidward);
    }

    @Test
    public void testUnwrapChallongeMatchWrapperArray() {
        ChallongeMatch m1 = new ChallongeMatch();
        m1.setPlayer1Id(spongebobId);
        m1.setPlayer2Id(patrickId);
        m1.setWinnerId(spongebobId);
        m1.setScoresCsv("11-4,11-5,11-6");

        ChallongeMatchWrapper mw1 = new ChallongeMatchWrapper();
        mw1.setMatch(m1);

        ChallongeMatch m2 = new ChallongeMatch();
        m2.setPlayer1Id(patrickId);
        m2.setPlayer2Id(squidwardId);
        m2.setWinnerId(patrickId);
        m2.setScoresCsv("11-9,8-11,11-6,11-5");

        ChallongeMatchWrapper mw2 = new ChallongeMatchWrapper();
        mw2.setMatch(m2);

        ChallongeMatch m3 = new ChallongeMatch();
        m3.setPlayer1Id(squidwardId);
        m3.setPlayer2Id(spongebobId);
        m1.setWinnerId(spongebobId);
        m3.setScoresCsv("5-11,6-11,7-11");

        ChallongeMatchWrapper mw3 = new ChallongeMatchWrapper();
        mw3.setMatch(m3);

        ChallongeMatchWrapper[] challongeWrapperArray = 
            new ChallongeMatchWrapper[] {
                mw1, mw2, mw3
            };
        
        List<ChallongeMatch> challongeMatchList =
            subject.unwrapChallongeMatchWrapperArray(challongeWrapperArray);

        assertThat(challongeMatchList, hasSize(3));
        assertThat(challongeMatchList, hasItem(m1));
        assertThat(challongeMatchList, hasItem(m2));
        assertThat(challongeMatchList, hasItem(m3));
    }

    @Test
    public void testFindByPlayer1() {
        List<ChallongeMatch> matchList = getTestSample1();
        List<ChallongeMatch> spongebobMatchList =
            subject.findByPlayer1(matchList, spongebobId);

        // in this match, player 1 is spongebob
        ChallongeMatch player1IsSpongebob = matchList.get(0);

        assertThat(spongebobMatchList, hasItem(player1IsSpongebob));
    }

    @Test
    public void testCreatePlayerIndexMap() {
        List<ChallongeParticipant> playerList = getPlayerList1();
        Map<Integer, Integer> indexMap =
            subject.createPlayerIndexMap(playerList);

        assertThat(indexMap.get(spongebobId), is(0));
        assertThat(indexMap.get(patrickId), is(1));
        assertThat(indexMap.get(squidwardId), is(2));
    }

    @Test
    public void testCreateRoundRobinGrid() {
        List<ChallongeMatch> matchList = getTestSample1();
        List<ChallongeParticipant> playerList = getPlayerList1();

        int size = playerList.size();
        RoundRobinCell[][] roundRobinGrid =
            subject.createRoundRobinGrid(matchList, playerList);

        // all cells must be filled, even if it is displayed as empty
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                assertThat(roundRobinGrid[i][j], notNullValue());
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(roundRobinGrid));
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }

        // the three matches are
        //
        // spongebob vs patrick
        // patrick vs squidward
        // squidward vs spongebob
        //
        // so we should at least have
        //
        //                  A          B           C       Place
        // A spongebob                win
        // B patrick                             loss
        // C squidward
//        assertThat(roundRobinGrid[0][0].getType(),
//            is(RoundRobinCell.TYPE_EMPTY));

        // column header
        assertThat(roundRobinGrid[1][0].getType(),
            is(RoundRobinCell.TYPE_TEXT));
        assertThat(roundRobinGrid[1][0].getContent(), is("A"));

        // row header

        RoundRobinCell spongebobVsPatrick = roundRobinGrid[1][3];
        assertThat(spongebobVsPatrick.getType(),
            is(RoundRobinCell.TYPE_MATCH_COMPLETE));
        assertThat(spongebobVsPatrick.getContent(),
            is("W 4 5 6"));

        // scores are 11-4, 11-5, 11-6

        assertThat(spongebobVsPatrick.getGameList(), hasSize(3));
        Game firstGame = spongebobVsPatrick.getGameList().get(0);
        assertThat(firstGame.getPlayer1Score(), is(11));
        assertThat(firstGame.getPlayer2Score(), is(4));
        assertThat(firstGame.isWinForPlayer1(), is(true));

        RoundRobinCell patrickVsSquidward = roundRobinGrid[2][4];
        assertThat(patrickVsSquidward.getContent(),
            is("L -9 8 -6 -5"));
        // scores are 9-11, 11-8, 6-11, 5-11

        Game thirdGame = patrickVsSquidward.getGameList().get(2);
        assertThat(thirdGame.isWinForPlayer1(), is(false));
        assertThat(thirdGame.getPlayer1Score(), is(6));
        assertThat(thirdGame.getPlayer2Score(), is(11));
    }
}
