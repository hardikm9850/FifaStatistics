package com.example.kevin.fifastatistics.restclient;

import android.util.Log;

import com.example.kevin.fifastatistics.gcm.NotificationTypesEnum;
import com.example.kevin.fifastatistics.user.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Rest Client that interacts with the FifaStats REST API. Implemented as a singleton.
 * Created by Kevin on 1/1/2016.
 */
public class RestClient {

    private static RestClient instance = new RestClient();
    private static final String JSON_TYPE = "application/json";
    private static final String HOST = "https://fifastatisticsapi.azurewebsites.net/";
    private static final String USERS = "users/";
    private static final String REQUESTS = "requests/";
    private static final String EMBEDDED = "_embedded";
    private static final String FIND_BY_NAME = USERS + "search/findByName?name=";
    private static final String FIND_BY_GOOGLE_ID = USERS + "search/findByGoogleId?googleId=";
    private static final String FIND_BY_RECEIVER_ID = REQUESTS + "search/findByReceiverId?receiverId=";
    private static final String NOTIFICATION_URL = "https://gcm-http.googleapis.com/gcm/send";
    private static final String NOTIFICATION_KEY = "AIzaSyDjCHksoGamhWxeNsaDN-DW5v3p9IcJNFE";

    private static final int OK = 200;
    private static final int CREATED = 201;

    public static RestClient getInstance() {
        return instance;
    }

    private RestClient() {
    }

    private JsonNode getResponse(String path) throws IOException
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(HOST + path)
                .get()
                .build();

        Response response = client.newCall(request).execute();

        if (response.code() == OK)
        {
            return parseJsonNodeFromJson(response.body().string());
        }
        else
        {
            throw new IOException();
        }
    }

    private JsonNode postResponse(String path, String body, HashMap<String, String> headers)
            throws IOException
    {
        OkHttpClient client = new OkHttpClient();

        MediaType type = MediaType.parse(JSON_TYPE);
        RequestBody requestBody = RequestBody.create(
                type, body);

        Request request = new Request.Builder()
                    .url(path)
                    .post(requestBody)
                    .addHeader("Content-type", JSON_TYPE)
                    .build();

        if (headers != null) {
            for (String key : headers.keySet()) {
                request.newBuilder().addHeader(key, headers.get(key));
            }
        }

        Response response = client.newCall(request).execute();
        int code = response.code();
        if (code == CREATED || code == OK)
        {
            return parseJsonNodeFromJson(response.body().string());
        }
        else
        {
            throw new IOException();
        }
    }

    /**
     * HTTP POST Request, with no extra headers
     */
    private JsonNode postResponse(String path, String body) throws IOException
    {
        return postResponse(path, body, null);
    }

    //----------------------------------------------------------------------------------------------
    // PUBLIC METHODS
    //----------------------------------------------------------------------------------------------

    /**
     * Sends a POST new user request.
     * @param name  The name of the user
     * @param googleId The googleId of the user
     * @param email The email of the user
     * @param registrationToken The registration token of the user
     * @param imageUrl The imageUrl of the user
     * @return the JSON response
     * @throws IOException
     */
    public JsonNode createUser(String name, String googleId, String email,
                               String registrationToken, String imageUrl)
            throws IOException
    {
        JsonNode response = postResponse(HOST + USERS, buildUser(
                name, googleId, email, registrationToken, imageUrl));

        if (response != null) {
            return response;
        }
        else {
            throw new IOException();
        }
    }

    public JsonNode sendFriendRequest(User currentUser, String receiverRegistrationToken)
            throws IOException
    {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("key", NOTIFICATION_KEY);

        JsonNode response = postResponse(
                NOTIFICATION_URL,
                buildFriendRequestNotification(currentUser, receiverRegistrationToken),
                headers);

        if (response != null) {
            return response;
        }
        else {
            throw new IOException();
        }
    }

    /**
     * Returns the list of users in JSON format
     * @return  the users, null if the response was empty
     * @throws IOException
     */
    public JsonNode getUsers() throws IOException
    {
        JsonNode response = getResponse(USERS);
        if (response != null) {
            return response.get(EMBEDDED).get("users");
        }
        else throw new IOException();
    }

    /**
     * Returns the users with the specified user name. The name must be an exact match.
     * @param name  the name being searched on
     * @return Users with the specified name, null if the response was empty
     * @throws IOException
     */
    public JsonNode getUsersWithName(String name) throws IOException
    {
        JsonNode response = getResponse(FIND_BY_NAME + name);
        if (response != null) {
            return response.get(EMBEDDED).get("users");
        }
        else throw new IOException();
    }

    /**
     * Returns the user with the specified googleId. Only one user can be returned, as googleId's are
     * unique among users.
     * @param googleId      The googleId
     * @return the user with the googleId, null if no user exists
     * @throws IOException
     */
    public JsonNode getUserWithGoogleId(String googleId) throws IOException
    {
        return getResponse(FIND_BY_GOOGLE_ID + googleId);
    }

    /**
     * Returns friend requests where the user defined by the ID parameter is the receiver of the
     * request.
     * @param id the User ID
     * @return the User's friend requests
     * @throws IOException
     */
    public ArrayNode getRequestsForUser(String id) throws IOException
    {
        JsonNode response = getResponse(FIND_BY_RECEIVER_ID + id);
        if (response != null)
        {
            return (ArrayNode) response.get(EMBEDDED).get("requests");
        }
        else throw new IOException();
    }

//    public void acceptFriendRequest

    // ---------------------------------------------------------------------------------------------
    // HELPERS
    // ---------------------------------------------------------------------------------------------

    private static String buildUser(String name, String googleId, String email,
                                    String registrationToken, String imageUrl)
    {
        Log.d("BUILDING", "building user");
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("name", name);
        node.put("googleId", googleId);
        node.put("registrationToken", registrationToken);
        node.put("email", email);
        node.put("imageUrl", imageUrl);
        Log.d("creating", "request body json: " + node.toString());
        return node.toString();
    }

    private static String buildFriendRequestNotification(User currentUser,
                                                         String receiverRegistrationToken)
    {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        ObjectNode notification = JsonNodeFactory.instance.objectNode();
        ObjectNode data = JsonNodeFactory.instance.objectNode();

        notification.put("body", currentUser.getName());
        notification.put("title", "New Friend Request");

        data.put("tag", NotificationTypesEnum.FRIEND_REQUEST.name());
        data.put("name", currentUser.getName());
        data.put("id", currentUser.getId());
        data.put("imageUrl", currentUser.getImageUrl());

        node.put("notification", notification);
        node.put("data", data);
        node.put("to", receiverRegistrationToken);

        return node.toString();
    }

    /**
     * Parses a JSON String and returns the JsonNode representation of it.
     * @param jsonPayload   The JSON String
     * @return the JsonNode
     */
    private static JsonNode parseJsonNodeFromJson(String jsonPayload) {
        ObjectMapper mapper	= new ObjectMapper();
        JsonNode jsonNode;
        if(jsonPayload == null || jsonPayload.isEmpty()) {
            return null;
        }
        try {
            jsonNode = mapper.readTree(jsonPayload);
        } catch (IOException e) {
            return null;
        }

        return jsonNode;
    }
}

