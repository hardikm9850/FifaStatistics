package com.example.kevin.fifastatistics.datagenerators;

import com.example.kevin.fifastatistics.models.databasemodels.user.MatchStub;

import java.util.Date;
import java.util.Random;

public class MatchStubGenerator {

    private static final int MAX_GOALS = 6;

    public static MatchStub generateMatchStub(String id) {

        Date today = new Date();
        Random goals = new Random();
        int goalsWinner = goals.nextInt(MAX_GOALS + 1);
        int goalsLoser = (goalsWinner == 0) ? 0 : goals.nextInt(goalsWinner + 1);

        return MatchStub.builder()
                .winnerId(chooseId(id))
                .date(today.toString())
                .goalsWinner(goalsWinner)
                .goalsLoser(goalsLoser)
                .penalties((goalsWinner == goalsLoser) ? PenaltiesGenerator.generatePenalties() : null)
                .build();
    }

    private static String chooseId(String userId) {
        Random didWin = new Random();
        return didWin.nextBoolean() ? userId : IdGenerator.getRandomId();
    }
}
