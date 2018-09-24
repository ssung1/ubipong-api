package com.eatsleeppong.ubipong.model.challonge;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ChallongeParticipant {
    private Integer id;
    private Integer tournamentId;
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
    private String challongeUsername;
    private String challongeEmailAddressVerified;
    private Boolean removable;
    private Boolean participatableOrInvitationAttached;
    private Boolean confirmRemove;
    private Boolean invitationPending;
    private String displayNameWithInvitationEmailAddress;
    private String emailHash;
    private String username;
    private String displayName;
    private String attachedParticipatablePortraitUrl;
    private Boolean canCheckIn;
    private Boolean checkedIn;
    private Boolean reactivatable;
    private Boolean checkInOpen;
    private List<Integer> groupPlayerIds;
    private Boolean hasIrrevelantSeed;
}
