package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.adapters.PendingUpdatesAdapter;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.CollectionUtils;

import java.util.List;

public class UpdatesCardViewModel extends FifaBaseViewModel {

    private List<MatchUpdate> mMatchUpdates;
    private ActivityLauncher mLauncher;
    private PendingUpdatesAdapter mAdapter;
    private boolean mIsCurrentUser;

    public UpdatesCardViewModel(ActivityLauncher launcher, List<MatchUpdate> updates, User user) {
        mLauncher = launcher;
        mMatchUpdates = updates;
        mIsCurrentUser = user.getId().equals(PrefsManager.getUserId());
        mAdapter = new PendingUpdatesAdapter(mMatchUpdates, mLauncher);
    }

    public void setPendingUpdates(List<MatchUpdate> updates) {
        mMatchUpdates = updates;
        mAdapter.setUpdates(updates);
        notifyPropertyChanged(BR.visibility);
    }

    public void removePendingUpdate(MatchUpdate update) {
        if (mAdapter.removeUpdate(update)) {
            notifyPropertyChanged(BR.visibility);
        }
    }

    public RecyclerView.Adapter<PendingUpdatesAdapter.PendingUpdateViewHolder> getAdapter() {
        return mAdapter;
    }

    @Bindable
    public int getVisibility() {
        return mIsCurrentUser && !CollectionUtils.isEmpty(mMatchUpdates) ? View.VISIBLE : View.GONE;
    }

    @Override
    public void destroy() {
        super.destroy();
        mLauncher = null;
    }
}
