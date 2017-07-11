package com.example.kevin.fifastatistics.models.databasemodels.match;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchUpdate extends DatabaseModel {

    private String id;
    private String matchId;
    private String creatorId;
    private String message;
    private Updates updates;

    public MatchUpdate() {
        this.updates = new Updates();
    }

    @JsonIgnore
    public Integer getStatFor(String key) {
        if (updates.statsForUpdates != null) {
            return updates.statsForUpdates.get(key);
        } else {
            return null;
        }
    }

    @JsonIgnore
    public Integer getStatAgainst(String key) {
        if (updates.statsAgainstUpdates != null) {
            return updates.statsAgainstUpdates.get(key);
        } else {
            return null;
        }
    }

    @Getter
    @Setter
    public static class Updates {

        /**
         * "possession" : 42
         * "offsides" : 3
         */
        private Map<String, Integer> statsForUpdates;
        private Map<String, Integer> statsAgainstUpdates;
        private PenaltiesUpdates penaltiesUpdates;
    }

    @Getter
    @Setter
    public static class PenaltiesUpdates {
        private Integer winner;
        private Integer loser;
    }

    public static class Builder {

        public static final int REMOVE_VAL = -1;

        private MatchUpdate update;

        public Builder(MatchUpdate update) {
            this();
            if (update != null) {
                this.update = update;
            }
        }

        public Builder() {
            update = new MatchUpdate();
        }

        public Builder matchId(String matchId) {
            update.matchId = matchId;
            return this;
        }

        public Builder creatorId(String creatorId) {
            update.creatorId = creatorId;
            return this;
        }

        public UpdatesBuilder statsFor() {
            if (update.updates.statsForUpdates == null) {
                update.updates.statsForUpdates = new HashMap<>();
            }
            return new UpdatesBuilder(update.updates.statsForUpdates);
        }

        public UpdatesBuilder statsAgainst() {
            if (update.updates.statsAgainstUpdates == null) {
                update.updates.statsAgainstUpdates = new HashMap<>();
            }
            return new UpdatesBuilder(update.updates.statsAgainstUpdates);
        }

        public MatchUpdate build() {
            return update;
        }

        public class UpdatesBuilder {

            private Map<String, Integer> updates;

            private UpdatesBuilder(Map<String, Integer> updates) {
                this.updates = updates;
            }

            public UpdatesBuilder goals(int goals) {
                return set("goals", goals);
            }

            public UpdatesBuilder shots(int shots) {
                return set("shots", shots);
            }

            public UpdatesBuilder shotsOnTarget(int shots) {
                return set("shotsOnTarget", shots);
            }

            public UpdatesBuilder injuries(int injuries) {
                return set("injuries", injuries);
            }

            public UpdatesBuilder offsides(int offsides) {
                return set("offsides", offsides);
            }

            private UpdatesBuilder set(String name, int val) {
                if (val == REMOVE_VAL) {
                    updates.remove(name);
                } else {
                    updates.put(name, val);
                }
                return this;
            }

            public Builder and() {
                return Builder.this;
            }

            public MatchUpdate build() {
                return update;
            }
        }
    }
}
