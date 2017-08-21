package com.example.kevin.fifastatistics.managers.sync;

import com.example.kevin.fifastatistics.interfaces.Consumer;
import com.example.kevin.fifastatistics.listeners.SimpleObserver;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import java.util.List;

import lombok.Builder;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;

public class MatchUpdateSynchronizer {

    private final User user;
    private final Consumer<Throwable> onSyncErrorHandler;
    private final Consumer<List<MatchUpdate>> onSyncSuccessHandler;
    private final Action0 onSyncCompleteHandler;

    @Builder
    private MatchUpdateSynchronizer(User user, Consumer<Throwable> onSyncErrorHandler,
                                   Consumer<List<MatchUpdate>> onSyncSuccessHandler, Action0 onSyncCompleteHandler) {
        this.user = user;
        this.onSyncErrorHandler = onSyncErrorHandler;
        this.onSyncSuccessHandler = onSyncSuccessHandler;
        this.onSyncCompleteHandler = onSyncCompleteHandler;
    }

    public Subscription sync() {
        int pendingUpdateCount = user.getPendingUpdateCount();
        if (pendingUpdateCount > 0) {
            return Observable.<List<MatchUpdate>>create(s -> s.onNext(PrefsManager.getMatchUpdates()))
                    .flatMap(this::syncMatchUpdates)
                    .compose(ObservableUtils.applySchedulers())
                    .subscribe(new SimpleObserver<ApiListResponse<MatchUpdate>>() {
                        @Override
                        public void onError(Throwable e) {
                            if (onSyncErrorHandler != null) {
                                onSyncErrorHandler.accept(e);
                            }
                        }

                        @Override
                        public void onNext(ApiListResponse<MatchUpdate> response) {
                            complete();
                            if (response != null) {
                                List<MatchUpdate> updates = response.getItems();
                                PrefsManager.setMatchUpdates(updates);
                                succeed(updates);
                            }
                        }
                    });
        } else {
            PrefsManager.setMatchUpdates(null);
            complete();
            return null;
        }
    }

    private Observable<ApiListResponse<MatchUpdate>> syncMatchUpdates(List<MatchUpdate> matches) {
        if (user.getPendingUpdateCount() != matches.size()) {
            return FifaApi.getUpdateApi().getUpdatesForUser(user.getId());
        } else {
            return Observable.just(null);
        }
    }

    private void succeed(List<MatchUpdate> updates) {
        if (onSyncSuccessHandler != null) {
            onSyncSuccessHandler.accept(updates);
        }
    }

    private void complete() {
        if (onSyncCompleteHandler != null) {
            onSyncCompleteHandler.call();
        }
    }
}
