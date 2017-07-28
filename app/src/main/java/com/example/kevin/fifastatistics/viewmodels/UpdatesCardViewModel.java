package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.adapters.PendingUpdatesAdapter;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.utils.CollectionUtils;

import java.util.List;

public class UpdatesCardViewModel extends FifaBaseViewModel {

    private List<MatchUpdate> mMatchUpdates;
    private ActivityLauncher mLauncher;
    private PendingUpdatesAdapter mAdapter;

    public UpdatesCardViewModel(ActivityLauncher launcher, List<MatchUpdate> updates) {
        mLauncher = launcher;
        mMatchUpdates = updates;
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
        return !CollectionUtils.isEmpty(mMatchUpdates) ? View.VISIBLE : View.GONE;
    }

    @Override
    public void destroy() {
        super.destroy();
        mLauncher = null;
    }
}
