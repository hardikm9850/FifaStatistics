package com.example.kevin.fifastatistics.datagenerators;

import java.util.UUID;

/**
 * Generates ID Strings.
 */
public class IdGenerator {

    public static String getRandomId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getRandomIdWithDashes() {
        return UUID.randomUUID().toString();
    }
}