package com.example.kevin.fifastatistics.models.databasemodels.footballers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Leaders implements Serializable {

    private LeaderSet leadersFor;
    private LeaderSet leadersAgainst;

    public static Leaders empty() {
        Leaders leaders = new Leaders();
        leaders.leadersFor = new LeaderSet();
        leaders.leadersAgainst = new LeaderSet();
        return leaders;
    }

    public static Leaders swapped(Leaders leaders) {
        Leaders l = new Leaders();
        l.leadersFor = leaders.leadersAgainst;
        l.leadersAgainst = leaders.leadersFor;
        return l;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return leadersAgainst.isEmpty() && leadersFor.isEmpty();
    }

    @Getter
    @Setter
    public static class LeaderSet implements Serializable {
        private Leader goals;
        private Leader injuries;
        private Leader yellowCards;
        private Leader redCards;

        public void initNullLeadersWith(Leader leader) {
            if (goals == null) goals = leader;
            if (injuries == null) injuries = leader;
            if (yellowCards == null) yellowCards = leader;
            if (redCards == null) redCards = leader;
        }

        @JsonIgnore
        public boolean isEmpty() {
            return goals == null && injuries == null && yellowCards == null && redCards == null;
        }
    }

    @AllArgsConstructor
    @Setter
    public static class Leader implements Serializable {
        private MatchEvents.DummyPlayer player;
        private int value;

        @JsonCreator
        public Leader() {}

        public MatchEvents.DummyPlayer getPlayer() {
            return player;
        }

        public int getValue() {
            return value;
        }

        @JsonIgnore
        public String getValueString() {
            return String.valueOf(value);
        }
    }
}
