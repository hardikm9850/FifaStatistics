package com.example.kevin.fifastatistics.utils;

import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.network.ApiAdapter;
import com.example.kevin.fifastatistics.network.CreateFailedException;
import com.example.kevin.fifastatistics.network.FifaApi;

import lombok.experimental.UtilityClass;
import rx.Observable;
import rx.exceptions.Exceptions;

@UtilityClass
public class MatchUtils {

    private static final FifaApi API = ApiAdapter.getFifaApi();

    /**
     * Creates a match (sends a POST) and returns the created match observable.
     * @param match     The match to be created
     * @return  The match observable
     * @throws CreateFailedException if the POST fails.
     */
    public static Observable<Match> createMatch(Match match)
            throws CreateFailedException {
        return API.createMatch(match)
                .compose(ObservableUtils.applySchedulers())
                .onErrorReturn(t -> {throw Exceptions.propagate(new CreateFailedException(t));})
                .map(response -> {
                    String matchId = NetworkUtils.getIdFromResponse(response);
                    return Match.fromMatchWithId(match, matchId);
                });
    }

    public static boolean validateMatch(Match match) {
        boolean didGoToPenalties = match.getPenalties() != null;

        float goalsWinner = match.getStats().getStatsFor().getGoals();
        float goalsLoser = match.getStats().getStatsAgainst().getGoals();
        if (didGoToPenalties && ((goalsWinner != goalsLoser) || !match.getPenalties().validate())) {
            return false;
        } else if (!didGoToPenalties && (goalsWinner == goalsLoser)) {
            return false;
        } else {
            return (match.getStats().validate());
        }
    }
}
