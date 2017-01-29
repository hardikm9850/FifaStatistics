package com.example.kevin.fifastatistics.models.databasemodels.user;

import com.example.kevin.fifastatistics.models.databasemodels.match.Penalties;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MatchStub {

    private String id;
    private String winnerId;
    private Date date;
    private int goalsWinner;
    private int goalsLoser;
    private Penalties penalties;

}
