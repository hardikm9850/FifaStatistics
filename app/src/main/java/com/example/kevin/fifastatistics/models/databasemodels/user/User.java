package com.example.kevin.fifastatistics.models.databasemodels.user;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.models.databasemodels.user.records.UserRecords;
import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <b>Class:</b> User <br><br>
 * <b>Description:</b> <br>
 * The User class defines a FIFA Statistics user. It contains all of the user's
 * properties and related items, such as name, level, experience, friends, stats,
 * matches, etc. <br>
 * There shall only ever be one instance of a user per googleId (two users
 * cannot be shared by a single Google account).
 */
@JsonDeserialize(builder = User.UserBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Getter
public class User extends DatabaseModel {

    @Setter private String registrationToken;
    @Setter private String imageUrl;
    @Setter private int level;
    @Setter private int experience;

    private String id;
    private String name;
    private String email;
    private String googleId;
    private ArrayList<Friend> friends;
    private ArrayList<Friend> incomingRequests;
    private ArrayList<Friend> outgoingRequests;
    private ArrayList<MatchStub> matches;
    private ArrayList<SeriesStub> series;
    private StatsPair recordStats;
    private StatsPair averageStats;
    private UserRecords matchRecords;
    private UserRecords seriesRecords;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPOJOBuilder(withPrefix = "")
    public static final class UserBuilder {
    }

    public void addIncomingRequest(Friend friend) {
        incomingRequests = addFriendToList(friend, incomingRequests);
    }

    public void addOutgoingRequest(Friend friend) {
        outgoingRequests = addFriendToList(friend, outgoingRequests);
    }

    public void addFriend(Friend friend) {
        friends = addFriendToList(friend, friends);
    }

    private ArrayList<Friend> addFriendToList(Friend friend, ArrayList<Friend> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(friend);
        return list;
    }

    public void deleteIncomingRequests()
    {
        if (incomingRequests != null) {
            incomingRequests.clear();
        }
    }

    public void deleteFriends()
    {
        if (friends != null) {
            friends.clear();
        }
    }

    /**
     * Serializes the user to JSON format.
     */
    @Override
    public String toString() {
        return SerializationUtils.toJson(this);
    }

    /**
     * A pair of Stats objects, one representing stats 'for' the user, and one representing stats
     * 'against' the user.
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class StatsPair {
        private Stats statsFor;
        private Stats statsAgainst;
    }
}
