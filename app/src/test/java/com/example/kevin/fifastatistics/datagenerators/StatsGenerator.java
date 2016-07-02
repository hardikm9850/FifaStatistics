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
        Stats stats = new Stats();
        stats.setGoals(randomInt(4));
        stats.setShots(randomInt(14));
        stats.setShotsOnTarget(randomInt(10));
        stats.setPossession(randomInt(40, 60));
        stats.setTackles(randomInt(8));
        stats.setFouls(randomInt(4));
        stats.setYellowCards(randomInt(5));
        stats.setRedCards(randomInt(2));
        stats.setOffsides(randomInt(7));
        stats.setInjuries(2);
        stats.setShotAccuracy(randomInt(10,100));
        stats.setPassAccuracy(randomInt(40, 90));

        return stats;
    }

    private static int randomInt(int lowerBound, int upperBound) {
        return rand.nextInt(upperBound - lowerBound + 1) + lowerBound;
    }

    private static int randomInt(int bound) {
        return rand.nextInt(bound) + 1;
    }
}
