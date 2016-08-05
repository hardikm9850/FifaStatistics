package com.example.kevin.fifastatistics.managers;

import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.network.ApiAdapter;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Manages retrieving DB Objects from either local storage or using the REST API.
 */
public class RetrievalManager {

    private static final FifaApi API = ApiAdapter.getFifaApi();

    /**
     * Retrieve the user with the specified ID.
     * TODO error handling
     */
    public static Observable<User> getUser(String id) {
        return API.getUser(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<User> getCurrentUser() {
        return Observable.just(SharedPreferencesManager.getUser())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
