package com.example.kevin.fifastatistics.viewmodels.card;

import android.os.Bundle;

import com.example.kevin.fifastatistics.data.Trie;
import com.example.kevin.fifastatistics.managers.FootballerLoader;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;

public class CreateMatchEventsViewModel extends FifaBaseViewModel {

    private Trie<Footballer> mAutocompleteTrie;

    public CreateMatchEventsViewModel(Team team1, Team team2, Bundle savedInstanceState) {
        loadFootballers();
        restore(savedInstanceState);
    }

    private void loadFootballers() {
        FootballerLoader loader = new FootballerLoader();
        loader.loadFootballers().subscribe(trie -> mAutocompleteTrie = trie);
    }

    private void restore(Bundle savedInstanceState) {

    }

    @Override
    public Bundle saveInstanceState(Bundle bundle) {
        return super.saveInstanceState(bundle);
    }
}
