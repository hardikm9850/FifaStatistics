package com.example.kevin.fifastatistics.models.databasemodels.footballers;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Leaders implements Serializable {

    private LeaderSet leadersFor;
    private LeaderSet leadersAgainst;

    @Getter
    @Setter
    public static class LeaderSet implements Serializable {
        private Leader goals;
        private Leader injuries;
        private Leader yellowCards;
        private Leader redCards;
    }

    @Getter
    @Setter
    public static class Leader implements Serializable {
        private MatchEvents.DummyPlayer player;
        private int value;
    }
}
