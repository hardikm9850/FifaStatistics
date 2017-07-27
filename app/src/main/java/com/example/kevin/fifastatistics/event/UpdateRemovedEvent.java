package com.example.kevin.fifastatistics.event;

import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;

import lombok.Getter;

public class UpdateRemovedEvent implements Event {

    @Getter private final MatchUpdate update;

    public UpdateRemovedEvent(MatchUpdate update) {
        this.update = update;
    }
}
