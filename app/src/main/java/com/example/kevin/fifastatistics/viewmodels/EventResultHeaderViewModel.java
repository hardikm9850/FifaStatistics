package com.example.kevin.fifastatistics.viewmodels;

import android.view.View;

import com.example.kevin.fifastatistics.managers.CrestUrlResizer;
import com.example.kevin.fifastatistics.models.databasemodels.match.FifaEvent;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EventResultHeaderViewModel extends FifaBaseViewModel {

    private static final DateFormat DATE_FORMAT = SimpleDateFormat.getDateInstance();
    private static final String PENALTIES_MESSAGE = "%s wins %s on penalties";

    private final FifaEvent mEvent;
    private final MatchProjection mMatch;

    public EventResultHeaderViewModel(FifaEvent event) {
        mEvent = event;
        mMatch = (mEvent instanceof MatchProjection) ? (MatchProjection) mEvent : null;
    }

    public String getDate() {
        return DATE_FORMAT.format(mEvent.getDate());
    }

    public String getMessage() {
        if (doesEventHavePenalties()) {
            String penalties = mMatch.getPenalties().toString();
            return String.format(PENALTIES_MESSAGE, mMatch.getWinnerFirstName(), penalties);
        } else {
            return null;
        }
    }

    public int getMessageVisibility() {
        return doesEventHavePenalties() ? View.VISIBLE : View.GONE;
    }

    private boolean doesEventHavePenalties() {
        return mMatch != null && mMatch.getPenalties() != null;
    }

    public String getWinnerImageUrl() {
        return CrestUrlResizer.resizeLarge(mEvent.getTeamWinnerImageUrl());
    }

    public String getLoserImageUrl() {
        return CrestUrlResizer.resizeLarge(mEvent.getTeamLoserImageUrl());
    }

    public String getWinnerName() {
        return getFirstName(mEvent.getWinnerName());
    }

    public String getLoserName() {
        return getFirstName(mEvent.getLoserName());
    }

    public String getWinnerScore() {
        return String.valueOf(mEvent.getScoreWinner());
    }

    public String getLoserScore() {
        return String.valueOf(mEvent.getScoreLoser());
    }

    private String getFirstName(String name) {
        return name.split(" ")[0];
    }
}
