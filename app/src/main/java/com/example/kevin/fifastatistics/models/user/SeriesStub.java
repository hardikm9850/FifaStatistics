package com.example.kevin.fifastatistics.models.user;

import java.util.ArrayList;

/**
 * <b>Class:</b> SeriesStub <br><br>
 * <b>Description:</b> <br>
 * The SeriesStub class is meant to act as a 'stub' of a Series, and should exist
 * only within instances of a User object. It is used as the series associated
 * with a User object, and the related full Series object can be accessed through
 * the 'id' property. It defines only a few traits: <ul>
 * <li> <b>id</b>, a reference to the full Series object represented by this SeriesStub
 * <li> <b>opponent</b>, the name of the User the series was against
 * <li> <b>date</b>, the date the series started on
 * <li> <b>matches</b>, A short summary of each match in the series
 * <li> <b>didWin</b>, true if the user won the series, false otherwise
 * </ul>
 * @version 1.0
 * @author Kevin
 *
 */
public class SeriesStub {

    public String id;
    public String opponent;
    public String date;
    public ArrayList<MatchSummary> matches;
    public boolean didWin;

    public SeriesStub(String id, String opponent, String date,
                      ArrayList<MatchSummary> matches, boolean didWin) {
        this.id = id;
        this.opponent = opponent;
        this.date = date;
        this.matches = matches;
        this.didWin = didWin;
    }

    /**
     * Adds a new Match Summary to the series.
     * @param goalsFor          The goals scored for the User in the match
     * @param goalsAgainst      The goals scored against the User in the match
     * @param didWin            Whether the user won or not
     */
    public void addMatchSummary(int goalsFor, int goalsAgainst, boolean didWin) {

        if (matches == null) {
            matches = new ArrayList<>();
        }
        else if (matches.size() == 7) {
            return;
        }

        matches.add(new MatchSummary(goalsFor, goalsAgainst, didWin));
    }

    /**
     * A short summary of each match in the series.
     */
    private class MatchSummary {

        public int goalsFor;
        public int goalsAgainst;
        public boolean didWin;

        public MatchSummary(int goalsFor, int goalsAgainst, boolean didWin) {
            this.goalsFor = goalsFor;
            this.goalsAgainst = goalsAgainst;
            this.didWin = didWin;
        }
    }


}