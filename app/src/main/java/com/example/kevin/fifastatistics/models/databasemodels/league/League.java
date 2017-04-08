package com.example.kevin.fifastatistics.models.databasemodels.league;

import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (name == null || shortName == null) return false;

        League league = (League) o;
        return name.equals(league.name) && shortName.equals(league.shortName);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + shortName.hashCode();
        return result;
    }
}
