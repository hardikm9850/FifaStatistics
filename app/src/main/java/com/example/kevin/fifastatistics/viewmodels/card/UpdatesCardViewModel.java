package com.example.kevin.fifastatistics.viewmodels.card;

import android.support.annotation.NonNull;

import com.example.kevin.fifastatistics.adapters.PendingUpdatesAdapter;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import java.util.List;

public class UpdatesCardViewModel extends RecyclerCardViewModel<MatchUpdate, PendingUpdatesAdapter> {

    public UpdatesCardViewModel(ActivityLauncher launcher, List<MatchUpdate> updates, User user) {
        super(launcher, updates, user.getId().equals(PrefsManager.getUserId()));
    }

    @NonNull
    @Override
    protected PendingUpdatesAdapter createAdapter() {
        return new PendingUpdatesAdapter(mItems, mLauncher);
    }
}
