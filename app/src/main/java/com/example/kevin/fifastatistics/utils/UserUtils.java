package com.example.kevin.fifastatistics.utils;

import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.ApiAdapter;
import com.example.kevin.fifastatistics.network.FifaApi;

import lombok.experimental.UtilityClass;
import retrofit2.Response;
import rx.Observable;

@UtilityClass
public class UserUtils {

    private static final FifaApi API = ApiAdapter.getFifaApi();

    /** Write the current user to shared preferences and send a PUT to the server. */
    public static Observable<Response<Void>> updateUser(User user) {
        SharedPreferencesManager.storeUser(user);

        // SynchronizationManager.syncUser(user)...

        return API.updateUser(user.getId(), user)
                .compose(ObservableUtils.applySchedulers())
                .onErrorReturn(t -> {
                    // TODO add task to synchronization manager
                    // TODO update item in nav drawer
                    return null;
                });
    }
}
