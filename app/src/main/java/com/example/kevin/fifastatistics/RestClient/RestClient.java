package com.example.kevin.fifastatistics.RestClient;

import android.app.DownloadManager;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Rest Client that interacts with the FifaStats RESTful API. Implemented as a singleton.
 * Created by Kevin on 1/1/2016.
 */
public class RestClient {

    private static RestClient instance = new RestClient();
    private static final String JSON_TYPE = "application/json";
    private static final String HOST = "https://fifastatisticsapi.azurewebsites.net/";
    private static final String USERS = "users/";
    private static final String EMBEDDED = "_embedded";
    private static final String FIND_BY_NAME = USERS + "search/findByName?name=";
    private static final String FIND_BY_GOOGLE_ID = USERS + "search/findByGoogleId?googleId=";

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

        if (response.code() == 200)
        {
            return parseJsonNodeFromJson(response.body().string());
        }
        else
        {
            return null;
        }
    }

    //----------------------------------------------------------------------------------------------
    // PUBLIC METHODS
    //----------------------------------------------------------------------------------------------

    public void createUser(String name, String googleId, String email, String imageUrl)
            throws IOException
    {
        OkHttpClient client = new OkHttpClient();

        MediaType type = MediaType.parse(JSON_TYPE);
        RequestBody body = RequestBody.create(type, buildUser(name, googleId, email, imageUrl));
        Request request = new Request.Builder()
                .url(HOST + USERS)
                .post(body)
                .addHeader("Content-type", JSON_TYPE)
                .build();

        Response response = client.newCall(request).execute();

        if (response.code() != 201)
        {
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

    // ---------------------------------------------------------------------------------------------
    // HELPERS
    // ---------------------------------------------------------------------------------------------

    private static String buildUser(String name, String googleId,
                                    String email, String imageUrl)
    {
        Log.d("BUILDING", "building user");
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("name", name);
        node.put("googleId", googleId);
        node.put("email", email);
        node.put("imageUrl", imageUrl);
        Log.d("creating", "request body json: " + node.toString());
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

