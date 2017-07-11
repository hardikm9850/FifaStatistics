package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.Bindable;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.kevin.fifastatistics.managers.CrestUrlResizer;
import com.example.kevin.fifastatistics.models.databasemodels.match.FifaEvent;
import com.example.kevin.fifastatistics.models.databasemodels.match.PenaltyEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EventResultHeaderViewModel extends FifaBaseViewModel {

    private static final DateFormat DATE_FORMAT = SimpleDateFormat.getDateInstance();
    private static final String PENALTIES_MESSAGE = "%s wins %s on penalties";

    private FifaEvent mEvent;
    private PenaltyEvent mMatch;

    public EventResultHeaderViewModel(@Nullable FifaEvent event) {
        init(event);
    }

    private void init(FifaEvent event) {
        mEvent = event;
        mMatch = (mEvent instanceof PenaltyEvent) ? (PenaltyEvent) mEvent : null;
    }

    public void setEvent(FifaEvent event) {
        init(event);
        notifyChange();
    }

    @Bindable
    public int getHeaderVisibility() {
        return mEvent != null ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public String getDate() {
        return mEvent != null ? DATE_FORMAT.format(mEvent.getDate()) : null;
    }

    @SuppressWarnings("ConstantConditions")
    @Bindable
    public String getMessage() {
        if (doesEventHavePenalties()) {
            String penalties = mMatch.getPenalties().toString();
            return String.format(PENALTIES_MESSAGE, getFirstName(mEvent.getWinnerName()), penalties);
        } else {
            return null;
        }
    }

    @Bindable
    public int getMessageVisibility() {
        return doesEventHavePenalties() ? View.VISIBLE : View.GONE;
    }

    private boolean doesEventHavePenalties() {
        return mMatch != null && mMatch.getPenalties() != null;
    }

    @Bindable
    public String getWinnerImageUrl() {
        return mEvent != null ? CrestUrlResizer.resizeLarge(mEvent.getTeamWinnerImageUrl()) : null;
    }

    @Bindable
    public String getLoserImageUrl() {
        return mEvent != null ? CrestUrlResizer.resizeLarge(mEvent.getTeamLoserImageUrl()) : null;
    }

    @Bindable
    public String getWinnerName() {
        return mEvent != null ? getFirstName(mEvent.getWinnerName()) : null;
    }

    @Bindable
    public String getLoserName() {
        return mEvent != null ? getFirstName(mEvent.getLoserName()) : null;
    }

    @Bindable
    public String getWinnerScore() {
        return mEvent != null ? String.valueOf(mEvent.getScoreWinner()) : null;
    }

    @Bindable
    public String getLoserScore() {
        return mEvent != null ? String.valueOf(mEvent.getScoreLoser()) : null;
    }

    private String getFirstName(String name) {
        return name.split(" ")[0];
    }
}
