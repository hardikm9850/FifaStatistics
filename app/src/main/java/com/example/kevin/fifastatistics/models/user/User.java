package com.example.kevin.fifastatistics.models.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

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

    public User(String name, String email, String googleId, String imageUrl)
    {
        this.name = name;
        this.email = email;
        this.googleId = googleId;
        this.imageUrl = imageUrl;
    }

    public User(String name, String email, String googleId, String imageUrl,
                String registrationToken)
    {
        this.name = name;
        this.email = email;
        this.googleId = googleId;
        this.imageUrl = imageUrl;
        this.registrationToken = registrationToken;
    }

    public User(String name, String email, String googleId, String registrationToken,
                String imageUrl, String id)
    {
        this.name = name;
        this.email = email;
        this.googleId = googleId;
        this.registrationToken = registrationToken;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public User() {

    }

    // ----------------------------------------------------------------------
    // GETTERS / SETTERS
    // ----------------------------------------------------------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
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

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public ArrayList<Friend> getIncomingRequests() {
        return incomingRequests;
    }

    public void setIncomingRequests(ArrayList<Friend> incomingRequests) {
        this.incomingRequests = incomingRequests;
    }

    public ArrayList<Friend> getOutgoingRequests() {
        return outgoingRequests;
    }

    public void setOutgoingRequests(ArrayList<Friend> outgoingRequests) {
        this.outgoingRequests = outgoingRequests;
    }

    public ArrayList<MatchStub> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<MatchStub> matches) {
        this.matches = matches;
    }

    public ArrayList<SeriesStub> getSeries() {
        return series;
    }

    public void setSeries(ArrayList<SeriesStub> series) {
        this.series = series;
    }

    public Stats getRecords() {
        return records;
    }

    public void setRecords(Stats records) {
        this.records = records;
    }

    public Stats getAverages() {
        return averages;
    }

    public void setAverages(Stats averages) {
        this.averages = averages;
    }

    public int getMatchWins() {
        return matchWins;
    }

    public void setMatchWins(int matchWins) {
        this.matchWins = matchWins;
    }

    public int getMatchLosses() {
        return matchLosses;
    }

    public void setMatchLosses(int matchLosses) {
        this.matchLosses = matchLosses;
    }

    public int getSeriesWins() {
        return seriesWins;
    }

    public void setSeriesWins(int seriesWins) {
        this.seriesWins = seriesWins;
    }

    public int getSeriesLosses() {
        return seriesLosses;
    }

    public void setSeriesLosses(int seriesLosses) {
        this.seriesLosses = seriesLosses;
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

    // ----------------------------------------------------------------------
    // UTILITIES
    // ----------------------------------------------------------------------

    /**
     * Adds a FriendRequest to the User's incoming requests list.
     * @param name          The name of the user
     * @param id            The ID of the user
     * @param imageUrl      The user's image URL
     */
    public void addIncomingRequest(String name, String id, String imageUrl, int level,
                                   String registrationToken)
    {
        if (incomingRequests == null) {
            incomingRequests = new ArrayList<>();
        }
        incomingRequests.add(new Friend(id, name, imageUrl, level, registrationToken));
    }

    /**
     * Adds a FriendRequest to the User's outgoing requests list.
     * @param name          The name of the user
     * @param id            The ID of the user
     * @param imageUrl      The user's image URL
     */
    public void addOutgoingRequest(String name, String id, String imageUrl, int level,
                                   String registrationToken)
    {
        if (outgoingRequests == null) {
            outgoingRequests = new ArrayList<>();
        }
        outgoingRequests.add(new Friend(id, name, imageUrl, level, registrationToken));
    }

    public void deleteIncomingRequests()
    {
        if (incomingRequests != null) {
            incomingRequests.clear();
        }
    }

    /**
     * Adds a Friend to the User's friends list.
     * @param id            The User's ID
     * @param name          The User's name
     * @param imageUrl      The User's image URL
     * @param level         The User's level
     * @param registrationToken     The User's registration token
     */
    public void addFriend(String id, String name, String imageUrl,
                          int level, String registrationToken)
    {
        if (friends == null) {
            friends = new ArrayList<>();
        }
        friends.add(new Friend(id, name, imageUrl, level, registrationToken));
    }

    public void addFriend(Friend friend)
    {
        if (friends == null) {
            friends = new ArrayList<>();
        }
        friends.add(friend);
    }

}
