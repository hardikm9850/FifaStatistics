package com.example.kevin.fifastatistics.viewmodels.card;

import android.content.Context;
import android.databinding.Bindable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.kevin.fifastatistics.managers.CrestUrlResizer;
import com.example.kevin.fifastatistics.managers.EventPresenter;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchScoreSummary;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.utils.ColorUtils;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;

import static com.example.kevin.fifastatistics.models.databasemodels.match.MatchScoreSummary.TextPartSummary;

public class BoxScoreViewModel extends FifaBaseViewModel {

    private static final int TOTAL_LAYOUT_WEIGHT_SUM = 10;

    private MatchScoreSummary mSummary;
    private EventPresenter<Match> mPresenter;
    private Context mContext;

    public BoxScoreViewModel(@Nullable Match match, Player currentUser, Context context) {
        mPresenter = new EventPresenter<>(currentUser);
        mContext = context;
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

    @ColorInt
    public int getFullTimeTextColor() {
        return ColorUtils.getPrimaryTextColor(mContext);
    }

    @ColorInt
    public int getEachHalfTextColor() {
        return ColorUtils.getSecondaryTextColor(mContext);
    }

    @Bindable
    public String getTopTeamImageUrl() {
        return CrestUrlResizer.resizeSmall(mPresenter.getTopTeamImageUrl());
    }

    @Bindable
    public String getBottomTeamImageUrl() {
        return CrestUrlResizer.resizeSmall(mPresenter.getBottomTeamImageUrl());
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

    @Bindable
    public float getStartLayoutWeight() {
        final float layoutWeightInLayout = 4f;
        return layoutWeightInLayout + getNumberOfNotVisibleParts();
    }

    private int getNumberOfNotVisibleParts() {
        int penalties = getPenaltiesVisibility() > 0 ? 1 : 0;
        int secondExtraTime = getSecondExtraTimeVisibility() > 0 ? 1 : 0;
        int firstExtraTime = getFirstExtraTimeVisibility() > 0 ? 1 : 0;
        return penalties + secondExtraTime + firstExtraTime;
    }

    public int getTotalWeightSum() {
        return TOTAL_LAYOUT_WEIGHT_SUM;
    }

    @Bindable
    public float getEndLayoutWeight() {
        return TOTAL_LAYOUT_WEIGHT_SUM - getStartLayoutWeight();
    }

    @Bindable
    public int getEndWeightSum() {
        final int numberOfPartLayouts = 6;
        return numberOfPartLayouts - getNumberOfNotVisibleParts();
    }

    @Override
    public void destroy() {
        super.destroy();
        mContext = null;
    }
}
