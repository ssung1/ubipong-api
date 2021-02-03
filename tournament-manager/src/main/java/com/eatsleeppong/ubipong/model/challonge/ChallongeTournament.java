package com.eatsleeppong.ubipong.model.challonge;

import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChallongeTournament {
    private Integer id;
    private String name;
    private String url;
    private String description;
    /**
     * If creating a tournament in an organization, include subdomain
     *
     * To retrieve it later, use api.challonge.com/v1/tournaments/subdomain-url.json.  This is necessary because
     * when using the browser, we can just do subdomain.challonge.com/url, but for APIs, we have to use
     * api.challonge.com
     */
    private String subdomain;
    /**
     * "single elimination", "round robin"
     */
    private String tournamentType;
    /**
     * "Table Tennis"
     */
    private String gameName;

    /**
     * "pending": not started; cannot get list of matches
     * "underway": started but not finished; can get list of matches but not all will have results
     * "awaiting_review": all results are in, but not fully finalized
     * "complete": all results are in, and user hit "end tournament" button
     */
    private String state;
}
