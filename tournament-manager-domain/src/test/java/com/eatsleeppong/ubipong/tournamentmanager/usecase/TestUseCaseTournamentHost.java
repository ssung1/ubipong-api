package com.eatsleeppong.ubipong.tournamentmanager.usecase;

import com.eatsleeppong.ubipong.tournamentmanager.TestHelper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Event;
import com.eatsleeppong.ubipong.tournamentmanager.domain.EventRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.MatchResult;
import com.eatsleeppong.ubipong.tournamentmanager.domain.MatchSheet;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Role;
import com.eatsleeppong.ubipong.tournamentmanager.domain.RoundRobinCell;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Tournament;
import com.eatsleeppong.ubipong.tournamentmanager.domain.TournamentRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.TournamentResult;
import com.eatsleeppong.ubipong.tournamentmanager.domain.User;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRepository;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRole;
import com.eatsleeppong.ubipong.tournamentmanager.domain.UserRoleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Matchers.any;
import static org.hamcrest.MatcherAssert.*;

public class TestUseCaseTournamentHost {

    private final TournamentRepository mockTournamentRepository = mock(TournamentRepository.class);
    private final UserRepository mockUserRepository = mock(UserRepository.class);
    private final EventRepository mockEventRepository = mock(EventRepository.class);

    private UseCaseTournamentHost useCaseTournamentHost = new UseCaseTournamentHost(
        mockTournamentRepository,
        mockUserRepository,
        mockEventRepository
    );

    @BeforeEach
    public void setupMocks() {
        when(mockTournamentRepository.save(any(Tournament.class))).thenAnswer(invocation ->
            invocation.getArgument(0));
        when(mockUserRepository.save(any(User.class))).thenAnswer(invocation ->
            invocation.getArgument(0));
        when(mockEventRepository.save(any(Event.class))).thenAnswer(invocation ->
            invocation.getArgument(0));
    }

    @Test
    public void testAddTournamentWithExistingUser() {
        final User owner = TestHelper.createUser();
        final UserRole adminRole = UserRole.builder()
            .userId(owner.getId())
            .role(Role.TOURNAMENT_ADMIN)
            .build();
        final Tournament tournament = TestHelper.createTournament();

        when(mockUserRepository.findByExternalReference(owner.getExternalReference())).thenReturn(Optional.of(owner));

        final Tournament addedTournament = useCaseTournamentHost.addTournament(tournament, owner);

        assertThat(addedTournament, notNullValue());
        assertThat(addedTournament.getName(), is(tournament.getName()));
        assertThat(addedTournament.getTournamentDate(), is(tournament.getTournamentDate()));
        assertThat(addedTournament.getUserRoleSet(), hasItem(adminRole));
    }

    @Test
    public void testAddTournamentWithNewUser() {
        final User owner = TestHelper.createUser();
        final UserRole adminRole = UserRole.builder()
            .userId(owner.getId())
            .role(Role.TOURNAMENT_ADMIN)
            .build();
        final Tournament tournament = TestHelper.createTournament();

        when(mockUserRepository.findByExternalReference(owner.getExternalReference())).thenReturn(Optional.empty());

        final Tournament addedTournament = useCaseTournamentHost.addTournament(tournament, owner);

        assertThat(addedTournament, notNullValue());
        assertThat(addedTournament.getName(), is(tournament.getName()));
        assertThat(addedTournament.getTournamentDate(), is(tournament.getTournamentDate()));
        assertThat(addedTournament.getUserRoleSet(), hasItem(adminRole));
    }

    @Test
    public void testGetTournamentList() {
        final PageRequest pageRequest = PageRequest.of(0, 1);
        final Page<Tournament> expectedPage = Page.empty();
        when(mockTournamentRepository.findAll(pageRequest)).thenReturn(expectedPage);

        final Page<Tournament> page = useCaseTournamentHost.getTournamentList(PageRequest.of(0, 1));

        assertThat(page, is(expectedPage));
    }

    @Test
    public void testGetTournament() {
        final Integer id = 123;
        final Tournament expectedTournament = TestHelper.createTournament();
        when(mockTournamentRepository.getOne(id)).thenReturn(expectedTournament);

        final Tournament tournament = useCaseTournamentHost.getTournament(id);

        assertThat(tournament, is(expectedTournament));
    }

    @Test
    public void testGetTournamentResult() {
        final Tournament expectedTournament = TestHelper.createTournament();
        // need to override event list because we cannot get result for incomplete
        // matches
        final Event event = expectedTournament.getEventList().get(0);
        when(event.getMatchRepository().findByChallongeUrl(event.getChallongeUrl()))
            .thenReturn(List.of(
                TestHelper.createMatch1(), TestHelper.createMatch2()
            ));

        final Integer id = 123;
        when(mockTournamentRepository.getOne(id)).thenReturn(expectedTournament);

        final TournamentResult tournamentResult = useCaseTournamentHost.getTournamentResult(id);

        assertThat(tournamentResult, is(expectedTournament.getResult()));
    }

    @Test
    public void testAddEvent() {
        final Event event = TestHelper.createEvent();
        final Event addedEvent = useCaseTournamentHost.addEvent(event);
        // should be the same except for id
        assertThat(addedEvent.withId(null), is(event.withId(null)));
    }

    @Test
    public void testFindEventByTournamentId() {
        final Integer tournamentId = 123;
        final List<Event> expectedEventList = List.of(TestHelper.createEvent());

        when(mockEventRepository.findByTournamentId(tournamentId)).thenReturn(expectedEventList);
        final List<Event> eventList = useCaseTournamentHost.findEventByTournamentId(tournamentId);
        assertThat(eventList, is(expectedEventList));
    }

    @Test
    public void testGetEvent() {
        final Integer id = 123;
        final Event expectedEvent = TestHelper.createEvent();
        when(mockEventRepository.getOne(id)).thenReturn(expectedEvent);

        final Event event = useCaseTournamentHost.getEvent(id);

        assertThat(event, is(expectedEvent));
    }

    @Test
    public void testGetRoundRobinGrid() {
        final Event event = TestHelper.createEvent();
        final Integer eventId = event.getId();
        when(mockEventRepository.getOne(eventId)).thenReturn(event);

        final List<List<RoundRobinCell>> roundRobinGrid = useCaseTournamentHost.getRoundRobinGrid(eventId);

        assertThat(roundRobinGrid, is(event.getRoundRobinGrid()));
    }

    @Test
    public void testGetMatchResultList() {
        final String challongeUrl = TestHelper.CHALLONGE_URL;
        final Event event = TestHelper.createEvent();
        when(mockEventRepository.getOneByChallongeUrl(challongeUrl)).thenReturn(event);
        // need to override event list because we cannot get result for incomplete
        // matches
        when(event.getMatchRepository().findByChallongeUrl(event.getChallongeUrl()))
            .thenReturn(List.of(
                TestHelper.createMatch1(), TestHelper.createMatch2()
            ));

        final List<MatchResult> matchResultList = useCaseTournamentHost.getMatchResultList(challongeUrl);

        assertThat(matchResultList, is(event.getMatchResultList()));
    }

    @Test
    public void testGetRoundRobinMatchList() {
        final String challongeUrl = TestHelper.CHALLONGE_URL;
        final Event event = TestHelper.createEvent();
        when(mockEventRepository.getOneByChallongeUrl(challongeUrl)).thenReturn(event);

        final List<MatchSheet> roundRobinGrid = useCaseTournamentHost.getRoundRobinMatchList(challongeUrl);

        assertThat(roundRobinGrid, is(event.getMatchSheetList()));
    }
}
