package com.example.kevin.fifastatistics.models.databasemodels.user.records;

import com.example.kevin.fifastatistics.models.databasemodels.match.Result;
import com.example.kevin.fifastatistics.utils.SerializationUtils;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CircularRecordTest {

    private static final Logger LOGGER = Logger.getLogger(CircularRecordTest.class.getName());

    @BeforeClass
    public static void oneTimeSetUp() {
        LOGGER.setLevel(Level.WARNING);
    }

    @Test
    public void circularRecord_addResultTest() {
        Record record = CircularRecord.emptyRecord();
        record.addResult(Result.WIN);
        assertCorrectWinsAndLosses(1, 0, record);

        record.addResult(Result.LOSS);
        assertCorrectWinsAndLosses(1, 1, record);

        record.addResult(Result.LOSS);
        assertCorrectWinsAndLosses(1, 2, record);

        for (int i = 1; i < 8; i++) {
            record.addResult(Result.WIN);
            assertCorrectWinsAndLosses(1 + i, 2, record);
        }

        record.addResult(Result.LOSS);
        assertCorrectWinsAndLosses(7, 3, record);

        record.addResult(Result.LOSS);
        assertCorrectWinsAndLosses(7, 3, record);

        record.addResult(Result.LOSS);
        assertCorrectWinsAndLosses(7, 3, record);

        record.addResult(Result.LOSS);
        assertCorrectWinsAndLosses(6, 4, record);

        record.addResult(Result.WIN);
        assertCorrectWinsAndLosses(6, 4, record);
    }

    @Test
    public void circularRecord_sizeThree_addResultTest() {
        Record record = CircularRecord.emptyRecord(3);
        record.addResult(Result.WIN);
        assertCorrectWinsAndLosses(1, 0, record);

        record.addResult(Result.LOSS);
        assertCorrectWinsAndLosses(1, 1, record);

        record.addResult(Result.LOSS);
        assertCorrectWinsAndLosses(1, 2, record);

        record.addResult(Result.WIN);
        assertCorrectWinsAndLosses(1, 2, record);
    }

    @Test
    public void circularRecord_serializationTest() {
        Record record = CircularRecord.emptyRecord();
        populateRecord(record, 15);
        String s = SerializationUtils.toJson(record);
        LOGGER.warning(s);
    }

    @Test
    public void circularRecord_deserializationTest() {
        Record record = CircularRecord.emptyRecord();
        populateRecord(record, 15);
        String s = SerializationUtils.toJson(record);
        LOGGER.warning(s);
        Record newRecord = SerializationUtils.fromJson(s, CircularRecord.class);
        LOGGER.warning(SerializationUtils.toJson(newRecord));
    }

    private void assertCorrectWinsAndLosses(int wins, int losses, Record record) {
        try {
            Assert.assertTrue(record.getWins() == wins && record.getLosses() == losses);
        } catch (AssertionError e) {
            LOGGER.severe("Wins and Losses mismatch! Expected " + wins + " wins and " + losses  +
                    " losses. Result was " + record.getWins() + " wins and " + record.getLosses()
                    + " losses.");
            Assert.fail();
        }
    }

    private void populateRecord(Record record) {
        populateRecord(record, 10);
    }


    private void populateRecord(Record record, int size) {
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            if (rand.nextInt(2) == 0) {
                record.addResult(Result.LOSS);
            } else {
                record.addResult(Result.WIN);
            }
        }
    }
}
