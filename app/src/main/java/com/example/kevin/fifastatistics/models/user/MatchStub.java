package com.example.kevin.fifastatistics.models.user;

/**
 * <b>Class:</b> MatchStub <br><br>
 * <b>Description:</b> <br>
 * The MatchStub class is meant to act as a 'stub' of a Match, and should exist
 * only within instances of a User or Series object. It is used as the matches
 * associated with a user or Series object, and the related full Match object can be
 * accessed through the 'id' property. It defines only a few traits: <ul>
 * <li> <b>id</b>, a reference to the full Match object represented by this MatchStub
 * <li> <b>opponent</b>, the name of the User the match was against
 * <li> <b>date</b>, the date the match was on
 * <li> <b>goalsFor</b>, the goals scored by the user
 * <li> <b>goalsAgainst</b>, the goals scored by the opponent
 * <li> <b>won</b>, true if the user won, false otherwise
 * </ul>
 * @version 1.0
 * @author Kevin
 *
 */
public class MatchStub {

    private String id;
    private String opponent;
    private String date;
    private int goalsFor;
    private int goalsAgainst;
    private boolean didWin;

    public MatchStub(String id, String opponent, String date,
                     int goalsFor, int goalsAgainst, boolean didWin) {
        this.id = id;
        this.opponent = opponent;
        this.date = date;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.didWin = didWin;
    }

    public boolean didWin() {
        return didWin;
    }

    public void setWin(boolean didWin) {
        this.didWin = didWin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }
}
