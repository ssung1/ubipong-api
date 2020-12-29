package com.eatsleeppong.ubipong.model.challonge;

import lombok.Data;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ChallongeParticipant {
    private Integer id;
    private Integer tournamentId;
    /**
     * if user does NOT have a challonge account, this field would be valid
     */
    private String name;
    private Integer seed;
    private Boolean active;
    private Date createdAt;
    private Date updatedAt;
    private String inviteEmail;
    private String finalRank;
    private String misc;
    private String icon;
    private Boolean onWaitingList;
    private Integer invitationId;
    private Integer groupId;
    private Date checkedInAt;
    private Integer rankedMemberId;
    /**
     * if user has a challonge account, this field would be valid
     */
    private String challongeUsername;
    private String challongeEmailAddressVerified;
    private Boolean removable;
    private Boolean participatableOrInvitationAttached;
    private Boolean confirmRemove;
    private Boolean invitationPending;
    private String displayNameWithInvitationEmailAddress;
    private String emailHash;
    /**
     * for all practical purposes, this is the same as challongeUsername
     */
    private String username;
    /**
     * this is probably the most reliable field to get some kind of name; this is either the same as "name"
     * or "username", whichever is valid
     */
    private String displayName;
    private String attachedParticipatablePortraitUrl;
    private Boolean canCheckIn;
    private Boolean checkedIn;
    private Boolean reactivatable;
    private Boolean checkInOpen;
    private List<Integer> groupPlayerIds;
    private Boolean hasIrrelevantSeed;
}
