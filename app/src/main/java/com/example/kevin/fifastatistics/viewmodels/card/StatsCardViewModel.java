package com.example.kevin.fifastatistics.viewmodels.card;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.managers.StatsPresenter;
import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;

public class StatsCardViewModel extends BaseObservable {

    public static final String MY_STATS_LEFT_HEADER;
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

    public static StatsCardViewModel matchStats(Context context, StatsPresenter presenter) {
        StatsCardViewModel viewModel = new StatsCardViewModel(context, Stats.Type.RECORDS);
        viewModel.mIsEventDetail = true;
        viewModel.mLeftHeader = presenter.getLeftHeader();
        viewModel.mRightHeader = presenter.getRightHeader();
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
