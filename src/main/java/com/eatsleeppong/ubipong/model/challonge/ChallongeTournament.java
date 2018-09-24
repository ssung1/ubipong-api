package com.eatsleeppong.ubipong.model.challonge;

import lombok.Data;

import java.util.List;

@Data
public class ChallongeTournament {
    private List<ChallongeMatchWrapper> challongeMatchWrapperList;
}
