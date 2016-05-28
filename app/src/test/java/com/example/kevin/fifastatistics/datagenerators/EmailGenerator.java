package com.example.kevin.fifastatistics.datagenerators;

import java.util.Random;

/**
 * Generates emails based off of the name provided.
 */
public class EmailGenerator {

    public static String generateEmailFromName(String name) {
        return name.replaceAll(" ", "_") + "@gmail.com";
    }

    public static String generateEmailFromNameWithNumbers(String name) {
        Random number = new Random();
        return name.replaceAll(" ", "_") + number.nextInt(500)+ "@gmail.com";
    }
}
