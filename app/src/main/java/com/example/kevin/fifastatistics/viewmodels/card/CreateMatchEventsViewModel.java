package com.example.kevin.fifastatistics.viewmodels.card;

import android.os.Bundle;
import android.util.Log;

import com.example.kevin.fifastatistics.data.Trie;
import com.example.kevin.fifastatistics.managers.FootballerLoader;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Completable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateMatchEventsViewModel extends FifaBaseViewModel {

    private final Trie<Footballer> mAutocompleteTrie = new Trie<>();
    private final Set<Team> mLoadedTeams = new HashSet<>();
    private FootballerLoader mLoader = new FootballerLoader();

    public CreateMatchEventsViewModel(Bundle savedInstanceState) {
        loadFootballers();
        restore(savedInstanceState);
    }

    private void loadFootballers() {
        mLoader.loadFootballers().subscribe(this::mergeFootballers);
    }

    private void mergeFootballers(List<Footballer> footballers) {
        Completable.fromAction(() -> addFootballersToTrie(footballers))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private synchronized void addFootballersToTrie(List<Footballer> footballers) {
        mAutocompleteTrie.insertAll(footballers);
    }

    private void restore(Bundle savedInstanceState) {

    }

    public void loadTeam(Team team) {
        if (!mLoadedTeams.contains(team)) {
            mLoader.loadTeamFootballers(team).subscribe(this::mergeFootballers);
            mLoadedTeams.add(team);
        }
    }

    @Override
    public Bundle saveInstanceState(Bundle bundle) {
        return super.saveInstanceState(bundle);
    }
}
