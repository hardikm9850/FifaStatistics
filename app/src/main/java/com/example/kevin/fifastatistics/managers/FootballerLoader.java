package com.example.kevin.fifastatistics.managers;

import com.example.kevin.fifastatistics.data.Trie;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.utils.StorageUtils;

import java.util.List;

import rx.Observable;

import static com.example.kevin.fifastatistics.managers.sync.FootballersSynchronizer.FOOTBALLERS_FILE;

public class FootballerLoader {

    private static final String TAG = "FootballerLoader";

    private static Trie<Footballer> sFootballers;

    public Observable<Trie<Footballer>> loadFootballers() {
        if (sFootballers != null) {
            return Observable.just(sFootballers);
        } else {
            return loadFootballersTrie();
        }
    }

    private Observable<Trie<Footballer>> loadFootballersTrie() {
        Observable<List<Footballer>> readResult = StorageUtils.readListFromDisk(FOOTBALLERS_FILE);
        return readResult
                .onErrorResumeNext(throwable -> null)
                .map(Trie::with)
                .doOnNext(trie -> sFootballers = trie)
                .compose(ObservableUtils.applySchedulers());
    }
}
