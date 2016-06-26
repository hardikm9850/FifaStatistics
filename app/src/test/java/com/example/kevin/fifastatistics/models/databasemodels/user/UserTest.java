package com.example.kevin.fifastatistics.models.databasemodels.user;

import com.example.kevin.fifastatistics.datagenerators.FriendGenerator;
import com.example.kevin.fifastatistics.datagenerators.UserGenerator;
import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserTest {

    private static final Logger LOGGER = Logger.getLogger(UserTest.class.getName());
    private static User user;

    @BeforeClass
    public static void oneTimeSetUp() {
        LOGGER.setLevel(Level.WARNING);
    }

    @Before
    public void setUp() {
        user = UserGenerator.generateUser();
    }

    @After
    public void tearDown() {
        user = null;
    }

    @Test
    public void user_SerializationTest() {
        try {
            String s = SerializationUtils.toFormattedJson(user);
            LOGGER.warning("User: ");
            LOGGER.warning(s);
        } catch (RuntimeException e) {
            LOGGER.severe(e.getMessage());
            Assert.fail("Serialization of user failed: " + e.getMessage());
        }
    }

    @Test
    public void user_DeserializationTest() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readValue(user.toString(), User.class);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            Assert.fail("Failed to deserialize user! " + e.getMessage());
        }
    }

    @Test
    public void user_addIncomingRequestTest() {
        Friend friend = FriendGenerator.generateFriend();
        int previousSize = user.getIncomingRequests().size();
        user.addIncomingRequest(friend);

        Assert.assertEquals("Incoming Request was not added to list!",
                previousSize + 1, user.getIncomingRequests().size());
    }

    @Test
    public void user_addIncomingRequest_nullList_Test() {
        Friend friend = FriendGenerator.generateFriend();
        user = UserGenerator.generateUser(UserGenerator.NULL_REQUESTS_LIST);

        Assert.assertNull(user.getIncomingRequests());

        user.addIncomingRequest(friend);

        Assert.assertEquals("Incoming Request was not added to list!",
                1, user.getIncomingRequests().size());
    }

    @Test
    public void user_addOutgoingRequestTest() {
        Friend friend = FriendGenerator.generateFriend();
        int previousSize = user.getOutgoingRequests().size();
        user.addOutgoingRequest(friend);

        Assert.assertEquals("Outgoing Request was not added to list!",
                previousSize + 1, user.getOutgoingRequests().size());
    }
}
