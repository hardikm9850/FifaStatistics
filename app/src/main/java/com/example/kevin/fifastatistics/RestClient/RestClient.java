package com.example.kevin.fifastatistics.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Rest Client that interacts with the FifaStats RESTful API. Implemented as a singleton.
 * Created by Kevin on 1/1/2016.
 */
public class RestClient {

    private static RestClient instance = new RestClient();

    private static final String host = "192.168.0.106";
    private static final String port = "8080";

    private static final String users = "Users/";
    private static final String embedded = "_embedded";

    public static RestClient getInstance() {
        return instance;
    }

    private RestClient() {
    }

    private String getUrlBase()
    {
        return "http://" + host + ":" + port + "/";
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
        catch(Exception e) {
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
    public JsonNode getUsers()
    {
        JsonNode response = getResponse(users);
        if (response != null) {
            return response.get(embedded).get("Users");
        }
        else {
            return null;
        }
    }

    /**
     * Returns the users with the specified user name. The name must be an exact match.
     * @param name  the name being searched on
     * @return Users with the specified name, null if the response was empty
     */
    public JsonNode getUsersWithName(String name)
    {
        JsonNode response = getResponse(users + "search/findByName" + "?name=" + name);
        if (response != null) {
            return response.get(embedded).get("Users");
        }
        else {
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
    private static JsonNode parseJsonNodeFromJson(String jsonPayload)
    {
        ObjectMapper mapper	= new ObjectMapper();
        JsonNode jsonNode = null;
        if(jsonPayload == null ||jsonPayload.isEmpty())
        {
            return null;
        }
        try
        {
            jsonNode = mapper.readTree(jsonPayload);
        } catch (Exception e)
        {

        }

        return jsonNode;
    }
}

