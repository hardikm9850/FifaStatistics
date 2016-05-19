package com.example.kevin.fifastatistics.models.user;

import lombok.Getter;

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
@Getter
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
}
