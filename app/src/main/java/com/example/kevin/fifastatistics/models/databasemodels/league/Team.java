package com.example.kevin.fifastatistics.models.databasemodels.league;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team implements Serializable {

    private String name;
    private String shortName;
    private String code;
    private String crestUrl;
    private String color;
}
