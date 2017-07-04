package com.example.kevin.fifastatistics.models.databasemodels.match;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model defining the result of a match that went to penalties.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Penalties implements Serializable {

    private int winner;
    private int loser;

    public boolean validate() {
        return winner != loser;
    }

    @Override
    public String toString() {
        return winner + "-" + loser;
    }
}
