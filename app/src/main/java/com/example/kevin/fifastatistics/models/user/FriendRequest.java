package com.example.kevin.fifastatistics.models.user;

/**
 * <b>Class:</b> FriendRequest <br><br>
 * <b>Description:</b> <br>
 * The FriendRequest class defines either an incoming or outgoing Friend Request
 * for a given User. It defines a few traits: <ul>
 * <li> <b>id</b>, a reference to the full external User identified in the Request
 * <li> <b>name</b>, the name of the User
 * <li> <b>imageUrl</b>, the URL of the user's image
 * </ul>
 * @version 1.0
 * @author Kevin Grant
 *
 */
public class FriendRequest {

    private String id;
    private String name;
    private String imageUrl;

    public FriendRequest(String id, String name, String imageUrl)
    {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public FriendRequest() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
