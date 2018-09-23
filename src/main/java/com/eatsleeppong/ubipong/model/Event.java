package com.eatsleeppong.ubipong.model;

import lombok.Data;

import java.util.List;

@Data
public class Event {
    private List<MatchWrapper> matchWrapperList;
}
