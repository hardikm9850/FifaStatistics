package com.example.kevin.fifastatistics.event;

import lombok.Getter;

public class SeriesRemovedEvent implements Event {

    @Getter private final String opponentId;

    public SeriesRemovedEvent(String opponentId) {
        this.opponentId = opponentId;
    }
}
