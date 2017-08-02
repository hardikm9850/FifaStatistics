package com.example.kevin.fifastatistics.models.databasemodels.match;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentSeries extends DatabaseModel {

    private String id;
    private String creatorId;
    private Friend opponent;
    private List<Match> matches;

    public CurrentSeries(String creatorId, Friend opponent, List<Match> matches) {
        this();
        this.creatorId = creatorId;
        this.opponent = opponent;
        this.matches = matches;
    }

    @JsonCreator
    public CurrentSeries() {
        matches = new ArrayList<>();
    }

    @JsonIgnore
    public String getOpponentId() {
        return opponent != null ? opponent.getId() : null;
    }
}
