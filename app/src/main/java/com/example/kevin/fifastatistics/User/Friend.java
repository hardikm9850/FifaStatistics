package com.example.kevin.fifastatistics.User;

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
    private String href;
    private String name;
    private String imageUrl;
    private int level;

    public Friend(String href, String name, String imageUrl, int level)
    {
        this.href = href;
        this.name = name;
        this.imageUrl = imageUrl;
        this.level = level;
    }

    public Friend() {

    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }
}
