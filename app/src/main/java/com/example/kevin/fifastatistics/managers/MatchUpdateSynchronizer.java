package com.example.kevin.fifastatistics.managers;

import com.example.kevin.fifastatistics.interfaces.Consumer;
import com.example.kevin.fifastatistics.listeners.SimpleObserver;
import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import java.util.List;

import lombok.Builder;
import rx.Observable;
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

    public void sync() {
        int pendingUpdateCount = user.getPendingUpdateCount();
        if (pendingUpdateCount > 0) {
            Observable.<List<MatchUpdate>>create(s -> s.onNext(SharedPreferencesManager.getMatchUpdates()))
                    .compose(ObservableUtils.applySchedulers())
                    .subscribe(updates -> {
                        if (pendingUpdateCount != updates.size()) {
                            syncMatchUpdates(user.getId());
                        } else {
                            complete();
                        }
                    });
        } else {
            complete();
        }
    }

    private void syncMatchUpdates(String userId) {
        FifaApi.getUpdateApi().getUpdatesForUser(userId)
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
                        List<MatchUpdate> updates = response.getItems();
                        SharedPreferencesManager.setMatchUpdates(updates);
                        succeed(updates);
                        complete();
                    }
                });
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
