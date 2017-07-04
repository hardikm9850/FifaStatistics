package com.example.kevin.fifastatistics.models.databasemodels.match;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchUpdate extends DatabaseModel {

    private String id;
    private String matchId;
    private String creatorId;
    private String message;
    private Updates updates;

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

        private MatchUpdate update = new MatchUpdate();

        public Builder() {}

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

            public UpdatesBuilder injuries(int injuries) {
                updates.put("injuries", injuries);
                return this;
            }

            public UpdatesBuilder offsides(int offsides) {
                updates.put("offsides", offsides);
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
