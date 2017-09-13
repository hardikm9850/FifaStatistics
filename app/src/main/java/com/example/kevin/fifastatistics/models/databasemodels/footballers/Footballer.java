package com.example.kevin.fifastatistics.models.databasemodels.footballers;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Footballer extends DatabaseModel {

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

    @Getter
    @Setter
    public static class PlayerStats implements Serializable {
        int goals;
        int goldenGoals;
        int yellowCards;
        int redCards;
        int injuries;
    }
}