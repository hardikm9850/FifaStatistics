package com.example.kevin.fifastatistics.models;

import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;

import java.util.List;

import lombok.Getter;

@Getter
public class FutApiResponse {

    private int page;
    private int totalPages;
    private int totalResults;
    private int count;
    private String type;
    private List<Footballer> items;
}
