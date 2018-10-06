package com.eatsleeppong.ubipong.manager;

import com.eatsleeppong.ubipong.model.Event;
import com.eatsleeppong.ubipong.model.challonge.ChallongeMatch;
import com.eatsleeppong.ubipong.model.challonge.ChallongeMatchWrapper;

import org.junit.Test;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * An event is represented as a list of matches in challonge.com
 */
public class TestEventManager {
    private EventManager subject = new EventManager();

    @Test
    public void testUnwrapChallongeMatchWrapperArray() {
        Integer spongebob = 123;
        Integer patrick = 234;
        Integer squidward = 345;

        ChallongeMatch m1 = new ChallongeMatch();
        m1.setPlayer1Id(spongebob);
        m1.setPlayer2Id(patrick);

        ChallongeMatchWrapper mw1 = new ChallongeMatchWrapper();
        mw1.setChallongeMatch(m1);

        ChallongeMatch m2 = new ChallongeMatch();
        m2.setPlayer1Id(patrick);
        m2.setPlayer2Id(squidward);

        ChallongeMatchWrapper mw2 = new ChallongeMatchWrapper();
        mw2.setChallongeMatch(m2);

        ChallongeMatch m3 = new ChallongeMatch();
        m3.setPlayer1Id(squidward);
        m3.setPlayer2Id(spongebob);
        ChallongeMatchWrapper mw3 = new ChallongeMatchWrapper();
        mw3.setChallongeMatch(m3);

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
        Integer spongebob = 123;
        Integer patrick = 234;
        Integer squidward = 345;

        ChallongeMatch m1 = new ChallongeMatch();
        m1.setPlayer1Id(spongebob);
        m1.setPlayer2Id(patrick);

        ChallongeMatch m2 = new ChallongeMatch();
        m2.setPlayer1Id(patrick);
        m2.setPlayer2Id(squidward);

        ChallongeMatch m3 = new ChallongeMatch();
        m3.setPlayer1Id(squidward);
        m3.setPlayer2Id(spongebob);

        List<ChallongeMatch> matchList = Arrays.asList(m1, m2, m3);
        List<ChallongeMatch> spongebobMatchList =
            subject.findByPlayer1(matchList, spongebob);

        assertThat(spongebobMatchList, hasItem(m1));
    }
}
