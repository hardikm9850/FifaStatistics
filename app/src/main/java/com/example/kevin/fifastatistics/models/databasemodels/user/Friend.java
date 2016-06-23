package com.example.kevin.fifastatistics.models.databasemodels.user;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
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
public class Friend extends DatabaseModel
{
    private String id;
    private String name;
    private String imageUrl;
    private int level;

    @Setter
    private String registrationToken;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class FriendBuilder {
    }
}
