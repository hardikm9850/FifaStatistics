package com.example.kevin.fifastatistics.event;

import com.example.kevin.fifastatistics.models.databasemodels.match.CurrentSeries;

import lombok.Getter;

/**
 * Created by kevin on 2017-08-18.
 */

public class SeriesRemovedEvent implements Event {

    @Getter private final String opponentId;

    public SeriesRemovedEvent(String opponentId) {
        this.opponentId = opponentId;
    }
}
