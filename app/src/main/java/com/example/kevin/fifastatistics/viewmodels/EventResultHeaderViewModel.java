package com.example.kevin.fifastatistics.viewmodels;

import android.content.res.Resources;
import android.databinding.Bindable;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.managers.CrestUrlResizer;
import com.example.kevin.fifastatistics.managers.EventPresenter;
import com.example.kevin.fifastatistics.models.databasemodels.match.FifaEvent;
import com.example.kevin.fifastatistics.models.databasemodels.match.PenaltyEvent;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.match.SeriesProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.utils.ResourceUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EventResultHeaderViewModel extends FifaBaseViewModel {

    private static final DateFormat DATE_FORMAT = SimpleDateFormat.getDateInstance();
    private static final String PENALTIES_MESSAGE = "%s wins %s on penalties";

    private FifaEvent mEvent;
    private PenaltyEvent mMatch;
    private EventPresenter<FifaEvent> mPresenter;

    public EventResultHeaderViewModel(@Nullable FifaEvent event, Player currentUser) {
        mPresenter = new EventPresenter<>(currentUser);
        init(event);
    }

    private void init(FifaEvent event) {
        mEvent = event;
        mPresenter.init(event);
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
        } else if (mEvent instanceof Series || mEvent instanceof SeriesProjection) {
            Resources r = FifaApplication.getContext().getResources();
            int matches = (mEvent.getScoreWinner() * 2) - 1;
            return r.getString(R.string.best_of, matches);
        } else {
            return null;
        }
    }

    @Bindable
    public int getMessageVisibility() {
        return mMatch == null || mMatch.getPenalties() != null ? View.VISIBLE : View.GONE;
    }

    private boolean doesEventHavePenalties() {
        return mMatch != null && mMatch.getPenalties() != null;
    }

    @Bindable
    public String getWinnerImageUrl() {
        return CrestUrlResizer.resizeLarge(mPresenter.getTopTeamImageUrl());
    }

    @Bindable
    public String getLoserImageUrl() {
        return CrestUrlResizer.resizeLarge(mPresenter.getBottomTeamImageUrl());
    }

    @Bindable
    public String getWinnerName() {
        return getFirstName(mPresenter.getTopName());
    }

    @Bindable
    public String getLoserName() {
        return getFirstName(mPresenter.getBottomName());
    }

    @Bindable
    public String getWinnerScore() {
        return mPresenter.getTopScore();
    }

    @Bindable
    public String getLoserScore() {
        return mPresenter.getBottomScore();
    }
}
