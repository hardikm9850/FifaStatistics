package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.Bindable;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.CurrentSeries;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.viewmodels.card.CurrentSeriesCardViewModel;
import com.example.kevin.fifastatistics.viewmodels.card.RecordsCardViewModel;
import com.example.kevin.fifastatistics.viewmodels.card.UpdatesCardViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

public class UserOverviewViewModel extends FifaBaseViewModel {

    private User mUser;
    private RecordsCardViewModel mRecords;
    private UserOverviewViewModelInteraction mInteraction;
    private UpdatesCardViewModel mUpdatesViewModel;
    private CurrentSeriesCardViewModel mCurrentSeriesViewModel;
    private List<MatchUpdate> mMatchUpdates;

    public UserOverviewViewModel(User user) {
        this(user, null, null, null);
    }

    public UserOverviewViewModel(User user, UserOverviewViewModelInteraction interaction,
                                 List<MatchUpdate> updates, ActivityLauncher launcher) {
        mUser = user;
        mInteraction = interaction;
        mRecords = new RecordsCardViewModel(user);
        mUpdatesViewModel = new UpdatesCardViewModel(launcher, updates, user);
        mMatchUpdates = updates;
        initCurrentSeriesCard(launcher, user);
    }

    private void initCurrentSeriesCard(ActivityLauncher launcher, User currentUser) {
        boolean isCurrentUser = launcher != null;
        List<CurrentSeries> series = PrefsManager.getSeriesPrefs().getCurrentSeries();
        mCurrentSeriesViewModel = new CurrentSeriesCardViewModel(launcher, series, isCurrentUser, currentUser);
    }

    public void update() {
        if (mUser != null) {
            Subscription s = FifaApi.getUserApi().getUser(mUser.getId())
                    .compose(ObservableUtils.applySchedulers())
                    .subscribe(new ObservableUtils.EmptyOnCompleteObserver<User>() {
                @Override
                public void onError(Throwable e) {
                    if (mInteraction != null) {
                        mInteraction.onUserUpdateFailure();
                    }
                }

                @Override
                public void onNext(User user) {
                    mUser = user;
                    mRecords.setUser(user);
                    notifyPropertyChanged(BR.stats);
                    notifyPropertyChanged(BR.records);
                    if (mInteraction != null) {
                        mInteraction.onUserUpdateSuccess(user);
                    }
                }
            });
            addSubscription(s);
        }
    }

    @Bindable
    public RecordsCardViewModel getRecords() {
        return mRecords;
    }

    @Bindable
    public UpdatesCardViewModel getUpdates() {
        return mUpdatesViewModel;
    }

    @Bindable
    public CurrentSeriesCardViewModel getSeries() {
        return mCurrentSeriesViewModel;
    }

    @Bindable
    public List<User.StatsPair> getStats() {
        if (mUser != null) {
            List<User.StatsPair> stats = new ArrayList<>();
            stats.add(mUser.getAverageStats());
            stats.add(mUser.getRecordStats());
            return stats;
        } else {
            return null;
        }
    }

    @Bindable
    public String getName() {
        return mUser != null ? mUser.getName() : null;
    }

    @Bindable
    public String getImageUrl() {
        return mUser != null ? mUser.getImageUrl() : null;
    }

    public void setPendingUpdates(List<MatchUpdate> updates) {
        mUpdatesViewModel.setItems(updates);
    }

    public void removePendingUpdate(MatchUpdate update) {
        mUpdatesViewModel.removeItem(update);
    }

    public void setCurrentSeries(List<CurrentSeries> series) {
        mCurrentSeriesViewModel.setItems(series);
    }

    public void removeSeriesWithOpponentId(String opponentId) {
        mCurrentSeriesViewModel.removeSeriesWithOpponentId(opponentId);
    }

    @Override
    public void destroy() {
        super.destroy();
        mInteraction = null;
        mUpdatesViewModel.destroy();
    }

    public interface UserOverviewViewModelInteraction {
        void onUserUpdateSuccess(User user);
        void onUserUpdateFailure();
    }
}
