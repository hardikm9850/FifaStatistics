package com.example.kevin.fifastatistics.models.databasemodels.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchUpdateResponse {

    private String message;
}

