package com.eatsleeppong.ubipong.model.challonge;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

public class TestChallongeParticipantWrapperList {
    /**
     * challonge api returns a list of participants
     */
    final String eventJson = "[{\"participant\":{\"id\":82304117,\"tournament_id\":5040275,\"name\":\"spongebob\",\"seed\":1,\"active\":true,\"created_at\":\"2018-09-20T22:16:34.745-05:00\",\"updated_at\":\"2018-09-20T22:16:34.745-05:00\",\"invite_email\":null,\"final_rank\":null,\"misc\":null,\"icon\":null,\"on_waiting_list\":false,\"invitation_id\":null,\"group_id\":null,\"checked_in_at\":null,\"ranked_member_id\":null,\"challonge_username\":null,\"challonge_email_address_verified\":null,\"removable\":true,\"participatable_or_invitation_attached\":false,\"confirm_remove\":true,\"invitation_pending\":false,\"display_name_with_invitation_email_address\":\"spongbob\",\"email_hash\":null,\"username\":null,\"display_name\":\"spongbob\",\"attached_participatable_portrait_url\":null,\"can_check_in\":false,\"checked_in\":false,\"reactivatable\":false,\"check_in_open\":false,\"group_player_ids\":[],\"has_irrelevant_seed\":false}},{\"participant\":{\"id\":82304119,\"tournament_id\":5040275,\"name\":\"patrick\",\"seed\":2,\"active\":true,\"created_at\":\"2018-09-20T22:16:39.693-05:00\",\"updated_at\":\"2018-09-20T22:16:39.693-05:00\",\"invite_email\":null,\"final_rank\":null,\"misc\":null,\"icon\":null,\"on_waiting_list\":false,\"invitation_id\":null,\"group_id\":null,\"checked_in_at\":null,\"ranked_member_id\":null,\"challonge_username\":null,\"challonge_email_address_verified\":null,\"removable\":true,\"participatable_or_invitation_attached\":false,\"confirm_remove\":true,\"invitation_pending\":false,\"display_name_with_invitation_email_address\":\"patrick\",\"email_hash\":null,\"username\":null,\"display_name\":\"patrick\",\"attached_participatable_portrait_url\":null,\"can_check_in\":false,\"checked_in\":false,\"reactivatable\":false,\"check_in_open\":false,\"group_player_ids\":[],\"has_irrelevant_seed\":false}},{\"participant\":{\"id\":82304121,\"tournament_id\":5040275,\"name\":\"squidward\",\"seed\":3,\"active\":true,\"created_at\":\"2018-09-20T22:16:44.659-05:00\",\"updated_at\":\"2018-09-20T22:16:44.659-05:00\",\"invite_email\":null,\"final_rank\":null,\"misc\":null,\"icon\":null,\"on_waiting_list\":false,\"invitation_id\":null,\"group_id\":null,\"checked_in_at\":null,\"ranked_member_id\":null,\"challonge_username\":null,\"challonge_email_address_verified\":null,\"removable\":true,\"participatable_or_invitation_attached\":false,\"confirm_remove\":true,\"invitation_pending\":false,\"display_name_with_invitation_email_address\":\"squidward\",\"email_hash\":null,\"username\":null,\"display_name\":\"squidward\",\"attached_participatable_portrait_url\":null,\"can_check_in\":false,\"checked_in\":false,\"reactivatable\":false,\"check_in_open\":false,\"group_player_ids\":[],\"has_irrelevant_seed\":false}},{\"participant\":{\"id\":82383830,\"tournament_id\":5040275,\"name\":\"plankton\",\"seed\":4,\"active\":true,\"created_at\":\"2018-09-22T06:45:58.990-05:00\",\"updated_at\":\"2018-09-22T06:45:58.990-05:00\",\"invite_email\":null,\"final_rank\":null,\"misc\":null,\"icon\":null,\"on_waiting_list\":false,\"invitation_id\":null,\"group_id\":null,\"checked_in_at\":null,\"ranked_member_id\":null,\"challonge_username\":null,\"challonge_email_address_verified\":null,\"removable\":true,\"participatable_or_invitation_attached\":false,\"confirm_remove\":true,\"invitation_pending\":false,\"display_name_with_invitation_email_address\":\"plankton\",\"email_hash\":null,\"username\":null,\"display_name\":\"plankton\",\"attached_participatable_portrait_url\":null,\"can_check_in\":false,\"checked_in\":false,\"reactivatable\":false,\"check_in_open\":false,\"group_player_ids\":[],\"has_irrelevant_seed\":false}}]";

    @Test
    public void testDeserializeParticipantWrapperList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<ChallongeParticipantWrapper> list = mapper.readValue(eventJson,
            new TypeReference<List<ChallongeParticipantWrapper>>(){});

        assertThat(list, hasSize(4));

        ChallongeParticipant firstParticipant;
        firstParticipant = list.get(0).getParticipant();

        assertThat(firstParticipant.getId(), is(82304117));
        assertThat(firstParticipant.getTournamentId(), is(5040275));
    }

    @Test
    public void testSerializeParticipantWrapperList() throws Exception {
        ChallongeParticipantWrapper pw = new ChallongeParticipantWrapper();
        ChallongeParticipant p = new ChallongeParticipant();

        pw.setParticipant(p);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(p);

        assertThat(json, containsString("tournament_id"));
        assertThat(json, containsString("\"id\":"));
    }
}
