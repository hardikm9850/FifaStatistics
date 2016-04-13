package com.example.kevin.fifastatistics.models.apiresponses;

import com.example.kevin.fifastatistics.models.user.Friend;
import com.example.kevin.fifastatistics.models.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserListResponse
{
    @JsonProperty("_embedded")
    private Embedded embedded;

    public UserListResponse() {

    }

    @JsonIgnore
    public ArrayList<User> getUsers()
    {
        return new ArrayList<>(Arrays.asList(embedded.getUsers()));
    }

    @JsonIgnore
    public ArrayList<Friend> getUsersAsFriends()
    {
        ArrayList<Friend> friends = new ArrayList<>();
        for (User user : Arrays.asList(embedded.getUsers())) {
            friends.add(new Friend(user));
        }
        return friends;
    }

    public static class Embedded
    {
        private User[] users;

        public Embedded() {

        }

        public User[] getUsers() {
            return users;
        }
    }
}
