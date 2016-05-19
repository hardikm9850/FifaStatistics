package com.example.kevin.fifastatistics.models.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
@AllArgsConstructor
@Getter
@Setter
public class FriendRequest {

    private String id;
    private String name;
    private String imageUrl;
}
