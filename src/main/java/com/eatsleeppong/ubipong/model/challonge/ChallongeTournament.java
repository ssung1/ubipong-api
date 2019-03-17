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
}
