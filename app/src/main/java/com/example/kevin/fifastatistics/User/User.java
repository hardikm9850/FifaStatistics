package com.example.kevin.fifastatistics.User;

import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * Created by Kevin on 1/1/2016.
 */
public class User {

    private String name;
    private String email;
    private String googleId;
    private String imageUrl;
    private ArrayNode friends;

    /**
     * Default constructor to create a new user.
     * @param name      Username
     * @param email     The user's email
     * @param googleId  The user's googleId
     * @param imageUrl  The user's google photoUrl
     */
    public User(String name, String email, String googleId, String imageUrl)
    {
        this.name = name;
        this.email = email;
        this.googleId = googleId;
        this.imageUrl = imageUrl;
    }

    public User(String name, String imageUrl)
    {
        this.name = name;
        this.imageUrl = imageUrl;
        this.email = null;
        this.googleId = null;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public String getGoogleId()
    {
        return googleId;
    }

}
