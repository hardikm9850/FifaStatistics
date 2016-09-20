package com.example.kevin.fifastatistics.models.databasemodels.match;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;

@JsonDeserialize(builder = Match.MatchBuilder.class)
@Builder
@Getter
public class Match extends DatabaseModel {

    private String id;
    private final User.StatsPair stats;
    private final Penalties penalties;
    private final Date date;

    @JsonDeserialize(as=Friend.class) private final Player winner;
    @JsonDeserialize(as=Friend.class) private final Player loser;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class MatchBuilder {
    }

    public static Match fromMatchWithId(Match match, String id) {
        match.id = id;
        return match;
    }

    @Override
    public String toString() {
        return SerializationUtils.toJson(this);
    }
}
