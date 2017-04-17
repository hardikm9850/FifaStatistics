package com.example.kevin.fifastatistics.utils;

import android.util.Log;

import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.models.patches.RegTokenPatch;
import com.example.kevin.fifastatistics.models.patches.UserTeamPatch;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.network.UserApi;

import lombok.experimental.UtilityClass;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@UtilityClass
public class UserUtils {

    private static final UserApi API = FifaApi.getUserApi();

    /** Write the current user to shared preferences and send a PUT to the server. */
    public static Observable<Response<Void>> updateUser(User user) {
        return performUserUpdate(user, AndroidSchedulers.mainThread(), Schedulers.io());
    }

    public static Observable<Response<Void>> updateUserSync(User user) {
        return performUserUpdate(user, Schedulers.immediate(), Schedulers.immediate());
    }

    private static Observable<Response<Void>> performUserUpdate(User user,
                                                                rx.Scheduler observeOn,
                                                                rx.Scheduler subscribeOn) {
        SharedPreferencesManager.storeUser(user);
        return API.updateUser(user.getId(), user)
                .observeOn(observeOn)
                .subscribeOn(subscribeOn)
                .onErrorReturn(t -> {
                    // TODO add task to synchronization manager
                    // TODO update item in nav drawer
                    return null;
                });
    }

    public static Observable<User> patchTeam(User user, Team team) {
        return API.patchTeam(user.getId(), new UserTeamPatch(team.getId()))
                .compose(ObservableUtils.applySchedulers());
    }

    public static Observable<User> patchRegToken(String userId, String regToken) {
        return API.patchRegToken(userId, new RegTokenPatch(regToken))
                .compose(ObservableUtils.applySchedulers());
    }

}
