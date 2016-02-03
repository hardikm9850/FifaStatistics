package com.example.kevin.fifastatistics.overview;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kevin.fifastatistics.restclient.RestClient;
import com.example.kevin.fifastatistics.user.User;
import com.example.kevin.fifastatistics.utils.PreferenceHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Attempts to retrieve any friends requests directed to the current user. If there are any,
 * they are linked to the current user.
 */
public class GetFriendRequestsAsyncTask extends AsyncTask<String, Void, Void>
{
    private static final RestClient client = RestClient.getInstance();
    private PreferenceHandler handler;
    private ArrayNode requests;
    private User user;

    public GetFriendRequestsAsyncTask(User user, PreferenceHandler handler)
    {
        this.user = user;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(String... args)
    {
        try
        {
            if (args[0] != null)
            {
                requests = client.getRequestsForUser(args[0]);
            }
        }
        catch (IOException e)
        {
            Log.e("GetFriendRequests", "Unable to retrieve requests");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        Log.d("GetFriendRequests", "requests: " + requests);
        if (requests != null)
        {
            int size = requests.size();
            Log.d("GetFriendRequests", "size: " + size);
            if (size > 0)
            {
                if (user.incomingRequests == null)
                {
                    user.incomingRequests = new ArrayList<>();
                }
                JsonNode request;
                User.Request incomingRequest = user.new Request();
                for (int i = 0; i < size; i++) {
                    request = requests.get(i);
                    incomingRequest.id = request.get("senderId").asText();
                    incomingRequest.name = request.get("senderName").asText();
                    incomingRequest.requestId = request.get("requestId").asText();
                    user.incomingRequests.add(incomingRequest);
                }

                handler.storeUserAsync(user);
            }
        }
    }
}
