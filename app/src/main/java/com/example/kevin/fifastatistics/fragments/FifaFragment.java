package com.example.kevin.fifastatistics.fragments;

/**
 * Created by Kevin on 4/30/2016.
 */
public interface FifaFragment
{
    /**
     * Performs any actions required by the fragment on back press.
     * @return false if the activity should continue with onBackPress actions,
     *         true otherwise.
     */
    boolean handleBackPress();
}
