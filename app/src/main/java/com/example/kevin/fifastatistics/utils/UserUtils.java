package com.example.kevin.fifastatistics.utils;

import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.ApiAdapter;
import com.example.kevin.fifastatistics.network.FifaApi;

import lombok.experimental.UtilityClass;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@UtilityClass
public class UserUtils {

    private static final FifaApi API = ApiAdapter.getFifaApi();

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

        // SynchronizationManager.syncUser(user)...

        return API.updateUser(user.getId(), user)
                .observeOn(observeOn)
                .subscribeOn(subscribeOn)
                .onErrorReturn(t -> {
                    // TODO add task to synchronization manager
                    // TODO update item in nav drawer
                    return null;
                });
    }
}
