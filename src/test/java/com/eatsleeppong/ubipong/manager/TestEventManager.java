package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.model.Game;
import com.eatsleeppong.ubipong.model.RoundRobinCell;
import com.eatsleeppong.ubipong.model.challonge.ChallongeMatch;
import com.eatsleeppong.ubipong.model.challonge.ChallongeMatchWrapper;

import com.eatsleeppong.ubipong.model.challonge.ChallongeParticipant;
import com.eatsleeppong.ubipong.model.challonge.ChallongeParticipantWrapper;
import com.eatsleeppong.ubipong.repo.ChallongeMatchRepository;
import com.eatsleeppong.ubipong.repo.ChallongeParticipantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * An event is represented as a list of matches in challonge.com
 */
public class TestEventManager {
    private final String eventName = "bikiniBottomOpen-RoundRobin-Group-1";
    private final ChallongeParticipantRepository mockParticipantRepository =
        mock(ChallongeParticipantRepository.class);
    private final ChallongeMatchRepository mockMatchRepository =
        mock(ChallongeMatchRepository.class);

    private EventManager subject = new EventManager(
        mockParticipantRepository, mockMatchRepository
    );

    private final Integer spongebobId = 123;
    private final Integer patrickId = 234;
    private final Integer squidwardId = 345;

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

    private ChallongeMatchWrapper[] getMatchWrapperArray1() {
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
        m3.setState(ChallongeMatch.STATE_OPEN);

        return Stream.of(m1, m2, m3)
            .map(m -> {
                ChallongeMatchWrapper mw = new ChallongeMatchWrapper();
                mw.setMatch(m);
                return mw;
            })
            .toArray(ChallongeMatchWrapper[]::new);
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

    private ChallongeParticipantWrapper[] getParticipantWrapperArray1() {
        ChallongeParticipant spongebob = new ChallongeParticipant();
        spongebob.setId(spongebobId);
        spongebob.setDisplayName("spongebob");

        ChallongeParticipant patrick = new ChallongeParticipant();
        patrick.setId(patrickId);
        patrick.setDisplayName("patrick");

        ChallongeParticipant squidward = new ChallongeParticipant();
        squidward.setId(squidwardId);
        squidward.setDisplayName("squidward");

        return Stream.of(spongebob, patrick, squidward)
            .map(p -> {
                ChallongeParticipantWrapper pw =
                    new ChallongeParticipantWrapper();
                pw.setParticipant(p);
                return pw;
            })
            .toArray(ChallongeParticipantWrapper[]::new);
    }

    @Before
    public void setupMocks() {
        when(mockParticipantRepository.getParticipantList(eventName))
            .thenReturn(
                getParticipantWrapperArray1()
            );
        when(mockMatchRepository.getMatchList(eventName))
            .thenReturn(
                getMatchWrapperArray1()
            );
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
    public void testUnwrapChallongeParticipantWrapperArray() {
        ChallongeParticipant p = new ChallongeParticipant();
        p.setId(spongebobId);

        ChallongeParticipantWrapper pw = new ChallongeParticipantWrapper();
        pw.setParticipant(p);

        ChallongeParticipantWrapper[] challongeParticipantWrapperArray = {
            pw
        };

        List<ChallongeParticipant> challongeParticipantList =
            subject.unwrapChallongeParticipantWrapperArray(
                challongeParticipantWrapperArray);

        assertThat(challongeParticipantList, hasSize(1));
        assertThat(challongeParticipantList, hasItem(p));
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
    public void testCreateRoundRobinGridOneSide() {
        RoundRobinCell[][] roundRobinGrid =
            subject.createRoundRobinGrid(eventName);

        // all cells must be filled, even if it is displayed as empty
        for (int i = 0; i < roundRobinGrid.length; ++i) {
            RoundRobinCell[] row = roundRobinGrid[i];
            for (int j = 0; j < row.length; ++j) {
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

        // row header
        assertThat(roundRobinGrid[0][0].getType(),
            is(RoundRobinCell.TYPE_EMPTY));
        assertThat(roundRobinGrid[0][1].getType(),
            is(RoundRobinCell.TYPE_EMPTY));
        assertThat(roundRobinGrid[0][2].getType(),
            is(RoundRobinCell.TYPE_TEXT));
        assertThat(roundRobinGrid[0][2].getContent(), is("A"));

        // column header
        assertThat(roundRobinGrid[1][0].getType(),
            is(RoundRobinCell.TYPE_TEXT));
        assertThat(roundRobinGrid[1][0].getContent(), is("A"));

        // row header

        RoundRobinCell spongebobVsPatrick = roundRobinGrid[1][3];
        assertThat(spongebobVsPatrick.getType(),
            is(RoundRobinCell.TYPE_MATCH_COMPLETE));
        assertThat(spongebobVsPatrick.isWinForPlayer1(), is(true));
        assertThat(spongebobVsPatrick.getContent(),
            is("W 4 5 6"));

        // scores are 11-4, 11-5, 11-6

        assertThat(spongebobVsPatrick.getGameList(), hasSize(3));
        Game firstGame = spongebobVsPatrick.getGameList().get(0);
        assertThat(firstGame.getPlayer1Score(), is(11));
        assertThat(firstGame.getPlayer2Score(), is(4));
        assertThat(firstGame.isWinForPlayer1(), is(true));

        RoundRobinCell patrickVsSquidward = roundRobinGrid[2][4];
        assertThat(patrickVsSquidward.isWinForPlayer1(), is(false));
        assertThat(patrickVsSquidward.getContent(),
            is("L -9 8 -6 -5"));
        // scores are 9-11, 11-8, 6-11, 5-11

        Game thirdGame = patrickVsSquidward.getGameList().get(2);
        assertThat(thirdGame.isWinForPlayer1(), is(false));
        assertThat(thirdGame.getPlayer1Score(), is(6));
        assertThat(thirdGame.getPlayer2Score(), is(11));
    }

    @Test
    public void testCreateInverseCell() {
        RoundRobinCell cell = new RoundRobinCell();
        // W 1 -2 3
        cell.setType(RoundRobinCell.TYPE_MATCH_COMPLETE);
        cell.setWinForPlayer1(true);

        Game game1 = new Game();
        game1.setWinForPlayer1(true);
        game1.setPlayer1Score(11);
        game1.setPlayer2Score(1);

        Game game2 = new Game();
        game2.setWinForPlayer1(false);
        game2.setPlayer1Score(2);
        game2.setPlayer2Score(11);

        Game game3 = new Game();
        game3.setWinForPlayer1(true);
        game3.setPlayer1Score(11);
        game3.setPlayer2Score(3);

        cell.setGameList(Arrays.asList(game1, game2, game3));

        RoundRobinCell inverseCell = subject.createInverseCell(cell);

        List<Game> gameList = inverseCell.getGameList();
        // inverse of game1
        assertThat(gameList.get(0).getPlayer1Score(), is(1));
        assertThat(gameList.get(0).getPlayer2Score(), is(11));
        assertThat(gameList.get(0).isWinForPlayer1(), is(false));

        assertThat(gameList.get(1).getPlayer1Score(), is(11));
        assertThat(gameList.get(1).getPlayer2Score(), is(2));
        assertThat(gameList.get(1).isWinForPlayer1(), is(true));

        assertThat(inverseCell.getContent(), is("L -1 2 -3"));
    }

    /**
     * @see #testCreateRoundRobinGridOneSide
     */
    @Test
    public void testCreateRoundRobinGridBothSides() {
        RoundRobinCell[][] roundRobinGrid =
            subject.createRoundRobinGrid(eventName);

        // this is one side of the result
        //
        //                  A          B           C       Place
        // A spongebob                win
        // B patrick                             loss
        // C squidward

        // we want to make sure we have the other side too
        // (in parentheses)
        //                  A          B           C       Place
        // A spongebob                win
        // B patrick       (loss)                loss
        // C squidward               (win)

        // original
        RoundRobinCell spongebobVsPatrick = roundRobinGrid[1][3];
        assertThat(spongebobVsPatrick.isWinForPlayer1(), is(true));

        // inverse
        RoundRobinCell patrickVsSpongebob = roundRobinGrid[2][2];
        assertThat(patrickVsSpongebob.isWinForPlayer1(), is(false));
        assertThat(patrickVsSpongebob.getContent(), is("L -4 -5 -6"));
    }
}
