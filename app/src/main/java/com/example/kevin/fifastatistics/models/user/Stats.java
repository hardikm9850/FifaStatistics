package com.example.kevin.fifastatistics.models.user;

import lombok.Getter;
import lombok.Setter;

/**
 * <b>Class:</b> Stats <br><br>
 * <b>Description:</b> <br>
 * The Stats class represents all of the User's main statistics, and should only exist
 * within instances of User objects. All of its properties are ints.
 */
@Getter
@Setter
public class Stats {

    private int goalsFor;
    private int goalsAgainst;
    private int shotsFor;
    private int shotsAgainst;
    private int shotsOnTarget;
    private int possession;
    private int tacklesFor;
    private int tacklesAgainst;
    private int fouls;
    private int redCards;
    private int offsides;
    private int shotAccuracy;
    private int passAccuracy;
}
