package com.example.kevin.fifastatistics.utils;

import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

public class StatsUtils {

    public static User.StatsPair shiftStatsUpForPair(User.StatsPair pair) {
        if (pair != null) {
            return new User.StatsPair(shiftStatsUp(pair.getStatsFor()), shiftStatsUp(pair.getStatsAgainst()));
        } else {
            return null;
        }
    }

    private static Stats shiftStatsUp(Stats stats) {
        stats.setGoals(stats.getShots());
        stats.setShots(stats.getShotsOnTarget());
        stats.setShotsOnTarget(stats.getPossession());
        stats.setPossession(stats.getTackles());
        stats.setTackles(stats.getFouls());
        stats.setFouls(stats.getYellowCards());
        stats.setYellowCards(stats.getRedCards());
        stats.setRedCards(stats.getInjuries());
        stats.setInjuries(stats.getOffsides());
        stats.setOffsides(stats.getCorners());
        stats.setCorners(stats.getShotAccuracy());
        stats.setPassAccuracy(OcrResultParser.ERROR_VALUE);
        return stats;
    }
}
