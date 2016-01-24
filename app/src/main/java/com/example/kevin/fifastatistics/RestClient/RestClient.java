package com.example.kevin.fifastatistics.RestClient;

import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Rest Client that interacts with the FifaStats RESTful API. Implemented as a singleton.
 * Created by Kevin on 1/1/2016.
 */
public class RestClient {

    private static RestClient instance = new RestClient();

    private static final String host = "fifastatisticsapi.azurewebsites.net";

    private static final String users = "users/";
    private static final String embedded = "_embedded";

    public static RestClient getInstance() {
        return instance;
    }

    private RestClient() {
    }

    private String getUrlBase()
    {
        return "https://" + host + "/";
    }

    private JsonNode getResponse(String path) {

        try {
            URL url = new URL(getUrlBase() + path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                String jsonString = stringBuilder.toString();
                return parseJsonNodeFromJson(jsonString);
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //----------------------------------------------------------------------------------------------
    // PUBLIC METHODS
    //----------------------------------------------------------------------------------------------

    /**
     * Returns the list of users in JSON format
     * @return  the users, null if the response was empty
     */
    public JsonNode getUsers() {
        JsonNode response = getResponse(users);
        if (response != null) {
            return response.get(embedded).get("users");
        } else {
            return null;
        }
    }

    /**
     * Returns the users with the specified user name. The name must be an exact match.
     * @param name  the name being searched on
     * @return Users with the specified name, null if the response was empty
     */
    public JsonNode getUsersWithName(String name) {
        JsonNode response = getResponse(users + "search/findByName" + "?name=" + name);
        if (response != null) {
            return response.get(embedded).get("users");
        } else {
            return null;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // HELPERS
    // ---------------------------------------------------------------------------------------------

    /**
     * Parses a JSON String and returns the JsonNode representation of it.
     * @param jsonPayload   The JSON String
     * @return the JsonNode
     */
    private static JsonNode parseJsonNodeFromJson(String jsonPayload) {
        ObjectMapper mapper	= new ObjectMapper();
        JsonNode jsonNode;
        if(jsonPayload == null ||jsonPayload.isEmpty()) {
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

