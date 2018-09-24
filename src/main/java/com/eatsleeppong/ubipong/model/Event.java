package com.eatsleeppong.ubipong.model;

import lombok.Data;

import java.util.List;

import com.eatsleeppong.ubipong.model.challonge.ChallongeMatch;

@Data
public class Event {
    private List<ChallongeMatch> challongeMatchList;
}
