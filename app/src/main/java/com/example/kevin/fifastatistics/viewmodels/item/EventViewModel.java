package com.example.kevin.fifastatistics.viewmodels.item;

import android.databinding.Bindable;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.managers.CrestUrlResizer;
import com.example.kevin.fifastatistics.managers.EventPresenter;
import com.example.kevin.fifastatistics.models.databasemodels.match.FifaEvent;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;

public abstract class EventViewModel<T extends FifaEvent> extends FifaBaseViewModel {

    T mEvent;
    private EventPresenter<T> mEventPresenter;

    EventViewModel(T event, Player user) {
        init(event, user);
    }

    public void setEvent(T event, Player user) {
        init(event, user);
        notifyChange();
    }

    private void init(T event, Player user) {
        mEvent = event;
        mEventPresenter = new EventPresenter<>(event, user);
        if (mEventPresenter.didUserParticipate()) {
            onInitEventUserPlayedIn(mEventPresenter.didUserWin());
        } else {
            onInitEventNotPlayedIn();
        }
    }

    protected void onInitEventUserPlayedIn(boolean didWin) {}
    protected void onInitEventNotPlayedIn() {}

    @Bindable
    public String getTopName() {
        return mEventPresenter.getTopName();
    }

    @Bindable
    public String getBottomName() {
        return mEventPresenter.getBottomName();
    }

    @Bindable
    public String getTopScore() {
        return mEventPresenter.getTopScore();
    }

    @Bindable
    public int getTopScoreTextAppearance() {
        return mEventPresenter.didTopWin() ? R.style.text_bold_white_small : R.style.text_white_small;
    }

    @Bindable
    public int getBottomScoreTextAppearance() {
        return mEventPresenter.didTopWin() ? R.style.text_white_small : R.style.text_bold_white_small;
    }

    @Bindable
    public String getBottomScore() {
        return mEventPresenter.getBottomScore();
    }

    @Bindable
    public String getTopImageUrl() {
        return CrestUrlResizer.resizeSmall(mEventPresenter.getTopTeamImageUrl());
    }

    @Bindable
    public String getBottomImageUrl() {
        return CrestUrlResizer.resizeSmall(mEventPresenter.getBottomTeamImageUrl());
    }

    public abstract void openEventDetail(View view);
}
