package com.example.kevin.fifastatistics.utils;

import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Parses the text returned by OCR into a StatsPair.
 */
public class OcrResultParser {

    /** Value set for a statistic that could not be parsed. */
    public static final int ERROR_VALUE = -1;

    private static final String TAG = "OCR";

    private static final List<String> ZERO_MAPPINGS =
            Arrays.asList("o", "O", "D", "Q", "Cl", "c", "C", "I]", "[I", "(I", "I)", "ll", "II",
                    "I!", "!I", "I", "n", "fl", "a", "[1", "1]", "[l", "l]", "{I", "I}", "l}",
                    "{l", "u", "U");

    private int GOALS_LINE = 0;
    private int SHOTS_LINE = 1;
    private int SHOTS_ON_TARGET_LINE = 2;
    private int POSSESSION_LINE = 3;
    private int TACKLES_LINE = 4;
    private int FOULS_LINE = 5;
    private int YELLOW_CARDS_LINE = 6;
    private int RED_CARDS_LINE = 7;
    private int INJURIES_LINE = 8;
    private int OFFSIDES_LINE = 9;
    private int CORNERS_LINE = 10;
    private int SHOT_ACCURACY_LINE = 11;
    private int PASS_ACCURACY_LINE = 12;

    private String mResult;

    public static OcrResultParser newInstance(String result) {
        OcrResultParser p = new OcrResultParser();
        p.mResult = result;
        return p;
    }

    /**
     * Attempts to parse the OCR result into a {@link User.StatsPair}. Any values that were
     * unable to be parsed are set to <code>-1</code>.
     * @return  the parsed StatsPair
     * @throws IOException if the parsing cannot be done at all
     */
    public User.StatsPair parse() throws IOException {
        removeGarbagePunctuation();
        String[] lines = mResult.split("[\\r\\n]+");
        User.StatsPair sp = new User.StatsPair();

        if (lines[0].contains("Shots") || lines[0].contains("Shuts")) {
            decrementLines();
            if (lines[0].contains("Target") || lines[0].contains("Talget")) {
                decrementLines();
            }
        }

        int[] result;

        // Goals is very finicky
        if (GOALS_LINE >= 0) {
            result = parseLine(lines[GOALS_LINE]);
            sp.getStatsFor().setGoals(result[0]);
            sp.getStatsAgainst().setGoals(result[1]);
        } else {
            sp.getStatsFor().setGoals(ERROR_VALUE);
            sp.getStatsAgainst().setGoals(ERROR_VALUE);
        }

        if (SHOTS_LINE >= 0) {
            result = parseLine(lines[SHOTS_LINE]);
            sp.getStatsFor().setShots(result[0]);
            sp.getStatsAgainst().setShots(result[1]);
        } else {
            sp.getStatsFor().setShots(ERROR_VALUE);
            sp.getStatsAgainst().setShots(ERROR_VALUE);
        }

        try {
            result = parseLine(lines[SHOTS_ON_TARGET_LINE]);
            sp.getStatsFor().setShotsOnTarget(result[0]);
            sp.getStatsAgainst().setShotsOnTarget(result[1]);

            result = parseLine(lines[POSSESSION_LINE]);
            sp.getStatsFor().setPossession(result[0]);
            sp.getStatsAgainst().setPossession(result[1]);

            result = parseLine(lines[TACKLES_LINE]);
            sp.getStatsFor().setTackles(result[0]);
            sp.getStatsAgainst().setTackles(result[1]);

            result = parseLine(lines[FOULS_LINE]);
            sp.getStatsFor().setFouls(result[0]);
            sp.getStatsAgainst().setFouls(result[1]);

            result = parseLine(lines[YELLOW_CARDS_LINE]);
            sp.getStatsFor().setYellowCards(result[0]);
            sp.getStatsAgainst().setYellowCards(result[1]);

            result = parseLine(lines[RED_CARDS_LINE]);
            sp.getStatsFor().setRedCards(result[0]);
            sp.getStatsAgainst().setRedCards(result[1]);

            result = parseLine(lines[INJURIES_LINE]);
            sp.getStatsFor().setInjuries(result[0]);
            sp.getStatsAgainst().setInjuries(result[1]);

            result = parseLine(lines[OFFSIDES_LINE]);
            sp.getStatsFor().setOffsides(result[0]);
            sp.getStatsAgainst().setOffsides(result[1]);

            result = parseLine(lines[CORNERS_LINE]);
            sp.getStatsFor().setCorners(result[0]);
            sp.getStatsAgainst().setCorners(result[1]);

            result = parseLine(lines[SHOT_ACCURACY_LINE]);
            sp.getStatsFor().setShotAccuracy(result[0]);
            sp.getStatsAgainst().setShotAccuracy(result[1]);

            result = parseLine(lines[PASS_ACCURACY_LINE]);
            sp.getStatsFor().setPassAccuracy(result[0]);
            sp.getStatsAgainst().setPassAccuracy(result[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IOException("Failed to parse OCR result");
        }
        if (sp.getStatsFor().getTackles() > 30 || sp.getStatsAgainst().getTackles() > 30) {
            sp = StatsUtils.shiftStatsUpForPair(sp);
        }
        return sp;
    }

    private void removeGarbagePunctuation() {
        mResult = mResult.replace("°C", "%");
        mResult = mResult.replaceAll("[%';:`.‘?_“\"°-»«><]", "");
    }

    private static int[] parseLine(String line) {
        line = line.trim();
        String[] items = line.split(" ");

        int leftVal = parseItem(items[0]);
        int rightVal = parseItem(items[items.length - 1]);
        return new int[] {leftVal, rightVal};
    }

    private static int parseItem(String item) {
        try {
            int result = Integer.parseInt(item);
            return (result > 100) ? ERROR_VALUE : result;
        } catch (NumberFormatException e) {
            return mapStringToInt(item);
        }
    }

    private static int mapStringToInt(String item) {
        if (doesItemMapToZero(item)) return 0;
        else {
            item = removeZeroPunctuation(item);
            if (item.equalsIgnoreCase("s")) return 5;
            else if (item.equals("A") || item.equals("g")) return 4;
            else if (item.equals("B")) return 8;
            else if (item.equals("i") || item.equals("l")) return 1;
            else if (item.equals("T")) return 7;
            else if (item.equals("ID")) return 10;
            else if (item.equals("M")) return 11;
            else if (itemEndsInInt(item)) {
                if (item.charAt(item.length() - 2) == 'l') {
                    return 10 + getLastIntOfString(item);
                } else {
                    return ERROR_VALUE;
                }
            } else if (itemEndsInZeroMappedLetter(item)) {
                return parseStringEndingInZeroMappedLetter(item);
            } else return ERROR_VALUE;
        }
    }

    private static boolean doesItemMapToZero(String item) {
        return ZERO_MAPPINGS.contains(item);
    }

    private static String removeZeroPunctuation(String item) {
        item = item.replaceAll("[{}()]", "");
        item = item.replace("[", "");
        return item.replace("]", "");
    }

    private static boolean itemEndsInInt(String item) {
        if (item.length() == 0) return false;
        try {
            getLastIntOfString(item);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean itemEndsInZeroMappedLetter(String item) {
        if (item.length() == 0) return false;
        String mappedLetters="QOoDcC";
        return mappedLetters.contains(item.substring(item.length() - 1));
    }

    private static int getLastIntOfString(String item) {
        return Integer.valueOf(item.substring(item.length() - 1));
    }

    private static int parseStringEndingInZeroMappedLetter(String item) {
        if (item.length() == 1) {
            return 0;
        } else {
            try{
                return 10 * Integer.valueOf(item.substring(item.length() - 2, item.length() - 1));
            } catch (NumberFormatException ex) {
                return ERROR_VALUE;
            }
        }
    }

    private void decrementLines() {
        GOALS_LINE--;
        SHOTS_LINE--;
        SHOTS_ON_TARGET_LINE--;
        POSSESSION_LINE--;
        TACKLES_LINE--;
        FOULS_LINE--;
        YELLOW_CARDS_LINE--;
        RED_CARDS_LINE--;
        INJURIES_LINE--;
        OFFSIDES_LINE--;
        CORNERS_LINE--;
        SHOT_ACCURACY_LINE--;
        PASS_ACCURACY_LINE--;
    }
}
