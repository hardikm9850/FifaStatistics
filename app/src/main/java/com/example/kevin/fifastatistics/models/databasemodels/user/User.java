package com.example.kevin.fifastatistics.models.databasemodels.user;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.models.databasemodels.user.records.UserRecords;
import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE, onConstructor=@__(@JsonCreator))
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class User extends DatabaseModel implements Player {

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

    public static User fromFriend(Friend friend) {
        User user = new User();
        user.name = friend.getName();
        user.imageUrl = friend.getImageUrl();
        user.registrationToken = friend.getRegistrationToken();
        user.id = friend.getId();
        user.level = friend.getLevel();
        return user;
    }

    public User(String name, String email, String googleId, String imageUrl) {
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
        return hasFriendWithIdInList(id, incomingRequests);
    }

    public boolean hasOutgoingRequestWithId(String id) {
        return hasFriendWithIdInList(id, outgoingRequests);
    }

    public boolean hasFriendWithId(String id) {
        return hasFriendWithIdInList(id, friends);
    }

    private boolean hasFriendWithIdInList(String id, List<Friend> friendList) {
        for (Friend friend : friendList) {
            if (friend.getId() != null && friend.getId().equals(id)) {
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
    public static class StatsPair implements Serializable {
        private Stats statsFor;
        private Stats statsAgainst;

        public StatsPair() {
            statsFor = new Stats();
            statsAgainst = new Stats();
        }

        public boolean validate() {
            return (statsFor.validate() && statsAgainst.validate());
        }
        
        public void swap() {
            Stats temp = statsFor;
            statsFor = statsAgainst;
            statsAgainst = temp;
        }
    }
}
