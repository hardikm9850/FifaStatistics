package com.example.kevin.fifastatistics.User;

import android.widget.ImageView;
import com.example.kevin.fifastatistics.R;

/**
 * Created by Kevin on 1/1/2016.
 */
public class User {

    private String name;
    private String email;
    private int imageId;
    private String imageUrl;
    private String password;

    private String defaultImage = "ball.png";

    /**
     * Default constructor to create a new user.
     * @param name      Username
     * @param email     The user's email
     * @param password  The password
     */
    public User(String name, String email, String password)
    {
        this.name = name;
        this.email = email;
        this.password = password;
        this.imageId = R.drawable.profile;
        this.imageUrl = null;
    }

    /**
     * Constructor that includes an image. Sets 'imageId' to -1.
     * @param name          Username
     * @param email         The user's email
     * @param password      The password
     * @param imageUrl      The image URL
     */
    public User(String name, String email, String password, String imageUrl)
    {
        this.name = name;
        this.email = email;
        this.password = password;
        this.imageId = -1;
        this.imageUrl = imageUrl;
    }

    public User(String name, String imageUrl)
    {
        this.name = name;
        this.imageUrl = imageUrl;
        this.password = null;
        this.imageId = -1;
        this.email = null;
    }

    public User(String name)
    {
        this.name = name;
        this.imageUrl = null;
        this.password = null;
        this.imageId = -1;
        this.email = null;
    }

    public String getName()
    {
        return this.name;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getImageUrl()
    {
        return this.imageUrl;
    }

    public int getImageId()
    {
        return this.imageId;
    }

    public void setImageId(int imageId)
    {
        this.imageId = imageId;
    }

}
