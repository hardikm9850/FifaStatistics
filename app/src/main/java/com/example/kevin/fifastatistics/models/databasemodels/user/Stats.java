package com.example.kevin.fifastatistics.models.databasemodels.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Stats {

    @JsonIgnore
    private static final String[] names =
            {
                "Goals", "Shots", "Shots On Target", "Possession (%)", "Tackles", "Fouls",
                "Yellow Cards", "Red Cards", "Offsides", "Injuries", "Shot Accuracy (%)",
                "Pass Accuracy (%)"
            };

    private float goals;
    private float shots;
    private float shotsOnTarget;
    private float possession;
    private float tackles;
    private float fouls;
    private float yellowCards;
    private float redCards;
    private float offsides;
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
                redCards, offsides, injuries, shotAccuracy, passAccuracy};
    }

    public boolean validate() {
        return !(shots < shotsOnTarget || possession > 100 || shotAccuracy > 100 || passAccuracy > 100);
    }
}
