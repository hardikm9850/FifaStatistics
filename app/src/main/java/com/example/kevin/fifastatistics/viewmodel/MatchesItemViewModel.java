package com.example.kevin.fifastatistics.viewmodel;

import android.databinding.Bindable;

import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;

public class MatchesItemViewModel extends FifaBaseViewModel {

    private MatchProjection mMatchProjection;
    private Player mUser;
    private String mStartName;
    private String mEndName;
    private int mStartScore;
    private int mEndScore;

    public MatchesItemViewModel(MatchProjection match, Player user) {
        init(match, user);
    }

    public void setMatch(MatchProjection match, Player user) {
        init(match, user);
        notifyChange();
    }

    private void init(MatchProjection match, Player user) {
        mMatchProjection = match;
        mUser = user;
        if (match.didPlayerWin(user) || match.didPlayerLose(user)) {
            mStartName = user.getName();
            mEndName = (match.didPlayerWin(user)) ? match.getLoserName() : match.getWinnerName();
            mStartScore = (match.didPlayerWin(user)) ? match.getGoalsWinner() : match.getGoalsLoser();
            mEndScore = (match.didPlayerWin(user)) ? match.getGoalsLoser() : match.getGoalsWinner();
        } else {
            mStartName = match.getWinnerName();
            mEndName = match.getLoserName();
            mStartScore = match.getGoalsWinner();
            mEndScore = match.getGoalsLoser();
        }
    }

    @Bindable
    public String getStartName() {
        return mStartName;
    }

    @Bindable
    public String getEndName() {
        return mEndName;
    }

    @Bindable
    public String getStartScore() {
        return String.valueOf(mStartScore);
    }

    @Bindable
    public String getEndScore() {
        return String.valueOf(mEndScore);
    }

}
