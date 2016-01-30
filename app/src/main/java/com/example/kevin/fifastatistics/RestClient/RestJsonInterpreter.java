package com.example.kevin.fifastatistics.RestClient;

import com.example.kevin.fifastatistics.User.User;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;

/**
 * Interprets the JSON returned by the {@link RestClient} class and returns data as its appropriate
 * type, e.g. User, Match, etc.
 * Created by Kevin on 1/1/2016.
 */
public class RestJsonInterpreter {

    private static RestJsonInterpreter ourInstance = new RestJsonInterpreter();
    private static RestClient client = RestClient.getInstance();

    public static RestJsonInterpreter getInstance() {
        return ourInstance;
    }

    private RestJsonInterpreter() {
    }

    /**
     * Returns an ArrayList of all Users in the database.
     * @return  the user list
     */
//    public ArrayList<User> getUserList() {
//        ArrayList<User> userList = new ArrayList<>();
//
//        JsonNode users = client.getUsers();
//        for (JsonNode user : users) {
//            userList.add(new User(user.get("name").asText(), user.get("imageUrl").asText()));
//        }
//
//        return userList;
//    }
}
