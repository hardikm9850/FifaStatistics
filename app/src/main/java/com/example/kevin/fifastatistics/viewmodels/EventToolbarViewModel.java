package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.kevin.fifastatistics.models.databasemodels.match.FifaEvent;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

public class EventToolbarViewModel extends BaseObservable {

    private final String mLeftImageUrl;
    private final String mRightImageUrl;
    private final int mLeftScore;
    private final int mRightScore;

    public EventToolbarViewModel(FifaEvent event, User currentUser) {
        if (currentUser.didLose(event)) {
            mLeftImageUrl = event.getTeamLoserImageUrl();
            mRightImageUrl = event.getTeamWinnerImageUrl();
            mLeftScore = event.getScoreLoser();
            mRightScore = event.getScoreWinner();
        } else {
            mLeftImageUrl = event.getTeamWinnerImageUrl();
            mRightImageUrl = event.getTeamLoserImageUrl();
            mLeftScore = event.getScoreWinner();
            mRightScore = event.getScoreLoser();
        }
    }

    @Bindable
    public String getLeftImage() {
        return mLeftImageUrl;
    }

    @Bindable
    public String getRightImage() {
        return mRightImageUrl;
    }

    @Bindable
    public String getScore() {
        return mLeftScore + " - " + mRightScore;
    }
}
