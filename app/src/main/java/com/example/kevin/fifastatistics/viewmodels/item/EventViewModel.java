package com.example.kevin.fifastatistics.viewmodels.item;

import android.databinding.Bindable;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.managers.CrestUrlResizer;
import com.example.kevin.fifastatistics.models.databasemodels.match.FifaEvent;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;

public abstract class EventViewModel<T extends FifaEvent> extends FifaBaseViewModel {

    T mEvent;
    private String mTopName;
    private String mBottomName;
    private String mTopImageUrl;
    private String mBottomImageUrl;
    private int mTopScore;
    private int mBottomScore;
    private boolean mDidTopWin;

    EventViewModel(T event, Player user) {
        init(event, user);
    }

    public void setEvent(T event, Player user) {
        init(event, user);
        notifyChange();
    }

    private void init(T event, Player user) {
        mEvent = event;
        boolean didWin = event.getWinnerId().equals(user.getId());
        if (didWin || event.getLoserId().equals(user.getId())) {
            initEventUserPlayedIn(didWin, user, event);
            onInitEventUserPlayedIn(didWin);
        } else {
            initEventNotPlayedIn(event);
            onInitEventNotPlayedIn();
        }
    }

    protected void onInitEventUserPlayedIn(boolean didWin) {}
    protected void onInitEventNotPlayedIn() {}

    private void initEventUserPlayedIn(boolean didWin, Player user, T event) {
        mTopName = user.getName();
        if (didWin) {
            mDidTopWin = true;
            mBottomName = event.getLoserName();
            mTopImageUrl = event.getTeamWinnerImageUrl();
            mBottomImageUrl = event.getTeamLoserImageUrl();
            mTopScore = getScoreWinner();
            mBottomScore = getScoreLoser();
        } else {
            mDidTopWin = false;
            mBottomName = event.getWinnerName();
            mTopImageUrl = event.getTeamLoserImageUrl();
            mBottomImageUrl = event.getTeamWinnerImageUrl();
            mTopScore = getScoreLoser();
            mBottomScore = getScoreWinner();
        }
    }

    protected abstract int getScoreWinner();
    protected abstract int getScoreLoser();

    private void initEventNotPlayedIn(T event) {
        mDidTopWin = true;
        mTopName = event.getWinnerName();
        mBottomName = event.getLoserName();
        mTopImageUrl = event.getTeamWinnerImageUrl();
        mBottomImageUrl = event.getTeamLoserImageUrl();
        mTopScore = getScoreWinner();
        mBottomScore = getScoreLoser();
    }

    @Bindable
    public String getTopName() {
        return mTopName;
    }

    @Bindable
    public String getBottomName() {
        return mBottomName;
    }

    @Bindable
    public String getTopScore() {
        return String.valueOf(mTopScore);
    }

    @Bindable
    public int getTopScoreTextAppearance() {
        return mDidTopWin ? R.style.text_bold_white_small : R.style.text_white_small;
    }

    @Bindable
    public int getBottomScoreTextAppearance() {
        return mDidTopWin ? R.style.text_white_small : R.style.text_bold_white_small;
    }

    @Bindable
    public String getBottomScore() {
        return String.valueOf(mBottomScore);
    }

    @Bindable
    public String getTopImageUrl() {
        return CrestUrlResizer.resizeSmall(mTopImageUrl);
    }

    @Bindable
    public String getBottomImageUrl() {
        return CrestUrlResizer.resizeSmall(mBottomImageUrl);
    }

    public abstract void openEventDetail(View view);
}
