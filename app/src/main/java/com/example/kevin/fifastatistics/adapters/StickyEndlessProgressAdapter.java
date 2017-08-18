package com.example.kevin.fifastatistics.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.models.databasemodels.match.FifaEvent;
import com.example.kevin.fifastatistics.utils.EventUtils;
import com.tonicartos.superslim.LayoutManager;
import com.tonicartos.superslim.LinearSLM;

import java.util.ArrayList;
import java.util.List;

public abstract class StickyEndlessProgressAdapter
        <HEADER_BINDING extends ViewDataBinding, ITEM_BINDING extends ViewDataBinding, E extends FifaEvent>
        extends EndlessProgressAdapter {

    private static final int HEADER_VIEW_TYPE = 4;
    private static final int CONTENT_VIEW_TYPE = 5;

    private List<LineItem<E>> mItems;
    private List<E> mEvents;
    private final int mHeaderLayoutId;
    private final int mEventLayoutId;
    private int mHeaderCount;
    private int mSectionFirstPosition;

    StickyEndlessProgressAdapter(@LayoutRes int headerLayout, @LayoutRes int eventLayout) {
        mItems = new ArrayList<>();
        mEvents = new ArrayList<>();
        mHeaderLayoutId = headerLayout;
        mEventLayoutId = eventLayout;
    }

    public void setEvents(List<E> events) {
        if (events != null) {
            mEvents = events;
            processEventsIntoItems(0, events.size());
            notifyDataSetChanged();
        }
    }

    public void notifyEventsAdded(int positionStart, int itemCount) {
        if (mEvents != null) {
            final int previousHeaderCount = mHeaderCount;
            processEventsIntoItems(positionStart, positionStart + itemCount);
            notifyItemRangeInserted(positionStart + previousHeaderCount, itemCount + mHeaderCount - previousHeaderCount);
        }
    }

    private void processEventsIntoItems(int startPos, int itemCount) {
        for (int i = startPos; i < itemCount; i++) {
            E previousEvent = getEventAtPosition(i - 1);
            E currentEvent = mEvents.get(i);
            if (!EventUtils.isEventsOnSameDay(previousEvent, currentEvent)) {
                mSectionFirstPosition = mHeaderCount + i;
                mHeaderCount++;
                mItems.add(new LineItem<>(true, mSectionFirstPosition, currentEvent));
            }
            mItems.add(new LineItem<>(false, mSectionFirstPosition, currentEvent));
        }
    }

    private E getEventAtPosition(int position) {
        if (mEvents != null && position < mEvents.size() && position >= 0) {
            return mEvents.get(position);
        } else {
            return null;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_VIEW_TYPE) {
            HEADER_BINDING binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), mHeaderLayoutId, parent, false);
            return createHeaderViewHolder(binding);
        } else if (viewType == CONTENT_VIEW_TYPE) {
            ITEM_BINDING binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), mEventLayoutId, parent, false);
            return createEventViewHolder(binding);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    protected abstract BindingViewHolder<HEADER_BINDING, E> createHeaderViewHolder(HEADER_BINDING binding);

    protected abstract BindingViewHolder<ITEM_BINDING, E> createEventViewHolder(ITEM_BINDING binding);

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LayoutManager.LayoutParams params = LayoutManager.LayoutParams.from(holder.itemView.getLayoutParams());
        params.setSlm(LinearSLM.ID);
        if (holder instanceof BindingViewHolder) {
            BindingViewHolder<?, E> itemHolder = (BindingViewHolder) holder;
            final LineItem<E> item = mItems.get(position);
            if (position < mItems.size()) {
                itemHolder.bindItem(item.event);
            }
            params.setFirstPosition(item.sectionFirstPosition);
            itemHolder.itemView.setLayoutParams(params);
            itemHolder.mBinding.executePendingBindings();
        } else if (holder instanceof ProgressViewHolder) {
            params.setFirstPosition(mItems.get(position - 1).sectionFirstPosition);
            holder.itemView.setLayoutParams(params);
        }
    }

    @Override
    public int getActualItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = super.getItemViewType(position);
        if (viewType != INVALID_VIEW_TYPE) {
            return viewType;
        } else {
            return mItems.get(position).isHeader ? HEADER_VIEW_TYPE : CONTENT_VIEW_TYPE;
        }
    }

    static abstract class BindingViewHolder<T extends ViewDataBinding, S extends FifaEvent> extends RecyclerView.ViewHolder {

        T mBinding;

        BindingViewHolder(T binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        abstract void bindItem(S item);
    }

    private static class LineItem<T extends FifaEvent> {
        int sectionFirstPosition;
        boolean isHeader;
        T event;

        LineItem(boolean isHeader, int sectionFirstPosition, T event) {
            this.isHeader = isHeader;
            this.sectionFirstPosition = sectionFirstPosition;
            this.event = event;
        }
    }
}
