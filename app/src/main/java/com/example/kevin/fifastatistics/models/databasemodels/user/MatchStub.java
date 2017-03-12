package com.example.kevin.fifastatistics.models.databasemodels.user;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.models.databasemodels.match.Penalties;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MatchStub extends DatabaseModel {

    private String id;
    private String winnerId;
    private Date date;
    private int goalsWinner;
    private int goalsLoser;
    private Penalties penalties;

}
