package com.eatsleeppong.ubipong.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class RoundRobinCell {
    public static final int TYPE_NAME = 10;
    public static final int TYPE_EMPTY = 11;
    public static final int TYPE_MATCH_COMPLETE = 12;
    public static final int TYPE_MATCH_INCOMPLETE = 13;
    public static final int TYPE_TEXT = 14;

    private int type = TYPE_EMPTY;
    private String content = "";
    private boolean winForPlayer1;
    private boolean winByDefault;
    private List<Game> gameList = Collections.emptyList();
}
