package com.example.kevin.fifastatistics.datagenerators;

import com.example.kevin.fifastatistics.models.databasemodels.match.Result;
import com.example.kevin.fifastatistics.models.databasemodels.user.records.UserRecords;

import java.util.Random;

public class RecordsGenerator {

    public static UserRecords generateRecords(int numberOfResults) {
        UserRecords records = UserRecords.emptyRecords();
        Random rand = new Random();
        for (int i = 0; i < numberOfResults; i++) {
            if (rand.nextInt(2) == 0) {
                records.addResult(Result.LOSS);
            } else {
                records.addResult(Result.WIN);
            }
        }

        return records;
    }
}
