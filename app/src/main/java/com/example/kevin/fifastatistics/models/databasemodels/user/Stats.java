package com.example.kevin.fifastatistics.models.databasemodels.user;

import android.content.res.Resources;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * <b>Class:</b> Stats <br><br>
 * <b>Description:</b> <br>
 * The Stats class represents all of the User's menu_players statistics, and should only exist
 * within instances of User objects. All of its properties are ints.
 */
@Getter
@Setter
public class Stats implements Serializable {

    public static final String GOALS;
    public static final String SHOTS;
    public static final String SHOTS_ON_TARGET;
    public static final String POSSESSION;
    public static final String TACKLES;
    public static final String FOULS;
    public static final String YELLOW_CARDS;
    public static final String RED_CARDS;
    public static final String OFFSIDES;
    public static final String INJURIES;
    public static final String CORNERS;
    public static final String SHOT_ACCURACY;
    public static final String PASS_ACCURACY;
    public static final String PENALTIES;

    static {
        Resources r = FifaApplication.getContext().getResources();
        GOALS = r.getString(R.string.goals);
        SHOTS = r.getString(R.string.shots);
        SHOTS_ON_TARGET = r.getString(R.string.shots_on_target);
        POSSESSION = r.getString(R.string.possession_percent);
        TACKLES = r.getString(R.string.tackles);
        FOULS = r.getString(R.string.fouls);
        YELLOW_CARDS = r.getString(R.string.yellow_cards);
        RED_CARDS = r.getString(R.string.red_cards);
        OFFSIDES = r.getString(R.string.offsides);
        INJURIES = r.getString(R.string.injuries);
        CORNERS = r.getString(R.string.corners);
        SHOT_ACCURACY = r.getString(R.string.shot_accuracy_percent);
        PASS_ACCURACY = r.getString(R.string.pass_accuracy_percent);
        PENALTIES = r.getString(R.string.penalties);
    }

    @JsonIgnore
    private static final String[] names =
            {
                    GOALS, SHOTS, SHOTS_ON_TARGET, POSSESSION, TACKLES, FOULS, YELLOW_CARDS, RED_CARDS,
                    OFFSIDES, INJURIES, CORNERS, SHOT_ACCURACY, PASS_ACCURACY
            };

    public enum Type {
        AVERAGES, RECORDS;
    }

    private float goals;
    private float shots;
    private float shotsOnTarget;
    private float possession;
    private float tackles;
    private float fouls;
    private float yellowCards;
    private float redCards;
    private float offsides;
    private float corners;
    private float injuries;
    private float shotAccuracy;
    private float passAccuracy;

    public Stats(Stats stats) {
        goals = stats.goals;
        shots = stats.shots;
        shotsOnTarget = stats.shotsOnTarget;
        possession = stats.possession;
        tackles = stats.tackles;
        fouls = stats.fouls;
        yellowCards = stats.yellowCards;
        redCards = stats.redCards;
        offsides = stats.offsides;
        corners = stats.corners;
        injuries = stats.injuries;
        shotAccuracy = stats.shotAccuracy;
        passAccuracy = stats.passAccuracy;
    }

    @JsonCreator
    public Stats() {}

    /**
     * Get the set of names representing the items.
     * @return an array of the names
     */
    public static String[] getNameSet() {
        return names.clone();
    }

    @JsonIgnore
    public int getCardCount() {
        return Math.round(yellowCards) + Math.round(redCards);
    }

    /**
     * Build the set of values that correlates with the names returned by {@link #getNameSet()}.
     * @return an array of the values.
     */
    public float[] buildValueSet() {
        return new float[] {goals, shots, shotsOnTarget, possession, tackles, fouls, yellowCards,
                redCards, offsides, injuries, corners, shotAccuracy, passAccuracy};
    }

    public boolean validate() {
        return !(shots < shotsOnTarget || possession > 100 || shotAccuracy > 100 || passAccuracy > 100);
    }

    /**
     * Aggregates all the average stats over the count to get the total stats.
     */
    public Stats aggregate(int count) {
        Stats stats = new Stats();
        stats.goals = Math.round(goals*count);
        stats.shots = Math.round(shots*count);
        stats.shotsOnTarget = Math.round(shotsOnTarget*count);
        stats.possession = possession;
        stats.tackles = Math.round(tackles*count);
        stats.fouls = Math.round(fouls*count);
        stats.yellowCards = Math.round(yellowCards*count);
        stats.redCards = Math.round(redCards*count);
        stats.offsides = Math.round(offsides*count);
        stats.corners = Math.round(corners*count);
        stats.injuries = Math.round(injuries*count);
        stats.shotAccuracy = shotAccuracy;
        stats.passAccuracy = passAccuracy;
        return stats;
    }
}
