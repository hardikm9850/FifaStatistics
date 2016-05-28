package com.example.kevin.fifastatistics.datagenerators;

import com.example.kevin.fifastatistics.models.user.SeriesStub;
import com.example.kevin.fifastatistics.models.user.SeriesStub.MatchSummary;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class SeriesStubGenerator {

    private static final int MAX_GAMES = 7;
    private static final int MIN_GAMES = 4;
    private static final int MAX_GOALS = 6;
    private static Random rand;

    public static SeriesStub generateSeriesStub() {

        rand = new Random();
        boolean didWin = rand.nextBoolean();
        int gameCount = rand.nextInt(MAX_GAMES - MIN_GAMES + 1) + MIN_GAMES;

        return SeriesStub.builder()
                .id(IdGenerator.getRandomId())
                .opponentName(NameGenerator.generateRandomFullName())
                .date(new Date().toString())
                .matches(generateMatchSummaryList(gameCount, didWin))
                .didWin(didWin)
                .build();
    }

    public static ArrayList<MatchSummary> generateMatchSummaryList(int count, boolean didWin) {

        boolean[] wins = getWinsArray(count, didWin);

        ArrayList<MatchSummary> summaries = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            summaries.add(MatchSummaryGenerator.generateMatchSummary(wins[i]));
        }

        return summaries;
    }

    private static boolean[] getWinsArray(int count, boolean didWin) {
        boolean[] wins = new boolean[count];
        if (didWin) {
            wins[wins.length - 1] = true;
            while (trueCount(wins) != MIN_GAMES) {
                wins[rand.nextInt(wins.length)] = true;
            }
        }
        else {
            while (trueCount(wins) != count - MIN_GAMES) {
                wins[rand.nextInt(wins.length - 1)] = true;
            }
        }

        return wins;
    }

    private static int trueCount(boolean[] wins) {
        int count = 0;
        for (boolean b : wins) {
            if (b) count++;
        }
        return count;
    }

    private static class MatchSummaryGenerator {

        public static MatchSummary generateMatchSummary(boolean didWin) {

            int goalsWinner = rand.nextInt(MAX_GOALS) + 1;
            int goalsLoser = rand.nextInt(goalsWinner);

            return new MatchSummary(
                    didWin ? goalsWinner : goalsLoser,
                    didWin ? goalsLoser : goalsWinner,
                    didWin);
        }
    }
}
