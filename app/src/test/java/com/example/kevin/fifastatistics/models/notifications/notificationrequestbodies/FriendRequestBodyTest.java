package com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies;

import com.example.kevin.fifastatistics.datagenerators.UserGenerator;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import org.junit.Test;

public class FriendRequestBodyTest {

    private static final String to = "sdd98fdsfdfsd98f7ds9fsdfdsjfksdfhsdf79f8sfhdsfs";

    @Test
    public void FriendRequestBody_SerializationTest() {
        User user = UserGenerator.generateUser();
        SendFriendRequestBody frb = new SendFriendRequestBody(user, to);

        System.out.println(frb.toString());
    }
}
