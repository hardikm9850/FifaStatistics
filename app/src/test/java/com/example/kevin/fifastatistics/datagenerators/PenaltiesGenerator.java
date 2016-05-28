package com.example.kevin.fifastatistics.datagenerators;


import com.example.kevin.fifastatistics.models.match.Penalties;

import java.util.Random;

public class PenaltiesGenerator {

    public static final int MAX_PENALTIES = 9;
    public static final int MIN_PENALTIES = 5;

    public static Penalties generatePenalties() {

        Random rand = new Random();
        int goalsWinner = rand.nextInt(MAX_PENALTIES - MIN_PENALTIES + 1) + MIN_PENALTIES;
        int difference = (goalsWinner > MIN_PENALTIES) ? 1 : rand.nextInt(2) + 1;

        return new Penalties(goalsWinner, goalsWinner - difference);
    }
}
