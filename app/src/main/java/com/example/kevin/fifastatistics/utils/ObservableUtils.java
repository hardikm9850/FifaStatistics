package com.example.kevin.fifastatistics.utils;

import lombok.experimental.UtilityClass;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@UtilityClass
public class ObservableUtils {

    /**
     * Apply the typical schedulers to an observable. Subscribes on Schedulers.io(), and observers
     * on the main thread.
     * <p>
     * Use it inside the compose() method.
     */
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
