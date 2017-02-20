package com.example.kevin.fifastatistics.utils;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.interfaces.ErrorHandler;
import com.example.kevin.fifastatistics.interfaces.OnMatchCreatedListener;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.network.ApiAdapter;
import com.example.kevin.fifastatistics.network.CreateFailedException;
import com.example.kevin.fifastatistics.network.FifaApi;

import lombok.experimental.UtilityClass;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.exceptions.Exceptions;

@UtilityClass
public class MatchUtils {

    private static final FifaApi API = ApiAdapter.getFifaApi();

    public static void createMatch(Match match, ErrorHandler errorHandler, OnMatchCreatedListener listener) {
        API.createMatch(match)
                .compose(ObservableUtils.applySchedulers())
                .subscribe(getCreateMatchObserver(match, errorHandler, listener));
    }

    private static Observer<Response<Void>> getCreateMatchObserver(final Match match, final ErrorHandler errorHandler, final OnMatchCreatedListener listener) {
        return new ObservableUtils.EmptyOnCompleteObserver<Response<Void>>() {
            @Override
            public void onError(Throwable e) {
                CreateFailedException cfe = new CreateFailedException(e);
                String errorMessage = ResourceUtils.getStringFromResourceId(R.string.error_create_match, cfe.getErrorCode().getMessage());
                errorHandler.handleError(errorMessage, cfe);
            }

            @Override
            public void onNext(Response<Void> response) {
                if (response != null) {
                    String matchId = NetworkUtils.getIdFromResponse(response);
                    listener.onMatchCreated(Match.fromMatchWithId(match, matchId));
                }
            }
        };
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
