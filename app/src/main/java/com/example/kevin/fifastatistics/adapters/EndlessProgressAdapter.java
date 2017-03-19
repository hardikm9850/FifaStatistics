package com.example.kevin.fifastatistics.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ItemProgressLoaderBinding;
import com.example.kevin.fifastatistics.interfaces.ProgressAdapter;
import com.example.kevin.fifastatistics.utils.ColorUtils;

public abstract class EndlessProgressAdapter extends RecyclerView.Adapter implements ProgressAdapter {

    static final int INVALID_VIEW_TYPE = -1;
    private static final int PROGRESS_VIEW_TYPE = 998;

    private boolean mIsNoMoreItemsToLoad = true;

    @Override
    public void notifyNoMoreItemsToLoad() {
        if (!mIsNoMoreItemsToLoad) {
            mIsNoMoreItemsToLoad = true;
            int insertionIndex = getActualItemCount();
            if (insertionIndex >= 0) {
                notifyItemRemoved(insertionIndex);
            }
        }
    }

    @Override
    public void notifyLoadingMoreItems() {
        if (mIsNoMoreItemsToLoad) {
            mIsNoMoreItemsToLoad = false;
            int insertionIndex = getActualItemCount();
            if (insertionIndex >= 0) {
                notifyItemInserted(insertionIndex);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getActualItemCount() && !mIsNoMoreItemsToLoad) {
            return PROGRESS_VIEW_TYPE;
        } else {
            return INVALID_VIEW_TYPE;
        }
    }

    public abstract int getActualItemCount();

    @Override
    public final int getItemCount() {
        if (!mIsNoMoreItemsToLoad) {
            return getActualItemCount() + 1;
        } else {
            return getActualItemCount();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == PROGRESS_VIEW_TYPE) {
            ItemProgressLoaderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_progress_loader, parent, false);
            return new ProgressViewHolder(binding);
        }
        return null;
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {

        ProgressViewHolder(ItemProgressLoaderBinding binding) {
            super(binding.getRoot());
            ColorUtils.setProgressBarColor(binding.progressItem, FifaApplication.getAccentColor());
        }
    }

    public boolean isNoMoreItemsToLoad() {
        return mIsNoMoreItemsToLoad;
    }

    public void setNoMoreItemsToLoad(boolean isNoMoreItemsToLoad) {
        this.mIsNoMoreItemsToLoad = isNoMoreItemsToLoad;
    }
}
