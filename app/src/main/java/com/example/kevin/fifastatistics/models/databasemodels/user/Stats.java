package com.example.kevin.fifastatistics.models.databasemodels.user;

import android.util.Log;

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

    public boolean validate() {
        return !(shots < shotsOnTarget || possession > 100 || shotAccuracy > 100 || passAccuracy > 100);
    }

    public void updateAverages(Stats stats, int totalCount) {
        goals = ((totalCount * goals) + stats.goals) / totalCount;
        shots = ((totalCount * shots) + stats.shots) / totalCount;
        shotsOnTarget = ((totalCount * shotsOnTarget) + stats.shotsOnTarget) / totalCount;
        possession = ((totalCount * possession) + stats.possession) / totalCount;
        tackles = ((totalCount * tackles) + stats.tackles) / totalCount;
        fouls = ((totalCount * fouls) + stats.fouls) / totalCount;
        yellowCards = ((totalCount * yellowCards) + stats.yellowCards) / totalCount;
        redCards = ((totalCount * redCards) + stats.redCards) / totalCount;
        offsides = ((totalCount * offsides) + stats.offsides) / totalCount;
        injuries = ((totalCount * injuries) + stats.injuries) / totalCount;
        shotAccuracy = ((totalCount * shotAccuracy) + stats.shotAccuracy) / totalCount;
        passAccuracy = ((totalCount * passAccuracy) + stats.passAccuracy) / totalCount;
    }

    public void updateRecords(Stats stats) {
        goals = (goals > stats.goals) ? goals : stats.goals;
        shots = (shots > stats.shots) ? shots : stats.shots;
        shotsOnTarget = (shotsOnTarget > stats.shotsOnTarget) ? shotsOnTarget : stats.shotsOnTarget;
        possession = (possession > stats.possession) ? possession : stats.possession;
        tackles = (tackles > stats.tackles) ? tackles : stats.tackles;
        fouls = (fouls > stats.fouls) ? fouls : stats.fouls;
        yellowCards = (yellowCards > stats.yellowCards) ? yellowCards : stats.yellowCards;
        redCards = (redCards > stats.redCards) ? redCards : stats.redCards;
        offsides = (offsides > stats.offsides) ? offsides : stats.offsides;
        injuries = (injuries > stats.injuries) ? injuries : stats.injuries;
        shotAccuracy = (shotAccuracy > stats.shotAccuracy) ? shotAccuracy : stats.shotAccuracy;
        passAccuracy = (passAccuracy > stats.passAccuracy) ? passAccuracy : stats.passAccuracy;
    }
}
