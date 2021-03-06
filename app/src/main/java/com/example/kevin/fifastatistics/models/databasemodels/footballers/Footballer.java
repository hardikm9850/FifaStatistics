package com.example.kevin.fifastatistics.models.databasemodels.footballers;

import com.example.kevin.fifastatistics.interfaces.Searchable;
import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Map;
import java.util.StringTokenizer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Footballer extends DatabaseModel implements Searchable {

    private String id;
    private String headshotImgUrl;
    private String commonName;
    private String firstName;
    private String lastName;
    private String name;
    private PlayerStats totalStats;
    private Map<String, PlayerStats> userStatsFor;
    private Map<String, PlayerStats> userStatsAgainst;
    private int baseId;

    @JsonIgnore
    @Override
    public String getSearchString() {
        return name;
    }

    @JsonIgnore
    @Override
    public String getSecondarySearchString() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return SerializationUtils.toFormattedJson(this);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class PlayerStats implements Serializable {
        int goals;
        int goldenGoals;
        int ownGoals;
        int yellowCards;
        int redCards;
        int injuries;
    }
}
