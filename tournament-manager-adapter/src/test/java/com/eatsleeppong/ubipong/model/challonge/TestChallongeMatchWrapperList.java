package com.eatsleeppong.ubipong.model.challonge;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;

public class TestChallongeMatchWrapperList {
    /**
     * challonge api returns a list of matches
     */
    final String json = "[{\"match\":{\"id\":134553303,\"tournament_id\":5040275,\"state\":\"complete\",\"player1_id\":82304119,\"player2_id\":82304121,\"player1_prereq_match_id\":null,\"player2_prereq_match_id\":null,\"player1_is_prereq_match_loser\":false,\"player2_is_prereq_match_loser\":false,\"winner_id\":82304119,\"loser_id\":82304121,\"started_at\":\"2018-09-22T07:46:27.289-04:00\",\"created_at\":\"2018-09-22T07:46:26.945-04:00\",\"updated_at\":\"2018-09-22T07:46:49.299-04:00\",\"identifier\":\"A\",\"has_attachment\":false,\"round\":1,\"player1_votes\":null,\"player2_votes\":null,\"group_id\":null,\"attachment_count\":null,\"scheduled_time\":null,\"location\":null,\"underway_at\":null,\"optional\":null,\"rushb_id\":null,\"completed_at\":\"2018-09-22T07:46:49.308-04:00\",\"suggested_play_order\":1,\"forfeited\":null,\"prerequisite_match_ids_csv\":\"\",\"scores_csv\":\"11-4,11-5,11-6\"}},{\"match\":{\"id\":134553304,\"tournament_id\":5040275,\"state\":\"open\",\"player1_id\":82383830,\"player2_id\":82304117,\"player1_prereq_match_id\":null,\"player2_prereq_match_id\":null,\"player1_is_prereq_match_loser\":false,\"player2_is_prereq_match_loser\":false,\"winner_id\":null,\"loser_id\":null,\"started_at\":\"2018-09-22T07:46:27.313-04:00\",\"created_at\":\"2018-09-22T07:46:26.955-04:00\",\"updated_at\":\"2018-09-22T07:46:27.313-04:00\",\"identifier\":\"B\",\"has_attachment\":false,\"round\":1,\"player1_votes\":null,\"player2_votes\":null,\"group_id\":null,\"attachment_count\":null,\"scheduled_time\":null,\"location\":null,\"underway_at\":null,\"optional\":null,\"rushb_id\":null,\"completed_at\":null,\"suggested_play_order\":2,\"forfeited\":null,\"prerequisite_match_ids_csv\":\"\",\"scores_csv\":\"\"}},{\"match\":{\"id\":134553305,\"tournament_id\":5040275,\"state\":\"open\",\"player1_id\":82304117,\"player2_id\":82304119,\"player1_prereq_match_id\":null,\"player2_prereq_match_id\":null,\"player1_is_prereq_match_loser\":false,\"player2_is_prereq_match_loser\":false,\"winner_id\":null,\"loser_id\":null,\"started_at\":\"2018-09-22T07:46:27.335-04:00\",\"created_at\":\"2018-09-22T07:46:26.961-04:00\",\"updated_at\":\"2018-09-22T07:46:27.336-04:00\",\"identifier\":\"C\",\"has_attachment\":false,\"round\":2,\"player1_votes\":null,\"player2_votes\":null,\"group_id\":null,\"attachment_count\":null,\"scheduled_time\":null,\"location\":null,\"underway_at\":null,\"optional\":null,\"rushb_id\":null,\"completed_at\":null,\"suggested_play_order\":3,\"forfeited\":null,\"prerequisite_match_ids_csv\":\"\",\"scores_csv\":\"\"}},{\"match\":{\"id\":134553306,\"tournament_id\":5040275,\"state\":\"open\",\"player1_id\":82304121,\"player2_id\":82383830,\"player1_prereq_match_id\":null,\"player2_prereq_match_id\":null,\"player1_is_prereq_match_loser\":false,\"player2_is_prereq_match_loser\":false,\"winner_id\":null,\"loser_id\":null,\"started_at\":\"2018-09-22T07:46:27.352-04:00\",\"created_at\":\"2018-09-22T07:46:26.969-04:00\",\"updated_at\":\"2018-09-22T07:46:27.353-04:00\",\"identifier\":\"D\",\"has_attachment\":false,\"round\":2,\"player1_votes\":null,\"player2_votes\":null,\"group_id\":null,\"attachment_count\":null,\"scheduled_time\":null,\"location\":null,\"underway_at\":null,\"optional\":null,\"rushb_id\":null,\"completed_at\":null,\"suggested_play_order\":4,\"forfeited\":null,\"prerequisite_match_ids_csv\":\"\",\"scores_csv\":\"\"}},{\"match\":{\"id\":134553307,\"tournament_id\":5040275,\"state\":\"open\",\"player1_id\":82383830,\"player2_id\":82304119,\"player1_prereq_match_id\":null,\"player2_prereq_match_id\":null,\"player1_is_prereq_match_loser\":false,\"player2_is_prereq_match_loser\":false,\"winner_id\":null,\"loser_id\":null,\"started_at\":\"2018-09-22T07:46:27.375-04:00\",\"created_at\":\"2018-09-22T07:46:26.975-04:00\",\"updated_at\":\"2018-09-22T07:46:27.375-04:00\",\"identifier\":\"E\",\"has_attachment\":false,\"round\":3,\"player1_votes\":null,\"player2_votes\":null,\"group_id\":null,\"attachment_count\":null,\"scheduled_time\":null,\"location\":null,\"underway_at\":null,\"optional\":null,\"rushb_id\":null,\"completed_at\":null,\"suggested_play_order\":5,\"forfeited\":null,\"prerequisite_match_ids_csv\":\"\",\"scores_csv\":\"\"}},{\"match\":{\"id\":134553308,\"tournament_id\":5040275,\"state\":\"open\",\"player1_id\":82304121,\"player2_id\":82304117,\"player1_prereq_match_id\":null,\"player2_prereq_match_id\":null,\"player1_is_prereq_match_loser\":false,\"player2_is_prereq_match_loser\":false,\"winner_id\":null,\"loser_id\":null,\"started_at\":\"2018-09-22T07:46:27.404-04:00\",\"created_at\":\"2018-09-22T07:46:26.981-04:00\",\"updated_at\":\"2018-09-22T07:46:27.404-04:00\",\"identifier\":\"F\",\"has_attachment\":false,\"round\":3,\"player1_votes\":null,\"player2_votes\":null,\"group_id\":null,\"attachment_count\":null,\"scheduled_time\":null,\"location\":null,\"underway_at\":null,\"optional\":null,\"rushb_id\":null,\"completed_at\":null,\"suggested_play_order\":6,\"forfeited\":null,\"prerequisite_match_ids_csv\":\"\",\"scores_csv\":\"\"}}]\n";

    @Test
    public void testDeserializeMatchWrapperList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<ChallongeMatchWrapper> list = mapper.readValue(json,
            new TypeReference<List<ChallongeMatchWrapper>>(){});

        assertThat(list, hasSize(6));
        ChallongeMatch firstMatch = list.get(0).getMatch();

        assertThat(firstMatch.getId(), is(134553303));
        assertThat(firstMatch.getTournamentId(), is(5040275));
    }
}
