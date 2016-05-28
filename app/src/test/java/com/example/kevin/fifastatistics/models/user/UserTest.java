package com.example.kevin.fifastatistics.models.user;

import com.example.kevin.fifastatistics.datagenerators.UserGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserTest {

    private static final Logger LOGGER = Logger.getLogger(UserTest.class.getName());

    @BeforeClass
    public static void oneTimeSetUp() {
        LOGGER.setLevel(Level.SEVERE);
    }

    @Test
    public void user_SerializationTest() {
        User user = UserGenerator.generateUser();
        try {
            String s = user.toFormattedString();
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
        String user = UserGenerator.generateUser().toString();
        try {
            User u = mapper.readValue(user, User.class);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            Assert.fail("Failed to deserialize user! " + e.getMessage());
        }
    }
}
