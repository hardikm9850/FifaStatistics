package com.example.kevin.fifastatistics.viewmodels.card;

import android.content.Context;
import android.os.Bundle;

import com.example.kevin.fifastatistics.data.Trie;
import com.example.kevin.fifastatistics.managers.FootballerLoader;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Completable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.CardItem;
import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.GoalItem;
import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.InjuryItem;

public class CreateMatchEventsViewModel extends FifaBaseViewModel {

    private final Trie<Footballer> mAutocompleteTrie = new Trie<>();
    private final Set<Team> mLoadedTeams = new HashSet<>();
    private FootballerLoader mLoader = new FootballerLoader();
    private Team mTeamWinner;
    private Team mTeamLoser;

    private CreateMatchEventsCardViewModel<GoalItem> mGoalsCard;
    private CreateMatchEventsCardViewModel<CardItem> mCardsCard;
    private CreateMatchEventsCardViewModel<InjuryItem> mInjuriesCard;

    public CreateMatchEventsViewModel(Context context, Bundle savedInstanceState, Match match) {
        restore(savedInstanceState);
        loadFootballers();
        mTeamWinner = match.getTeamWinner();
        mTeamLoser = match.getTeamLoser();
        loadTeam(mTeamWinner);
        loadTeam(mTeamLoser);
        MatchEvents events = match.getEvents() != null ? match.getEvents() : new MatchEvents();
        mGoalsCard = new CreateMatchEventsCardViewModel<>(context, events.getGoals(), mAutocompleteTrie,
                mTeamWinner, mTeamLoser, GoalItem::new);
        mCardsCard = new CreateMatchEventsCardViewModel<>(context, events.getCards(), mAutocompleteTrie,
                mTeamWinner, mTeamLoser, CardItem::new);
        mInjuriesCard = new CreateMatchEventsCardViewModel<>(context, events.getInjuries(), mAutocompleteTrie,
                mTeamWinner, mTeamLoser, InjuryItem::new);
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
        mTeamWinner = team;
        mGoalsCard.setTeamWinner(team);
        mCardsCard.setTeamWinner(team);
        mInjuriesCard.setTeamWinner(team);
    }
    
    public void setTeamLoser(Team team) {
        loadTeam(team);
        mTeamLoser = team;
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

    public List<GoalItem> getGoals() {
        return mGoalsCard.getItems() != null ? mGoalsCard.getItems() : Collections.emptyList();
    }

    public List<CardItem> getCards() {
        return mGoalsCard.getItems() != null ? mCardsCard.getItems() : Collections.emptyList();
    }

    public List<InjuryItem> getInjuries() {
        return mGoalsCard.getItems() != null ? mInjuriesCard.getItems() : Collections.emptyList();
    }

    public Team getTeamWinner() {
        return mTeamWinner;
    }

    public boolean validateFieldsFilled() {
        return mGoalsCard.validateFieldsAreFilled() && mInjuriesCard.validateFieldsAreFilled() &&
                mCardsCard.validateFieldsAreFilled();
    }

    // was having issues with this, removing for now
    public boolean validateCorrectTeamCounts(Match match) {
        Team winner = match.getTeamWinner();
        Team loser = match.getTeamLoser();
        return mGoalsCard.validateCorrectTeamCounts(match.getGoalsPair(), winner, loser) &&
                mCardsCard.validateCorrectTeamCounts(match.getCardsPair(), winner, loser) &&
                mInjuriesCard.validateCorrectTeamCounts(match.getInjuriesPair(), winner, loser);
    }

    @Override
    public Bundle saveInstanceState(Bundle bundle) {
        return super.saveInstanceState(bundle);
    }
}
