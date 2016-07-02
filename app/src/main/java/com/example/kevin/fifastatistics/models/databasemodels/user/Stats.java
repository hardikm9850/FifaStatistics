package com.example.kevin.fifastatistics.models.databasemodels.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    private static final String[] names =
            {
                "Goals", "Shots", "Shots On Target", "Possession (%)", "Tackles", "Fouls",
                "Yellow Cards", "Red Cards", "Offsides", "Injuries", "Shot Accuracy (%)",
                "Pass Accuracy (%)"
            };

    private int goals;
    private int shots;
    private int shotsOnTarget;
    private int possession;
    private int tackles;
    private int fouls;
    private int yellowCards;
    private int redCards;
    private int offsides;
    private int injuries;
    private int shotAccuracy;
    private int passAccuracy;

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
    public int[] buildValueSet() {
        return new int[] {goals, shots, shotsOnTarget, possession, tackles, fouls, yellowCards,
                redCards, offsides, injuries, shotAccuracy, passAccuracy};
    }
}
