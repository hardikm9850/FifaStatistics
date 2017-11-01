package com.example.kevin.fifastatistics.utils;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import lombok.experimental.UtilityClass;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

@UtilityClass
public class ObservableUtils {

    public static final Observer EMPTY_OBSERVER = new EmptyOnCompleteObserver() {
        @Override
        public void onError(Throwable e) {
            Log.d("ERROR", Log.getStackTraceString(e));
        }

        @Override
        public void onNext(Object o) {

        }
    };

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

    public static <T> Observable.Transformer<T, T> applyBackground() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate());
    }

    public static void unsubscribeCompositeSubscription(CompositeSubscription cs) {
        if (cs != null && cs.hasSubscriptions()) {
            cs.unsubscribe();
        }
    }

    public static abstract class EmptyOnCompleteObserver<T> implements Observer<T> {
        @Override
        public final void onCompleted() {}

        @Override
        public abstract void onError(Throwable e);

        @Override
        public abstract void onNext(T t);
    }

    public static abstract class OnNextObserver<T> extends EmptyOnCompleteObserver<T> {
        @Override
        public final void onError(Throwable e) {};

        @Override
        public abstract void onNext(T t);
    }

    public static Func1<Observable<? extends Throwable>, Observable<?>> getExponentialBackoffRetryWhen() {
        return error -> error.zipWith(Observable.range(1, 5),
                (Func2<Throwable, Integer, Integer>) (throwable, retryAttempt) -> retryAttempt)
                .flatMap((Func1<Integer, Observable<?>>) retryAttempt -> {
                    if (retryAttempt >= 0) {
                        return Observable.timer((long) Math.pow(2, retryAttempt), TimeUnit.SECONDS);
                    } else {
                        return Observable.error(null);
                    }
        });
    }
}
