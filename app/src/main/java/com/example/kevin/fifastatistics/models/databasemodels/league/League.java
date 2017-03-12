package com.example.kevin.fifastatistics.models.databasemodels.league;

import com.example.kevin.fifastatistics.models.apiresponses.ApiListResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class League implements Serializable {

    private String name;
    private String shortName;

    @JsonProperty("_links")
    private ApiListResponse.Links links;

    @JsonIgnore
    public String getTeamUrl() {
        return links != null ? (links.getTeams() != null ? links.getTeams().getHref() : null) : null;
    }
}
