package com.example.kevin.fifastatistics.models.databasemodels.user;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The Friend class is meant to act as a 'stub' of a user, and should exist only
 * within instances of a User object.
 */
@JsonDeserialize(builder = Friend.FriendBuilder.class)
@Builder
@Getter
public class Friend extends DatabaseModel implements Player
{
    private final String id;
    private final String name;
    private final String imageUrl;
    private String favoriteTeamId;
    private int level;

    @Setter
    private String registrationToken;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class FriendBuilder {
    }

    public static Friend fromPlayer(Player user) {
        return Friend.builder()
                .id(user.getId())
                .imageUrl(user.getImageUrl())
                .name(user.getName())
                .registrationToken(user.getRegistrationToken())
                .favoriteTeamId(user.getFavoriteTeamId())
                .build();
    }

    @Override
    public String toString() {
        return SerializationUtils.toJson(this);
    }

}
