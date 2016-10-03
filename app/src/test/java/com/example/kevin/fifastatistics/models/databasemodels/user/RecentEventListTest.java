package com.example.kevin.fifastatistics.models.databasemodels.user;

import com.example.kevin.fifastatistics.datagenerators.MatchStubGenerator;
import com.example.kevin.fifastatistics.utils.SerializationUtils;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RecentEventListTest {

    private static final Logger LOGGER = Logger.getLogger(UserTest.class.getName());

    @BeforeClass
    public static void oneTimeSetUp() {
        LOGGER.setLevel(Level.WARNING);
    }

    @Test
    public void recentEventList_serializationFlattensToListTest() {
        RecentEventList<MatchStub> matches = getMatchStubList();
        LOGGER.warning(SerializationUtils.toFormattedJson(matches));
    }

    private RecentEventList<MatchStub> getMatchStubList() {
        RecentEventList<MatchStub> matches = new RecentEventList<>();
        matches.add(MatchStubGenerator.generateMatchStub("hello"));
        for(int i = 0; i < 5; i++) {
            matches.add(MatchStubGenerator.generateMatchStub());
        }
        return matches;
    }
}
