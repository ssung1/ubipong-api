package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.model.Event;
import com.eatsleeppong.ubipong.model.Match;
import com.eatsleeppong.ubipong.model.MatchWrapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * An event is represented as a list of matches in challonge.com
 */
public class TestEventManager {
    private TestEventManager fixture = new TestEventManager();

    @Test
    public void getPlayerListOneMatch() {
        Match m1 = new Match();
        m1.setPlayer1Id(1);
        m1.setPlayer2Id(2);

        MatchWrapper mw1 = new MatchWrapper();
        mw1.setMatch(m1);

        List<MatchWrapper> matchWrapperList =
            new ArrayList<>();
        Event event = new Event();
        event.setMatchWrapperList(matchWrapperList);

        matchWrapperList.add(mw1);
    }
}
