package com.eatsleeppong.ubipong.repo;

import com.eatsleeppong.ubipong.model.challonge.ChallongeParticipantWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration-test")
public class TestIntChallongeParticipantRepository {
    private final String tournament = "integration_test_rr";

    @Autowired
    private ChallongeParticipantRepository subject;

    @Test
    public void testGetParticipantList() {
        List<String> nameList = Arrays
            .stream(subject.getParticipantList(tournament))
            .map(p -> p.getParticipant().getName())
            .collect(Collectors.toList());

        assertThat(nameList, hasItem("spongebob"));
        assertThat(nameList, hasItem("patrick"));
        assertThat(nameList, hasItem("squidward"));
        assertThat(nameList, hasItem("plankton"));
    }

    @Test
    public void testGetParticipantListFull() {
        final ChallongeParticipantWrapper[] challongeParticipantWrapperArray = subject.getParticipantList(tournament);

        assertThat(challongeParticipantWrapperArray.length, is(4));
    }
}
