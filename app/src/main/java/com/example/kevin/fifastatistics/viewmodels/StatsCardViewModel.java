package com.example.kevin.fifastatistics.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;

public class StatsCardViewModel extends BaseObservable {

    private static final String MY_STATS_LEFT_HEADER;
    private static final String PLAYER_STATS_RIGHT_HEADER;

    static {
        Context context = FifaApplication.getContext();
        MY_STATS_LEFT_HEADER = context.getString(R.string.you);
        PLAYER_STATS_RIGHT_HEADER = context.getString(R.string.opponent).substring(0, 3);
    }

    private String mTitle;
    private String mLeftHeader;
    private String mRightHeader;
    private boolean mIsEventDetail;

    public static StatsCardViewModel myStats(Context context, Stats.Type type) {
        return initPlayerStatsViewModel(context, type, MY_STATS_LEFT_HEADER);
    }

    public static StatsCardViewModel playerStats(Context context, Stats.Type type, String name) {
        return initPlayerStatsViewModel(context, type, name);
    }

    private static StatsCardViewModel initPlayerStatsViewModel(Context context, Stats.Type type, String name) {
        StatsCardViewModel viewModel = new StatsCardViewModel(context, type);
        viewModel.mLeftHeader = name;
        viewModel.mRightHeader = PLAYER_STATS_RIGHT_HEADER;
        return viewModel;
    }

    public static StatsCardViewModel matchStats(Context context, @NonNull Match match, String username) {
        StatsCardViewModel viewModel = new StatsCardViewModel(context, Stats.Type.RECORDS);
        viewModel.mIsEventDetail = true;
        final String winnerName = match.getWinner().getName();
        final String loserName = match.getLoser().getName();
        if (winnerName.equals(username)) {
            viewModel.mLeftHeader = MY_STATS_LEFT_HEADER;
            viewModel.mRightHeader = match.getLoser().getFirstName();
        } else if (loserName.equals(username)) {
            viewModel.mLeftHeader = match.getWinner().getFirstName();
            viewModel.mRightHeader = MY_STATS_LEFT_HEADER;
        } else {
            viewModel.mLeftHeader = match.getWinner().getFirstName();
            viewModel.mRightHeader = match.getLoser().getFirstName();
        }
        return viewModel;
    }

    private StatsCardViewModel(Context context, Stats.Type type) {
        initTitle(context, type);
    }

    private void initTitle(Context context, Stats.Type type) {
        if (context != null && !mIsEventDetail) {
            if (type == Stats.Type.AVERAGES) {
                mTitle = context.getString(R.string.averages);
            } else {
                mTitle = context.getString(R.string.records);
            }
        }
    }

    @Bindable
    public int getTitleVisibility() {
        return mIsEventDetail ? View.GONE : View.VISIBLE;
    }

    @Bindable
    public String getTitle() {
        return mTitle;
    }

    public String getLeftHeader() {
        return mLeftHeader;
    }

    public String getRightHeader() {
        return mRightHeader;
    }
}
