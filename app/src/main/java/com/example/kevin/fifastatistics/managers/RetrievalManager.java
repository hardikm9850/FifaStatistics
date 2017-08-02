package com.example.kevin.fifastatistics.managers;

import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.league.League;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Manages retrieving DB Objects from either local storage or network
 */
public class RetrievalManager {

    private static ApiListResponse<User> cachedUsers;
    private static ApiListResponse<League> cachedLeagues;
    private static Map<League, ApiListResponse<Team>> cachedTeams = new HashMap<>();

    public static Observable<User> getUser(String id) {
        return FifaApi.getUserApi().getUser(id).compose(ObservableUtils.applySchedulers());
    }

    public static Observable<User> getCurrentUser() {
        return Observable.<User>create(s -> s.onNext(PrefsManager.getUser())).compose(ObservableUtils.applySchedulers());
    }

    public static void syncCurrentUserWithServer() {
        getCurrentUser().flatMap(user -> getUser(user.getId())).subscribe(PrefsManager::storeUser);
    }

    public static Observable<ApiListResponse<User>> getUsers() {
        if (cachedUsers != null) {
            return Observable.just(cachedUsers);
        } else {
            return FifaApi.getUserApi().getUsers()
                    .compose(ObservableUtils.applySchedulers())
                    .map(users -> cachedUsers = users);
        }
    }

    public static Observable<List<MatchUpdate>> getLocalPendingUpdates() {
        return Observable.<List<MatchUpdate>>create(s -> s.onNext(PrefsManager.getMatchUpdates()))
                .compose(ObservableUtils.applyBackground());
    }

    public static Observable<List<User>> getUsersWithoutCurrentUser() {
        return RetrievalManager.getCurrentUser().flatMap(RetrievalManager::getUsersWithout);
    }

    private static Observable<List<User>> getUsersWithout(User user) {
        return getUsers().map(ApiListResponse::getItems).map(users -> {
            users.remove(user);
            return users;
        });
    }

    public static Observable<ApiListResponse<League>> getLeagues() {
        if (cachedLeagues != null) {
            return Observable.just(cachedLeagues);
        } else {
            return FifaApi.getLeagueApi().getLeagues()
                    .compose(ObservableUtils.applySchedulers())
                    .map(leagues -> cachedLeagues = leagues);
        }
    }

    public static Observable<ApiListResponse<Team>> getTeamsForLeague(League league) {
        if (league == null || league.getTeamUrl() == null) {
            return Observable.error(new IllegalArgumentException());
        } else {
            if (cachedTeams.containsKey(league)) {
                return Observable.just(cachedTeams.get(league));
            } else {
                return FifaApi.getLeagueApi().getTeams(league.getTeamUrl())
                        .compose(ObservableUtils.applySchedulers())
                        .map(team -> {
                            cachedTeams.put(league, team);
                            return team;
                        });
            }
        }
    }

    public static Observable<Team> getTeam(final String id) {
        if (id == null) {
            return Observable.just(null);
        } else {
            return FifaApi.getLeagueApi().getTeam(id).compose(ObservableUtils.applySchedulers());
        }
    }

    public static Observable<Match> getMatch(final String id) {
        if (id == null) {
            return Observable.error(new IllegalArgumentException("id is null"));
        } else {
            return FifaApi.getMatchApi().getMatch(id)
                    .compose(ObservableUtils.applySchedulers());
        }
    }
}
