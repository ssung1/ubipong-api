package com.eatsleeppong.ubipong.tournamentmanager.controller.mapper;

import com.eatsleeppong.ubipong.model.challonge.ChallongeParticipant;
import com.eatsleeppong.ubipong.model.challonge.ChallongeParticipantWrapper;
import com.eatsleeppong.ubipong.tournamentmanager.domain.Player;

import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {
    public Player mapChallongeParticipantWrapperToPlayer(ChallongeParticipantWrapper challongeParticipantWrapper) {
        return mapChallongeParticipantToPlayer(challongeParticipantWrapper.getParticipant());
    }

    public Player mapChallongeParticipantToPlayer(ChallongeParticipant challongeParticipant) {
        return Player.builder()
            .id(challongeParticipant.getId())
            .name(challongeParticipant.getDisplayName())
            .build();
    }
}