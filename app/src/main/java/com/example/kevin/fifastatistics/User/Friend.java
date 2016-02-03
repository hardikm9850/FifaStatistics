package com.example.kevin.fifastatistics.user;

/**
 * Created by Kevin on 1/29/2016.
 */
/**
 * <b>Class:</b> Friend <br><br>
 * <b>Description:</b> <br>
 * The Friend class is meant to act as a 'stub' of a user, and should exist only
 * within instances of a User object. It defines only a few traits: <ul>
 * <li> <b>href</b>, a reference to the full user object represented by this friend
 * <li> <b>name</b>, the name of the user
 * <li> <b>imageUrl</b>, the URL of the user's image, and
 * <li> <b>level</b>, the user's level.
 * </ul>
 * @version 1.0
 * @author Kevin Grant
 *
 */
public class Friend
{
    public String id;
    public String name;
    public String imageUrl;
    public int level;

    public Friend(String id, String name, String imageUrl, int level)
    {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.level = level;
    }

    public Friend() {

    }
}
