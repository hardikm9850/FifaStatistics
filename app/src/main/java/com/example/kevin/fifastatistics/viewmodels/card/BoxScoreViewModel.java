package com.example.kevin.fifastatistics.viewmodels.card;

import android.databinding.Bindable;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.kevin.fifastatistics.managers.EventPresenter;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchScoreSummary;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;

import static com.example.kevin.fifastatistics.models.databasemodels.match.MatchScoreSummary.*;

public class BoxScoreViewModel extends FifaBaseViewModel {

    private MatchScoreSummary mSummary;
    private EventPresenter<Match> mPresenter;

    public BoxScoreViewModel(@Nullable Match match, Player currentUser) {
        mPresenter = new EventPresenter<>(currentUser);
        initBoxscore(match);
    }

    private void initBoxscore(Match match) {
        mSummary = match != null ? match.getSummary() : null;
        mPresenter.init(match);
    }

    public void setMatch(Match match) {
        initBoxscore(match);
        notifyChange();
    }

    @Bindable
    public int getVisibility() {
        return mSummary != null ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public int getFirstExtraTimeVisibility() {
        return mSummary != null && mSummary.getFirstExtraTime() != null ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public int getSecondExtraTimeVisibility() {
        return mSummary != null && mSummary.getSecondExtraTime() != null ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public int getPenaltiesVisibility() {
        return mSummary != null && mSummary.getPenalties() != null ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public String getTopTeamImageUrl() {
        return mPresenter.getTopTeamImageUrl();
    }

    @Bindable
    public String getBottomTeamImageUrl() {
        return mPresenter.getBottomTeamImageUrl();
    }

    @Bindable
    public String getTopName() {
        return getFirstName(mPresenter.getTopName());
    }

    @Bindable
    public String getBottomName() {
        return getFirstName(mPresenter.getBottomName());
    }

    @Bindable
    public TeamSummary getTopTeamSummary() {
        return mPresenter.getTopBoxScore();
    }

    @Bindable
    public TeamSummary getBottomTeamSummary() {
        return mPresenter.getBottomBoxScore();
    }

    @Bindable
    public TextPartSummary getFirstHalf() {
        return mPresenter.getBoxScore().getFirstHalf().stringify();
    }

    @Bindable
    public TextPartSummary getSecondHalf() {
        return mPresenter.getBoxScore().getSecondHalf().stringify();
    }

    @Bindable
    public TextPartSummary getFirstExtraTime() {
        return mPresenter.getBoxScore().getFirstExtraTime().stringify();
    }

    @Bindable
    public TextPartSummary getSecondExtraTime() {
        return mPresenter.getBoxScore().getSecondExtraTime().stringify();
    }

    @Bindable
    public TextPartSummary getPenalties() {
        return mPresenter.getBoxScore().getPenalties().stringify();
    }

    @Bindable
    public TextPartSummary getFullTime() {
        return mPresenter.getBoxScore().getFullTime().stringify();
    }
}
