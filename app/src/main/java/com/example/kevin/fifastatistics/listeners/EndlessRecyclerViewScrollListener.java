package com.example.kevin.fifastatistics.listeners;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.tonicartos.superslim.LayoutManager;

public class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private static final int ROW_COUNT_VISIBLE_THRESHOLD = 5;

    private int visibleThreshold = ROW_COUNT_VISIBLE_THRESHOLD;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean mLoading = true;

    private RecyclerView.LayoutManager mLayoutManager;
    private ILoadMoreCallBack mLoadMoreCallBack;
    private final LAYOUT_MANAGER_TYPE mLayoutManagerType;

    public EndlessRecyclerViewScrollListener(LayoutManager layoutManager, ILoadMoreCallBack loadMoreCallBack) {
        init(layoutManager, loadMoreCallBack);
        mLayoutManagerType = LAYOUT_MANAGER_TYPE.STICKYLAYOUT;
    }

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager, ILoadMoreCallBack loadMoreCallBack) {
        init(layoutManager, loadMoreCallBack);
        mLayoutManagerType = LAYOUT_MANAGER_TYPE.LINEARLAYOUT;
    }

    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager, ILoadMoreCallBack loadMoreCallBack) {
        init(layoutManager, loadMoreCallBack);
        visibleThreshold = ROW_COUNT_VISIBLE_THRESHOLD * layoutManager.getSpanCount();
        mLayoutManagerType = LAYOUT_MANAGER_TYPE.GRIDVIEW;
    }

    public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager, ILoadMoreCallBack loadMoreCallBack) {
        init(layoutManager, loadMoreCallBack);
        visibleThreshold = ROW_COUNT_VISIBLE_THRESHOLD * layoutManager.getSpanCount();
        mLayoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRIDVIEW;
    }

    private void init(RecyclerView.LayoutManager layoutManager, ILoadMoreCallBack loadMoreCallBack) {
        mLayoutManager = layoutManager;
        mLoadMoreCallBack = loadMoreCallBack;
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            }
            else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = getLastVisibleItemPosition();
        int totalItemCount = mLayoutManager.getItemCount();

        resetListIfInvalidated(totalItemCount);
        updatePreviousItemCountIfLoading(totalItemCount);
        maybeLoadMoreData(lastVisibleItemPosition, totalItemCount, view);
    }

    private int getLastVisibleItemPosition() {
        int lastVisibleItemPosition = 0;
        switch (mLayoutManagerType) {
            case STICKYLAYOUT:
                lastVisibleItemPosition = ((LayoutManager) mLayoutManager).findLastVisibleItemPosition();
                break;
            case LINEARLAYOUT:
                lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                break;
            case STAGGERED_GRIDVIEW:
                int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
                break;
            case GRIDVIEW:
                lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                break;
        }
        return lastVisibleItemPosition;
    }

    private void resetListIfInvalidated(int totalItemCount) {
        if (totalItemCount < previousTotalItemCount - 1) {
            this.currentPage = 0;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.mLoading = true;
            } else {
                this.mLoading = false;
            }
        }
    }

    private void updatePreviousItemCountIfLoading(int totalItemCount) {
        if (mLoading && (totalItemCount > previousTotalItemCount + 1)) { // Account for loading item
            mLoading = false;
            previousTotalItemCount = totalItemCount;
        }
    }

    private void maybeLoadMoreData(int lastVisibleItemPosition, int totalItemCount, RecyclerView view) {
        boolean didBreachVisibleThreshold = !mLoading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount;
        if (didBreachVisibleThreshold) {
            currentPage++;
            if (mLoadMoreCallBack != null) {
                final int loadMorePage = currentPage;
                final int loadMoreItemCount = totalItemCount;
                view.post(() -> maybeCallLoadMoreCallback(loadMorePage, loadMoreItemCount));
                mLoading = true;
            }
        }
    }

    private void maybeCallLoadMoreCallback(int loadMorePage, int loadMoreItemCount) {
        if (mLoadMoreCallBack != null) {
            mLoadMoreCallBack.onLoadMore(loadMorePage, loadMoreItemCount);
        }
    }

    public interface ILoadMoreCallBack {
        void onLoadMore(int page, int totalItemsCount);
    }

    /**
     * Reset the loading flag if this stream is being refreshed and repopulated
     */
    public void resetLoading() {
        mLoading = false;
    }

    enum LAYOUT_MANAGER_TYPE {
        STICKYLAYOUT,
        LINEARLAYOUT,
        GRIDVIEW,
        STAGGERED_GRIDVIEW
    }
}
