package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Match;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Player;
import com.eatsleeppong.ubipong.tournamentmanager.dto.response.RoundRobinMatch;
import com.eatsleeppong.ubipong.model.challonge.*;

import com.eatsleeppong.ubipong.rating.model.TournamentResultRequestLineItem;
import com.eatsleeppong.ubipong.tournamentmanager.repository.*;
import com.eatsleeppong.ubipong.tournamentmanager.dto.EventDto;
import com.eatsleeppong.ubipong.tournamentmanager.dto.response.Game;
import com.eatsleeppong.ubipong.tournamentmanager.dto.response.RoundRobinCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.webservices.client.AutoConfigureWebServiceClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An event is represented as a list of matches in challonge.com
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TestEventManager {
    private final String challongeUrl = "bikiniBottomOpen-RoundRobin-Group-1";
    private final String eventName = "Round Robin Group 1";

    @MockBean
    private ChallongeTournamentRepository mockTournamentRepository;

    @MockBean
    private ChallongeParticipantRepository mockParticipantRepository;

    @MockBean
    private ChallongeMatchRepository mockMatchRepository;

    // only used to help with testing
    @Autowired
    private SpringJpaEventRepository springJpaEventRepository;

    @Autowired
    private EventManager subject;

    private final Integer spongebobId = 123;
    private final Integer patrickId = 234;
    private final Integer squidwardId = 345;

    private final String spongebobName = "spongebob";
    private final String patrickName = "patrick";
    private final String squidwardName = "squidward";

    private List<ChallongeMatch> getMatchList1() {
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

        return Arrays.asList(m1, m2, m3);
    }

    private List<Match> createMatchList() {
        Integer complete = com.eatsleeppong.ubipong.tournamentmanager.domain.Game.STATUS_COMPLETE;
        com.eatsleeppong.ubipong.tournamentmanager.domain.Game.GameBuilder gameBuilder = com.eatsleeppong.ubipong.tournamentmanager.domain.Game.builder();
        final Match m1 = Match.builder()
            .player1Id(spongebobId)
            .player2Id(patrickId)
            .status(Match.STATUS_COMPLETE)
            .winnerId(spongebobId)
            .gameList(List.of(
                gameBuilder.player1Score(11).player2Score(4).status(complete).build(),
                gameBuilder.player1Score(11).player2Score(5).status(complete).build(),
                gameBuilder.player1Score(11).player2Score(6).status(complete).build()
            ))
            .build();
        final Match m2 = Match.builder()
            .player1Id(patrickId)
            .player2Id(squidwardId)
            .status(Match.STATUS_COMPLETE)
            .winnerId(squidwardId)
            .gameList(List.of(
                gameBuilder.player1Score(9).player2Score(11).status(complete).build(),
                gameBuilder.player1Score(11).player2Score(8).status(complete).build(),
                gameBuilder.player1Score(6).player2Score(11).status(complete).build(),
                gameBuilder.player1Score(5).player2Score(11).status(complete).build()
            ))
            .build();
        final Match m3 = Match.builder()
            .player1Id(spongebobId)
            .player2Id(squidwardId)
            .status(Match.STATUS_INCOMPLETE)
            .build();

        return List.of(m1, m2, m3);
    }

    /**
     * <pre>
     *                   A           B           C                 Place
     *  A spongebob                  W 4 5 6
     *  B patrick        L -4 -5 -6              L -9 8 -6 -5
     *  C squidward                  W 9 -8 6 5
     *
     * </pre>
     */
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

    private List<Player> createPlayerList() {
        final Player spongebob = Player.builder()
            .id(spongebobId)
            .name(spongebobName)
            .build();

        final Player patrick = Player.builder()
            .id(patrickId)
            .name(patrickName)
            .build();

        final Player squidward = Player.builder()
            .id(squidwardId)
            .name(squidwardName)
            .build();

        return List.of(spongebob, patrick, squidward);
    }

    private ChallongeTournamentWrapper getTournamentWrapper1() {
        ChallongeTournament t1 = new ChallongeTournament();
        t1.setName(eventName);
        t1.setDescription("an event is called a tournament on challonge.com");
        t1.setUrl(challongeUrl);

        ChallongeTournamentWrapper tw1 = new ChallongeTournamentWrapper();
        tw1.setTournament(t1);

        return tw1;
    }

    private EventDto createEvent() {
        return EventDto.builder()
            .challongeUrl(challongeUrl)
            .name(eventName)
            .tournamentId(1)
            .build();
    }

    @BeforeEach
    public void setupMocks() {
        when(mockParticipantRepository.findByChallongeUrl(challongeUrl))
            .thenReturn(createPlayerList());
        when(mockMatchRepository.findByChallongeUrl(challongeUrl))
            .thenReturn(createMatchList());

        when(mockMatchRepository.getMatchList(challongeUrl))
            .thenReturn(getMatchWrapperArray1());
        when(mockTournamentRepository.getTournament(challongeUrl))
            .thenReturn(getTournamentWrapper1());
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
        List<ChallongeMatch> matchList = getMatchList1();
        List<ChallongeMatch> spongebobMatchList =
            subject.findByPlayer1(matchList, spongebobId);

        // in this match, player 1 is spongebob
        ChallongeMatch player1IsSpongebob = matchList.get(0);

        assertThat(spongebobMatchList, hasItem(player1IsSpongebob));
    }

    @Test
    @DisplayName("should create the display for a round robin grid from match result")
    public void testCreateRoundRobinGridOneSide() {
        subject.addEvent(createEvent());
        RoundRobinCell[][] roundRobinGrid =
            subject.createRoundRobinGrid(challongeUrl);

        // all cells must be filled, even if it is displayed as empty
        for (int i = 0; i < roundRobinGrid.length; ++i) {
            RoundRobinCell[] row = roundRobinGrid[i];
            for (int j = 0; j < row.length; ++j) {
                assertThat(roundRobinGrid[i][j], notNullValue());
            }
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
    @DisplayName("should create round robin grid from both player's point of view")
    public void testCreateRoundRobinGridBothSides() {
        subject.addEvent(createEvent());
        RoundRobinCell[][] roundRobinGrid =
            subject.createRoundRobinGrid(challongeUrl);

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

    @Test
    @DisplayName("should create tournament result list")
    public void testCreateTournamentResultList() {
        final EventDto event = createEvent();
        subject.addEvent(event);

        final TournamentResultRequestLineItem[] tournamentResultList = subject.createTournamentResultList(challongeUrl);

        assertThat(tournamentResultList, arrayWithSize(2));

        final List<String> allWinners = Arrays.stream(tournamentResultList)
                .map(TournamentResultRequestLineItem::getWinner)
                .collect(Collectors.toList());

        final List<String> allLosers = Arrays.stream(tournamentResultList)
                .map(TournamentResultRequestLineItem::getLoser)
                .collect(Collectors.toList());

        /**
         * <pre>
         *                   A           B           C                 Place
         *  A spongebob                  W 4 5 6
         *  B patrick        L -4 -5 -6              L -9 8 -6 -5
         *  C squidward                  W 9 -8 6 5
         *
         * </pre>
         */

        assertThat(allWinners, hasItem(spongebobName));
        assertThat(allWinners, hasItem(squidwardName));
        // I'm sorry, Patrick
        assertThat(allLosers, hasItem(patrickName));

        final List<String> allResultStrings = Arrays.stream(tournamentResultList)
                .map(TournamentResultRequestLineItem::getResultString)
                .collect(Collectors.toList());
        assertThat(allResultStrings, hasItem("4 5 6"));
        assertThat(allResultStrings, hasItem("9 -8 6 5"));
    }

    @Test
    @DisplayName("should include event name in the tournament result list")
    public void testCreateTournamentResultListShouldIncludeEventTitle() {
        final EventDto event = createEvent();
        subject.addEvent(event);

        final TournamentResultRequestLineItem[] tournamentResultList = subject.createTournamentResultList(challongeUrl);

        // event name in the tournament result list is really the event title
        assertThat(tournamentResultList[0].getEventName(), is(eventName));
    }

    @Test
    public void testCreateRoundRobinMatch() {
        subject.addEvent(createEvent());
        final List<RoundRobinMatch> roundRobinMatchList = subject.createRoundRobinMatchList(challongeUrl);

        assertThat(roundRobinMatchList, hasSize(3));

        assertThat(roundRobinMatchList.get(0).getPlayer1Seed(), is("A"));
        assertThat(roundRobinMatchList.get(0).getPlayer1Name(), is(spongebobName));
        assertThat(roundRobinMatchList.get(0).getPlayer2Seed(), is("B"));
        assertThat(roundRobinMatchList.get(0).getPlayer2Name(), is(patrickName));

        assertThat(roundRobinMatchList.get(1).getPlayer1Seed(), is("B"));
        assertThat(roundRobinMatchList.get(1).getPlayer1Name(), is(patrickName));
        assertThat(roundRobinMatchList.get(1).getPlayer2Seed(), is("C"));
        assertThat(roundRobinMatchList.get(1).getPlayer2Name(), is(squidwardName));

        assertThat(roundRobinMatchList.get(2).getPlayer1Seed(), is("A"));
        assertThat(roundRobinMatchList.get(2).getPlayer1Name(), is(spongebobName));
        assertThat(roundRobinMatchList.get(2).getPlayer2Seed(), is("C"));
        assertThat(roundRobinMatchList.get(2).getPlayer2Name(), is(squidwardName));
    }

    @Test
    @DisplayName("add an event in our own database and a tournament on challonge.com")
    public void testAddEventLinkedToChallonge() {
        final EventDto event = createEvent();

        final ArgumentCaptor<ChallongeTournamentWrapper> argument = 
           ArgumentCaptor.forClass(ChallongeTournamentWrapper.class);
        final SpringJpaEvent addedEvent = subject.addEvent(event);
        verify(mockTournamentRepository).createTournament(argument.capture());

        assertThat(argument.getValue().getTournament().getName(), is(event.getName()));

        final Optional<SpringJpaEvent> retrievedEvent = springJpaEventRepository.findById(addedEvent.getId());
        assertTrue(retrievedEvent.isPresent());
    }
}
