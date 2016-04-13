package com.example.kevin.fifastatistics.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * <b>Class:</b> User <br><br>
 * <b>Description:</b> <br>
 * The User class defines a FIFA Statistics user. It contains all of the user's
 * properties and related items, such as name, level, experience, friends, stats,
 * matches, etc. <br>
 * There shall only ever be one instance of a user per googleId (two users
 * cannot be shared by a single Google account).
 *
 * @version 1.0
 * @author Kevin Grant
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String id;
    private String name;
    private String email;
    private String googleId;
    private String registrationToken;
    private String imageUrl;
    private ArrayList<Friend> friends;
    private ArrayList<Friend> incomingRequests;
    private ArrayList<Friend> outgoingRequests;
    private ArrayList<MatchStub> matches;
    private ArrayList<SeriesStub> series;
    private Stats records;
    private Stats averages;
    private int matchWins;
    private int matchLosses;
    private int seriesWins;
    private int seriesLosses;
    private int level;
    private int experience;

    public User() {

    }

    private User(Builder builder)
    {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.googleId = builder.googleId;
        this.registrationToken = builder.registrationToken;
        this.imageUrl = builder.imageUrl;
    }

    public static class Builder
    {
        private String id;
        private String name;
        private String email;
        private String googleId;
        private String registrationToken;
        private String imageUrl;

        public Builder withId(String id)
        {
            this.id = id;
            return this;
        }

        public Builder withName(String name)
        {
            this.name = name;
            return this;
        }

        public Builder withEmail(String email)
        {
            this.email = email;
            return this;
        }

        public Builder withGoogleId(String googleId)
        {
            this.googleId = googleId;
            return this;
        }

        public Builder withRegistrationToken(String registrationToken)
        {
            this.registrationToken = registrationToken;
            return this;
        }

        public Builder withImageUrl(String imageUrl)
        {
            this.imageUrl = imageUrl;
            return this;
        }

        public User build()
        {
            throwExceptionIfPropertiesAreNull();
            return new User(this);
        }

        private void throwExceptionIfPropertiesAreNull()
        {
            if (name == null) throwExceptionForProperty("name");
            else if (email == null) throwExceptionForProperty("email");
            else if (googleId == null) throwExceptionForProperty("googleId");
            else if (imageUrl == null) throwExceptionForProperty("imageUrl");
            else if (registrationToken == null) throwExceptionForProperty(
                    "registrationToken");
        }

        private void throwExceptionForProperty(String propertyName)
        {
            throw new IllegalArgumentException("ERROR! " + propertyName +
                    " cannot be null!");
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGoogleId() {
        return googleId;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public ArrayList<Friend> getIncomingRequests() {
        return incomingRequests;
    }

    public ArrayList<Friend> getOutgoingRequests() {
        return outgoingRequests;
    }

    public ArrayList<MatchStub> getMatches() {
        return matches;
    }

    public ArrayList<SeriesStub> getSeries() {
        return series;
    }

    public Stats getRecords() {
        return records;
    }

    public Stats getAverages() {
        return averages;
    }

    public int getMatchWins() {
        return matchWins;
    }

    public int getMatchLosses() {
        return matchLosses;
    }

    public int getSeriesWins() {
        return seriesWins;
    }

    public int getSeriesLosses() {
        return seriesLosses;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    /**
     * Adds a FriendRequest to the User's incoming requests list.
     */
    public void addIncomingRequest(Friend friend)
    {
        if (incomingRequests == null) {
            incomingRequests = new ArrayList<>();
        }
        incomingRequests.add(friend);
    }

    /**
     * Adds a FriendRequest to the User's outgoing requests list.
     */
    public void addOutgoingRequest(Friend friend)
    {
        if (outgoingRequests == null) {
            outgoingRequests = new ArrayList<>();
        }
        outgoingRequests.add(friend);
    }

    public void deleteIncomingRequests()
    {
        if (incomingRequests != null) {
            incomingRequests.clear();
        }
    }

    public void addFriend(Friend friend)
    {
        if (friends == null) {
            friends = new ArrayList<>();
        }
        friends.add(friend);
    }
}
