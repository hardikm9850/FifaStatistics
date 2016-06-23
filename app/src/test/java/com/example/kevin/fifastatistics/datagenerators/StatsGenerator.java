package com.example.kevin.fifastatistics.datagenerators;

import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import java.util.Random;

public class StatsGenerator {

    private static Random rand;

    public static User.StatsPair generateStatsPair() {
        rand = new Random();
        return new User.StatsPair(generateStats(), generateStats());
    }

    private static Stats generateStats() {
        return Stats.builder()
                .goals(randomInt(4))
                .shots(randomInt(14))
                .shotsOnTarget(randomInt(10))
                .possession(randomInt(40, 60))
                .tackles(randomInt(8))
                .fouls(randomInt(4))
                .yellowCards(randomInt(5))
                .redCards(randomInt(2))
                .offsides(randomInt(7))
                .injuries(2)
                .shotAccuracy(randomInt(10,100))
                .passAccuracy(randomInt(40, 90))
                .build();
    }

    private static int randomInt(int lowerBound, int upperBound) {
        return rand.nextInt(upperBound - lowerBound + 1) + lowerBound;
    }

    private static int randomInt(int bound) {
        return rand.nextInt(bound) + 1;
    }
}
