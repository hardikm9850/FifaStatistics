package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.Bindable;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.listeners.SimpleObserver;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.CurrentSeries;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.viewmodels.card.CurrentSeriesCardViewModel;
import com.example.kevin.fifastatistics.viewmodels.card.LeadersCardViewModel;
import com.example.kevin.fifastatistics.viewmodels.card.RecordsCardViewModel;
import com.example.kevin.fifastatistics.viewmodels.card.UpdatesCardViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;

public class UserOverviewViewModel extends FifaBaseViewModel {

    private User mUser;
    private RecordsCardViewModel mRecords;
    private UserOverviewViewModelInteraction mInteraction;
    private UpdatesCardViewModel mUpdatesViewModel;
    private CurrentSeriesCardViewModel mCurrentSeriesViewModel;
    private LeadersCardViewModel mLeadersViewModel;

    public UserOverviewViewModel(User user) {
        this(user, null, null);
    }

    public UserOverviewViewModel(User user, UserOverviewViewModelInteraction interaction, ActivityLauncher launcher) {
        mUser = user;
        mInteraction = interaction;
        mRecords = new RecordsCardViewModel(user);
        boolean isCurrentUser = interaction != null;
        mUpdatesViewModel = new UpdatesCardViewModel(launcher, null, user, isCurrentUser);
        mLeadersViewModel = new LeadersCardViewModel(user.getLeaders(), user.getName(), launcher, isCurrentUser);
        initCurrentSeriesCard(launcher, user, isCurrentUser);
    }

    private void initCurrentSeriesCard(ActivityLauncher launcher, User currentUser, boolean isCurrentUser) {
        mCurrentSeriesViewModel = new CurrentSeriesCardViewModel(launcher, null, isCurrentUser, currentUser);
        Observable.fromCallable(() -> PrefsManager.getSeriesPrefs().getCurrentSeries())
                .compose(ObservableUtils.applySchedulers())
                .subscribe(series -> mCurrentSeriesViewModel.setItems(series));
    }

    public void update() {
        if (mUser != null) {
            Subscription s = FifaApi.getUserApi().getUser(mUser.getId())
                    .compose(ObservableUtils.applySchedulers())
                    .subscribe(new SimpleObserver<User>() {
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
                    mLeadersViewModel.setLeaders(user.getLeaders());
                    notifyPropertyChanged(BR.stats);
                    notifyPropertyChanged(BR.records);
                    notifyPropertyChanged(BR.leaders);
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
    public LeadersCardViewModel getLeaders() {
        return mLeadersViewModel;
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
