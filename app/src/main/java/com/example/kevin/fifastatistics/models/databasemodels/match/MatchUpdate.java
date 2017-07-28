package com.example.kevin.fifastatistics.models.databasemodels.match;

import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.utils.NetworkUtils;
import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchUpdate extends DatabaseModel {

    private String id;
    private String matchId;
    private String creatorId;
    private String receiverId;
    private String message;
    private String summary;
    private Updates updates;

    @JsonProperty("_links")
    private ApiListResponse.Links links;

    public String getId() {
        if (id == null && links != null && links.getSelf() != null) {
            id = NetworkUtils.getIdFromUrl(links.getSelf().getHref());
        }
        return id;
    }

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

    @JsonIgnore
    public boolean hasUpdates() {
        return (updates.statsAgainstUpdates != null && updates.statsAgainstUpdates.size() > 0) ||
                (updates.statsForUpdates != null && updates.statsForUpdates.size() > 0) ||
                (updates.penaltiesUpdates != null && updates.penaltiesUpdates.isUpdated());
    }

    @Getter
    @Setter
    public static class Updates implements Serializable {

        /**
         * "possession" : 42
         * "offsides" : 3
         */
        private Map<String, Integer> statsForUpdates;
        private Map<String, Integer> statsAgainstUpdates;
        private PenaltiesUpdates penaltiesUpdates;

        @Override
        public String toString() {
            return SerializationUtils.toFormattedJson(this);
        }
    }

    @Getter
    @Setter
    public static class PenaltiesUpdates implements Serializable {
        private Integer winner;
        private Integer loser;

        @Override
        public String toString() {
            return SerializationUtils.toFormattedJson(this);
        }

        @JsonIgnore
        private boolean isUpdated() {
            return winner != null || loser != null;
        }
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

        public Builder receiverId(String receiverId) {
            update.receiverId = receiverId;
            return this;
        }

        public Builder id(String id) {
            update.id = id;
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

            public UpdatesBuilder possession(int possession) {
                return set("possession", possession);
            }

            public UpdatesBuilder tackles(int tackles) {
                return set("tackles", tackles);
            }

            public UpdatesBuilder fouls(int fouls) {
                return set("fouls", fouls);
            }

            public UpdatesBuilder yellowCards(int yellowCards) {
                return set("yellowCards", yellowCards);
            }

            public UpdatesBuilder redCards(int redCards) {
                return set("redCards", redCards);
            }

            public UpdatesBuilder injuries(int injuries) {
                return set("injuries", injuries);
            }

            public UpdatesBuilder offsides(int offsides) {
                return set("offsides", offsides);
            }

            public UpdatesBuilder corners(int corners) {
                return set("corners", corners);
            }

            public UpdatesBuilder shotAccuracy(int shotAccuracy) {
                return set("shotAccuracy", shotAccuracy);
            }

            public UpdatesBuilder passAccuracy(int passAccuracy) {
                return set("passAccuracy", passAccuracy);
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

    @Override
    public String toString() {
        return SerializationUtils.toFormattedJson(this);
    }
}
