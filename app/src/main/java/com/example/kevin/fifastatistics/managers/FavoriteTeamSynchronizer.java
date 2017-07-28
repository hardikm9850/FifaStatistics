package com.example.kevin.fifastatistics.managers;

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
        return Observable.<String>create(s -> {
            Team favTeam = SharedPreferencesManager.getFavoriteTeam();
            if (mUser.getFavoriteTeamId() != null && favTeam == null) {
                s.onNext(mUser.getFavoriteTeamId());
            } else {
                s.onNext(null);
            }
        }).flatMap(id -> {
            if (id != null) {
                return FifaApi.getLeagueApi().getTeam(id);
            } else {
                return Observable.empty();
            }
        }).compose(ObservableUtils.applyBackground()).subscribe(new ObservableUtils.OnNextObserver<Team>() {
            @Override
            public void onNext(Team team) {
                if (team != null) {
                    SharedPreferencesManager.setFavoriteTeam(team);
                }
            }
        });
    }
}
