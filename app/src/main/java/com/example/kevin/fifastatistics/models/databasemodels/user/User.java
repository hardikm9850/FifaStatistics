package com.example.kevin.fifastatistics.models.databasemodels.user;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Result;
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
    private RecentEventList<MatchStub> matches;
    private RecentEventList<SeriesStub> series;

    private StatsPair recordStats;
    private StatsPair averageStats;
    private UserRecords matchRecords;
    private UserRecords seriesRecords;

    public User(String name, String email, String googleId, String imageUrl) {
        friends = new ArrayList<>();
        incomingRequests = new ArrayList<>();
        outgoingRequests = new ArrayList<>();
        matches = new RecentEventList<>();
        series = new RecentEventList<>();
        recordStats = new StatsPair();
        averageStats = new StatsPair();
        matchRecords = UserRecords.emptyRecords();
        seriesRecords = UserRecords.emptyRecords();

        this.name = name;
        this.email = email;
        this.googleId = googleId;
        this.imageUrl = imageUrl;
    }

    public static User fromFriend(Friend friend) {
        User user = new User(friend.getName(), "", "", friend.getImageUrl());
        user.registrationToken = friend.getRegistrationToken();
        user.id = friend.getId();
        user.level = friend.getLevel();
        return user;
    }

    public void addMatch(Match match) {
        boolean didWin = match.getWinner().getId().equals(id);
        matchRecords.addResult(didWin ? Result.WIN : Result.LOSS);

        Stats statsFor = didWin ? match.getStats().getStatsFor() : match.getStats().getStatsAgainst();
        Stats statsAgainst = didWin ? match.getStats().getStatsAgainst() : match.getStats().getStatsFor();
        int totalMatches = matchRecords.getTotalCount();
        averageStats.updateAverages(statsFor, statsAgainst, totalMatches);
        recordStats.updateRecords(statsFor, statsAgainst);
        matches.add(MatchStub.fromMatch(match));
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
            if (friend.getId().equals(id)) {
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

        public void updateAverages(Stats statsFor, Stats statsAgainst, int totalMatches) {
            statsFor.updateAverages(statsFor, totalMatches);
            statsAgainst.updateAverages(statsAgainst, totalMatches);
        }

        public void updateRecords(Stats statsFor, Stats statsAgainst) {
            statsFor.updateRecords(statsFor);
            statsAgainst.updateRecords(statsAgainst);
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
