package com.example.kevin.fifastatistics.viewmodels.card;

import android.support.annotation.NonNull;

import com.example.kevin.fifastatistics.adapters.PendingUpdatesAdapter;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.CollectionUtils;

import java.util.List;

public class UpdatesCardViewModel extends RecyclerCardViewModel<MatchUpdate, PendingUpdatesAdapter> {

    public UpdatesCardViewModel(ActivityLauncher launcher, List<MatchUpdate> updates, User user, boolean isCurrentUser) {
        super(launcher, updates, isCurrentUser);
    }

    @NonNull
    @Override
    protected PendingUpdatesAdapter createAdapter() {
        return new PendingUpdatesAdapter(mItems, mLauncher);
    }

    @Override
    public void setItems(List<MatchUpdate> items) {
        if (CollectionUtils.getSize(items) + CollectionUtils.getSize(mItems) > 0) {
            super.setItems(items);
        }
    }
}
