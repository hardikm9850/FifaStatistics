package com.example.kevin.fifastatistics.datagenerators;

import com.example.kevin.fifastatistics.models.user.Stats;

import java.util.Random;

public class StatsGenerator {

    private static Random val;

    public static Stats generateStats() {
        val = new Random();
        return Stats.builder()
                .goalsFor(randomInt(4))
                .goalsAgainst(randomInt(4))
                .shotsFor(randomInt(14))
                .shotsAgainst(randomInt(15))
                .shotsOnTarget(randomInt(10))
                .possession(randomInt(40, 60))
                .tacklesFor(randomInt(8))
                .tacklesAgainst(randomInt(9))
                .fouls(randomInt(4))
                .redCards(randomInt(2))
                .offsides(randomInt(7))
                .shotAccuracy(randomInt(10,100))
                .passAccuracy(randomInt(40, 90))
                .build();
    }

    private static int randomInt(int lowerBound, int upperBound) {
        return val.nextInt(upperBound - lowerBound + 1) + lowerBound;
    }

    private static int randomInt(int bound) {
        return val.nextInt(bound) + 1;
    }
}
