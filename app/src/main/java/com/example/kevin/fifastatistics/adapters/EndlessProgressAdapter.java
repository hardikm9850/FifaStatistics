package com.example.kevin.fifastatistics.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.interfaces.ProgressAdapter;

public abstract class EndlessProgressAdapter extends RecyclerView.Adapter implements ProgressAdapter {

    boolean noMoreItemsToLoad = true;
    static final int PROGRESS_VIEW_TYPE = 998;
    static final int INVALID_VIEW_TYPE = -1;

    @Override
    public void notifyNoMoreItemsToLoad() {
        if (!noMoreItemsToLoad) {
            noMoreItemsToLoad = true;
            int insertionIndex = getActualItemCount();
            if (insertionIndex >= 0) {
                notifyItemRemoved(insertionIndex);
            }
        }
    }

    @Override
    public void notifyLoadingMoreItems() {
        if (noMoreItemsToLoad) {
            noMoreItemsToLoad = false;
            int insertionIndex = getActualItemCount();
            if (insertionIndex >= 0) {
                notifyItemInserted(insertionIndex);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getActualItemCount() && !noMoreItemsToLoad) {
            return PROGRESS_VIEW_TYPE;
        } else {
            return INVALID_VIEW_TYPE;
        }
    }

    public abstract int getActualItemCount();

    @Override
    public final int getItemCount() {
        if (!noMoreItemsToLoad) {
            return getActualItemCount() + 1;
        } else {
            return getActualItemCount();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == PROGRESS_VIEW_TYPE) {
            return new ProgressViewHolder(LayoutInflater.from(context).inflate(R.layout.item_progress_loader, parent, false));
        }
        return null;
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

    public boolean isNoMoreItemsToLoad() {
        return noMoreItemsToLoad;
    }

    public void setNoMoreItemsToLoad(boolean noMoreItemsToLoad) {
        this.noMoreItemsToLoad = noMoreItemsToLoad;
    }
}
