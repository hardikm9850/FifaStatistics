package com.example.kevin.fifastatistics.models.databasemodels.user.records;

import com.example.kevin.fifastatistics.models.databasemodels.match.Result;
import com.example.kevin.fifastatistics.utils.SerializationUtils;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRecordsTest {

    private static final Logger LOGGER = Logger.getLogger(CircularRecordTest.class.getName());

    @BeforeClass
    public static void oneTimeSetUp() {
        LOGGER.setLevel(Level.WARNING);
    }

    @Test
    public void userRecords_emptyRecordsTest() {
        UserRecords records = UserRecords.emptyRecords();

        assertTrue(records.getOverallRecord().getWins() == 0);
        assertTrue(records.getOverallRecord().getLosses() == 0);

        assertTrue(records.getLastTenRecord().getWins() == 0);
        assertTrue(records.getLastTenRecord().getLosses() == 0);

        assertTrue(records.getStreak().equals("-"));
    }

    @Test
    public void userRecords_addResultTest() {
        UserRecords records = UserRecords.emptyRecords();
        Random rand = new Random();
        final int numberOfResultsToAdd = 50;
        for (int i = 0; i < numberOfResultsToAdd; i++) {
            if (rand.nextInt(2) == 0) {
                addResultAndAssertCorrect(Result.WIN, records);
            } else {
                addResultAndAssertCorrect(Result.LOSS, records);
            }
        }
        LOGGER.warning(SerializationUtils.toJson(records));
    }

    @Test
    public void userRecords_streak_deserializesCorrectTypeTest() {
        UserRecords records = UserRecords.emptyRecords();
        records.addResult(Result.LOSS);
        records.addResult(Result.LOSS);

        String recordsJson = SerializationUtils.toJson(records);
        LOGGER.warning(recordsJson);

        UserRecords newRecords = SerializationUtils.fromJson(recordsJson, UserRecords.class);
        assertEquals(newRecords.getStreak(), "L2");
    }

    private void addResultAndAssertCorrect(Result result, UserRecords records) {
        int overallWins = records.getOverallRecord().getWins();
        int overallLosses = records.getOverallRecord().getLosses();
        int lastTenWins = records.getLastTenRecord().getWins();
        int lastTenLosses = records.getLastTenRecord().getLosses();
        records.addResult(result);

        if (result == Result.WIN) {
            assertTrue(records.getOverallRecord().getWins() == overallWins + 1);
            assertTrue(records.getOverallRecord().getLosses() == overallLosses);
            assertTrue(records.getLastTenRecord().getWins() >= lastTenWins);
            assertTrue(records.getLastTenRecord().getLosses() <= lastTenLosses);
        } else {
            assertTrue(records.getOverallRecord().getWins() == overallWins);
            assertTrue(records.getOverallRecord().getLosses() == overallLosses + 1);
            assertTrue(records.getLastTenRecord().getWins() <= lastTenWins);
            assertTrue(records.getLastTenRecord().getLosses() >= lastTenLosses);
        }
    }
}
