package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.model.Event;
import com.eatsleeppong.ubipong.model.Match;
import org.junit.Test;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An event is represented as a list of matches in challonge.com
 */
public class TestEventManager {
    private EventManager fixture = new EventManager();

    @Test
    public void testGroupByPlayerOneMatch() {
        // given
        Match m1 = new Match();
        m1.setPlayer1Id(1);
        m1.setPlayer2Id(2);

        List<Match> matchList = new ArrayList<>();
        Event event = new Event();
        event.setMatchList(matchList);

        matchList.add(m1);

        // when
        Map<Integer, List<Match>> groupedMatchList = 
            fixture.groupByPlayer(event);

        // then
        assertThat(groupedMatchList, notNullValue());
    }
}
