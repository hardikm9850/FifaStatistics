package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.Bindable;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.match.Penalties;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;

public class MatchesItemViewModel extends FifaBaseViewModel implements EventViewModel<MatchProjection> {

    private MatchProjection mMatchProjection;
    private String mTopName;
    private String mBottomName;
    private int mTopScore;
    private int mBottomScore;
    private int mTopPenalties;
    private int mBottomPenalties;
    private boolean mDidTopWin;

    public MatchesItemViewModel(MatchProjection match, Player user) {
        init(match, user);
    }

    @Override
    public void setEvent(MatchProjection match, Player user) {
        init(match, user);
        notifyChange();
    }

    private void init(MatchProjection match, Player user) {
        mMatchProjection = match;
        Penalties penalties = match.getPenalties();
        int winnerPenalties = penalties != null ? penalties.getWinner() : 0;
        int loserPenalties = penalties != null ? penalties.getLoser() : 0;
        boolean didWin = match.didPlayerWin(user);
        if (didWin || match.didPlayerLose(user)) {
            initMatchUserPlayedIn(didWin, user, match, winnerPenalties, loserPenalties);
        } else {
            initMatchNotPlayedIn(match, winnerPenalties, loserPenalties);
        }
    }

    private void initMatchUserPlayedIn(boolean didWin, Player user, MatchProjection match, int winnerPenalties, int loserPenalties) {
        mTopName = user.getName();
        if (didWin) {
            mDidTopWin = true;
            mBottomName = match.getLoserName();
            mTopScore = match.getGoalsWinner();
            mBottomScore = match.getGoalsLoser();
            mTopPenalties = winnerPenalties;
            mBottomPenalties = loserPenalties;
        } else {
            mDidTopWin = false;
            mBottomName = match.getWinnerName();
            mTopScore = match.getGoalsLoser();
            mBottomScore = match.getGoalsWinner();
            mTopPenalties = loserPenalties;
            mBottomPenalties = winnerPenalties;
        }
    }

    private void initMatchNotPlayedIn(MatchProjection match, int winnerPenalties, int loserPenalties) {
        mDidTopWin = true;
        mTopName = match.getWinnerName();
        mBottomName = match.getLoserName();
        mTopScore = match.getGoalsWinner();
        mBottomScore = match.getGoalsLoser();
        mTopPenalties = winnerPenalties;
        mBottomPenalties = loserPenalties;
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
    public String getTopPenalties() {
        return String.valueOf(mTopPenalties);
    }

    @Bindable
    public String getBottomPenalties() {
        return String.valueOf(mBottomPenalties);
    }

    @Bindable
    public int getPenaltiesVisibility() {
        return mMatchProjection.getPenalties() != null ? View.VISIBLE : View.INVISIBLE;
    }

    public void openMatchDetail(View view) {
        // TODO
    }
}
