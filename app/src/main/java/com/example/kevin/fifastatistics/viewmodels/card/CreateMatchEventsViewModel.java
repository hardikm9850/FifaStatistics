package com.example.kevin.fifastatistics.viewmodels.card;

import android.content.Context;
import android.os.Bundle;

import com.example.kevin.fifastatistics.data.Trie;
import com.example.kevin.fifastatistics.managers.FootballerLoader;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Completable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.*;

public class CreateMatchEventsViewModel extends FifaBaseViewModel {

    private final Trie<Footballer> mAutocompleteTrie = new Trie<>();
    private final Set<Team> mLoadedTeams = new HashSet<>();
    private FootballerLoader mLoader = new FootballerLoader();

    private CreateMatchEventsCardViewModel<GoalItem> mGoalsCard;
    private CreateMatchEventsCardViewModel<CardItem> mCardsCard;
    private CreateMatchEventsCardViewModel<InjuryItem> mInjuriesCard;

    public CreateMatchEventsViewModel(Context context, Bundle savedInstanceState, Team team1, Team team2) {
        restore(savedInstanceState);
        loadFootballers();
        loadTeam(team1);
        loadTeam(team2);
        mGoalsCard = new CreateMatchEventsCardViewModel<>(context, null, mAutocompleteTrie,
                team1, team2, GoalItem::new);
        mCardsCard = new CreateMatchEventsCardViewModel<>(context, null, mAutocompleteTrie,
                team1, team2, CardItem::new);
        mInjuriesCard = new CreateMatchEventsCardViewModel<>(context, null, mAutocompleteTrie,
                team1, team2, InjuryItem::new);
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

    private void loadTeam(Team team) {
        if (team != null && !mLoadedTeams.contains(team)) {
            mLoader.loadTeamFootballers(team).subscribe(this::mergeFootballers);
            mLoadedTeams.add(team);
        }
    }

    public void setTeamWinner(Team team) {
        loadTeam(team);
        mGoalsCard.setTeamWinner(team);
        mCardsCard.setTeamWinner(team);
        mInjuriesCard.setTeamWinner(team);
    }
    
    public void setTeamLoser(Team team) {
        loadTeam(team);
        mGoalsCard.setTeamLoser(team);
        mCardsCard.setTeamLoser(team);
        mInjuriesCard.setTeamLoser(team);
    }

    public void setGoalCount(int count) {
        mGoalsCard.setCount(count);
    }

    public void setCardCount(int count) {
        mCardsCard.setCount(count);
    }

    public void setInjuryCount(int count) {
        mInjuriesCard.setCount(count);
    }

    public CreateMatchEventsCardViewModel<GoalItem> getGoalsViewModel() {
        return mGoalsCard;
    }

    public CreateMatchEventsCardViewModel<CardItem> getCardsViewModel() {
        return mCardsCard;
    }

    public CreateMatchEventsCardViewModel<InjuryItem> getInjuriesViewModel() {
        return mInjuriesCard;
    }

    @Override
    public Bundle saveInstanceState(Bundle bundle) {
        return super.saveInstanceState(bundle);
    }
}
