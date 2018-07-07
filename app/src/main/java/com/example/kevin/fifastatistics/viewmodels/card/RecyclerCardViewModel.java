package com.example.kevin.fifastatistics.viewmodels.card;

import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.adapters.AbstractCardAdapter;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.utils.CollectionUtils;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;

import java.util.List;

public abstract class RecyclerCardViewModel<T, ADAPTER extends AbstractCardAdapter<T, ?, ?>> extends
        FifaBaseViewModel {

    protected List<T> mItems;
    protected ActivityLauncher mLauncher;
    protected ADAPTER mAdapter;
    private boolean mIsCurrentUser;

    public RecyclerCardViewModel(List<T> items) {
        this(null, items, true);
    }

    public RecyclerCardViewModel(ActivityLauncher launcher, List<T> items, boolean isCurrentUser) {
        mLauncher = launcher;
        mItems = items;
        mIsCurrentUser = isCurrentUser;
        mAdapter = createAdapter();
    }

    @NonNull
    protected abstract ADAPTER createAdapter();

    public void setItems(List<T> items) {
        mItems = items;
        mAdapter.setItems(items);
        notifyPropertyChanged(BR.visibility);
    }

    public void removeItem(T item) {
        if (mAdapter.removeItem(item)) {
            notifyPropertyChanged(BR.visibility);
        }
    }

    public RecyclerView.Adapter<AbstractCardAdapter.ItemViewHolder> getAdapter() {
        return mAdapter;
    }

    @Bindable
    public int getVisibility() {
        return mIsCurrentUser && !CollectionUtils.isEmpty(mItems) ? View.VISIBLE : View.GONE;
    }

    @Override
    public void destroy() {
        super.destroy();
        mLauncher = null;
    }
}
