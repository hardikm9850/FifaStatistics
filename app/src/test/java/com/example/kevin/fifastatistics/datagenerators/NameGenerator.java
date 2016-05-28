package com.example.kevin.fifastatistics.datagenerators;

import java.util.Random;

public class NameGenerator {

    private static final String[] firstNames =
            {
                    "Eyshr", "Kevin", "Nelson", "Kelsey", "Keith", "Tim", "Matt",
                    "Tom", "Derrick", "Damian", "Zafar", "Lucas", "Aidan", "Alex",
                    "Nathan", "Wesley"
            };

    private static final String[] lastNames =
            {
                    "Sahota", "Grant", "Hoang", "Rayner", "Alexander", "Tang", "Hinton",
                    "Ghag", "Modelo", "Goeres", "Ali", "Kops", "Singer", "Dunbar",
                    "Parade", "McDonald"
            };

    public static String generateRandomFullName() {
        return generateRandomFirstName() + " " + generateRandomLastName();
    }

    public static String generateRandomFirstName() {
        Random index =  new Random();
        return firstNames[index.nextInt(firstNames.length)];
    }

    public static String generateRandomLastName() {
        Random index =  new Random();
        return lastNames[index.nextInt(lastNames.length)];
    }
}
