package com.example.kevin.fifastatistics.managers.sync;

import com.example.kevin.fifastatistics.listeners.SimpleObserver;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import rx.Observable;
import rx.Subscription;

public class FavoriteTeamSynchronizer {

    private final User mUser;

    public static FavoriteTeamSynchronizer with(User user) {
        return new FavoriteTeamSynchronizer(user);
    }

    private FavoriteTeamSynchronizer(User user) {
        mUser = user;
    }

    public Subscription sync() {
        Observable.OnSubscribe<String> favoriteTeamInPrefs = getFavoriteTeamIdInPreferences();
        Observable<Team> teamObservable = retrieveFavoriteTeam(favoriteTeamInPrefs);
        return saveFavoriteTeamToPrefs(teamObservable);
    }

    private Observable.OnSubscribe<String> getFavoriteTeamIdInPreferences() {
        return s -> {
            Team favTeam = PrefsManager.getFavoriteTeam();
            boolean favTeamNotSynced = mUser.getFavoriteTeamId() != null && favTeam == null;
            boolean favTeamHasNoFutId = favTeam != null && favTeam.getFutId() == 0;
            if (favTeamNotSynced || favTeamHasNoFutId) {
                s.onNext(mUser.getFavoriteTeamId());
            } else {
                s.onNext(null);
            }
        };
    }

    private Observable<Team> retrieveFavoriteTeam(Observable.OnSubscribe<String> teamId) {
        return Observable.create(teamId).flatMap(id -> {
            if (id != null) {
                return FifaApi.getLeagueApi().getTeam(id);
            } else {
                return Observable.empty();
            }
        });
    }

    private Subscription saveFavoriteTeamToPrefs(Observable<Team> teamObservable) {
        return teamObservable
                .compose(ObservableUtils.applyBackground())
                .subscribe(new SimpleObserver<Team>() {
                    @Override
                    public void onNext(Team team) {
                        if (team != null) {
                            PrefsManager.setFavoriteTeam(team);
                        }
                    }
        });
    }
}
