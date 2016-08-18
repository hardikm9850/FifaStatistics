package com.example.kevin.fifastatistics.models.databasemodels.user;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.models.databasemodels.user.records.UserRecords;
import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The User class defines a FIFA Statistics user. It contains all of the user's
 * properties and related items, such as name, level, experience, friends, stats,
 * matches, etc. <br>
 * There shall only ever be one instance of a user per googleId (two users
 * cannot be shared by a single Google account).
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE, onConstructor=@__(@JsonCreator))
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class User extends DatabaseModel {

    @Setter private String registrationToken;
    @Setter private int level;
    @Setter private int experience;
    @Setter private String id;

    private String name;
    private String email;
    private String googleId;
    private String imageUrl;

    private List<Friend> friends;
    private List<Friend> incomingRequests;
    private List<Friend> outgoingRequests;
    private List<MatchStub> matches;
    private List<SeriesStub> series;

    private StatsPair recordStats;
    private StatsPair averageStats;
    private UserRecords matchRecords;
    private UserRecords seriesRecords;

    public User(String name, String email, String googleId, String imageUrl) {
        friends = new ArrayList<>();
        incomingRequests = new ArrayList<>();
        outgoingRequests = new ArrayList<>();
        matches = new ArrayList<>();
        series = new ArrayList<>();
        recordStats = new StatsPair();
        averageStats = new StatsPair();
        matchRecords = UserRecords.emptyRecords();
        seriesRecords = UserRecords.emptyRecords();

        this.name = name;
        this.email = email;
        this.googleId = googleId;
        this.imageUrl = imageUrl;
    }

    public void addIncomingRequest(Friend friend) {
        incomingRequests.add(friend);
    }

    public void addOutgoingRequest(Friend friend) {
        outgoingRequests.add(friend);
    }

    public void acceptIncomingRequest(Friend friend) {
        incomingRequests.remove(friend);
        friends.add(friend);
    }

    public void declineIncomingRequest(Friend friend) {
        incomingRequests.remove(friend);
    }

    public void removeOutgoingRequest(Friend friend) {
        outgoingRequests.remove(friend);
    }

    public boolean hasIncomingRequestWithId(String id) {
        return hasIncomingRequestsWithIdInList(id, incomingRequests);
    }

    public boolean hasOutgoingRequestWithId(String id) {
        return hasIncomingRequestsWithIdInList(id, outgoingRequests);
    }

    private boolean hasIncomingRequestsWithIdInList(String id, List<Friend> requestList) {
        for (Friend request : requestList) {
            if (request.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void addFriend(Friend friend) {
        friends.add(friend);
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
    @Getter
    public static class StatsPair {
        private Stats statsFor;
        private Stats statsAgainst;

        public StatsPair() {
            statsFor = new Stats();
            statsAgainst = new Stats();
        }
    }
}
