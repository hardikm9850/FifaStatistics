package com.example.kevin.fifastatistics.models.databasemodels.user;

import android.content.res.Resources;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
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

    static {
        Resources r = FifaApplication.getContext().getResources();
        GOALS = r.getString(R.string.goals);
        SHOTS = r.getString(R.string.shots);
        SHOTS_ON_TARGET = r.getString(R.string.shots_on_target);
    }

    @JsonIgnore
    private static final String[] names =
            {
                GOALS, SHOTS, "Shots On Target", "Possession (%)", "Tackles", "Fouls",
                "Yellow Cards", "Red Cards", "Offsides", "Injuries", "Corners", "Shot Accuracy (%)",
                "Pass Accuracy (%)"
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

    /**
     * Get the set of names representing the items.
     * @return an array of the names
     */
    public static String[] getNameSet() {
        return names.clone();
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
}
