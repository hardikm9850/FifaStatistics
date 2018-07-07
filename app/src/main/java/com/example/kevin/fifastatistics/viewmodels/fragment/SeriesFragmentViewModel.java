package com.example.kevin.fifastatistics.viewmodels.fragment;

import android.databinding.Bindable;
import android.view.View;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.listeners.SimpleObserver;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.match.SeriesProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.card.LeadersCardViewModel;
import com.example.kevin.fifastatistics.viewmodels.card.SeriesMatchesCardViewModel;
import com.example.kevin.fifastatistics.viewmodels.card.StatsCardViewModel;

import rx.Subscription;

public class SeriesFragmentViewModel extends ProgressFragmentViewModel {

    private SeriesFragmentViewModel.OnSeriesLoadedListener mListener;
    private SeriesProjection mProjection;
    private StatsCardViewModel mStatsViewModel;
    private LeadersCardViewModel mLeadersViewModel;
    private SeriesMatchesCardViewModel mMatchesViewModel;
    private Series mSeries;
    private String mSeriesId;
    private User mUser;
    private ActivityLauncher mLauncher;

    public SeriesFragmentViewModel(SeriesFragmentViewModel.OnSeriesLoadedListener listener, SeriesProjection projection,
                                  User user, String seriesId, ActivityLauncher launcher) {
        mListener = listener;
        mProjection = projection;
        mUser = user;
        mSeriesId = seriesId;
        mLauncher = launcher;
    }

    public void loadSeries() {
        if (mSeries != null) {
            return;
        }
        showProgressBar();
        String id = mProjection != null ? mProjection.getId() : mSeriesId;
        Subscription s = RetrievalManager.getSeries(id).subscribe(new SimpleObserver<Series>() {
            @Override
            public void onError(Throwable e) {
                hideProgressBar();
                showRetryButton();
                if (mListener != null) {
                    mListener.onSeriesLoadFailed(e);
                }
            }

            @Override
            public void onNext(Series series) {
                hideProgressBar();
                mSeries = series;
                if (mListener != null) {
                    mListener.onSeriesLoaded(series);
                }
                notifySeriesLoaded();
            }
        });
        addSubscription(s);
    }

    private void notifySeriesLoaded() {
        mMatchesViewModel = new SeriesMatchesCardViewModel(mLauncher, mSeries.getMatches(), mUser, mSeries.getWinner());
        mStatsViewModel = StatsCardViewModel.seriesStats(mSeries, getUsername());
        if (mSeries.getLeaders() != null) {
            mLeadersViewModel = LeadersCardViewModel.series(mSeries, mUser, mLauncher);
        }
        if (mProjection != null) {
            notifyPropertyChanged(BR.statsVisibility);
            notifyPropertyChanged(BR.stats);
            notifyPropertyChanged(BR.leaders);
            notifyPropertyChanged(BR.series);
            notifyPropertyChanged(BR.matches);
            notifyPropertyChanged(BR.visibility);
            notifyPropertyChanged(BR.leadersVisibility);
        }
    }

    @Override
    public void onRetryButtonClick() {
        loadSeries();
    }

    @Override
    public void destroy() {
        super.destroy();
        mListener = null;
        mLauncher = null;
    }

    @Bindable
    public int getHeaderVisibility() {
        return mProjection == null && mSeries == null ? View.GONE : View.VISIBLE;
    }

    @Bindable
    public int getStatsVisibility() {
        return mSeries != null ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public StatsCardViewModel getStats() {
        return mStatsViewModel;
    }

    @Bindable
    public LeadersCardViewModel getLeaders() {
        return mLeadersViewModel;
    }

    @Bindable
    public SeriesMatchesCardViewModel getMatches() {
        return mMatchesViewModel;
    }

    @Bindable
    public int getLeadersVisibility() {
        return visibleIf(mLeadersViewModel != null);
    }

    @Bindable
    public Series getSeries() {
        return mSeries;
    }

    public void setSeries(Series series) {
        mSeries = series;
        if (series != null && mListener != null) {
            mListener.onSeriesLoaded(series);
        }
    }

    public String getUsername() {
        return mUser.getName();
    }

    public interface OnSeriesLoadedListener {
        void onSeriesLoaded(Series series);
        void onSeriesLoadFailed(Throwable t);
    }
}
