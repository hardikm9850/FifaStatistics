package com.example.kevin.fifastatistics.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.event.ColorChangeEvent;
import com.example.kevin.fifastatistics.event.EventBus;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.utils.CollectionUtils;
import com.example.kevin.fifastatistics.viewmodels.item.ItemViewModel;

import java.util.List;

/**
 * Adapter for items inside a card view.
 */
public abstract class AbstractCardAdapter
        <T, BINDING extends ViewDataBinding, VIEWMODEL extends ItemViewModel<T>>
        extends RecyclerView.Adapter {

    private static final int HEADER_VIEW_TYPE = 1;
    private static final int ITEM_VIEW_TYPE = 2;

    protected List<T> mItems;
    private ActivityLauncher mLauncher;
    private int mButtonColor;
    private int mLayout;
    private boolean mIncludeHeader = false;

    public AbstractCardAdapter(List<T> items, ActivityLauncher launcher, @LayoutRes int layout) {
        mItems = items;
        mLauncher = launcher;
        mButtonColor = FifaApplication.getAccentColor();
        mLayout = layout;
        subscribeToColorChangeEvents();
    }

    protected void subscribeToColorChangeEvents() {
        EventBus.getInstance().observeEvents(ColorChangeEvent.class).subscribe(event -> {
            mButtonColor = event.color;
            notifyDataSetChanged();
        });
    }

    public void setItems(List<T> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public boolean removeItem(T item) {
        int indexRemoved = CollectionUtils.remove(mItems, item);
        if (indexRemoved >= 0) {
            notifyItemRemoved(indexRemoved);
            return true;
        } else {
            return false;
        }
    }

    protected void setIncludeHeader(boolean includeHeader) {
        mIncludeHeader = includeHeader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE) {
            BINDING binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    mLayout, parent, false);
            return new ItemViewHolder(binding);
        } else {
            return getHeaderViewHolder(parent);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int size = getItemCount();
        if (position < size) {
            if (isHeaderItem(position)) {
                bindHeader(holder);
            } else {
                int offset = mIncludeHeader ? 1 : 0;
                ((ItemViewHolder) holder).bind(mItems.get(position - offset), position == size - 1, position - offset);
            }
        }
    }

    @Override
    public int getItemCount() {
        int offset = mIncludeHeader ? 1 : 0;
        return CollectionUtils.getSize(mItems) + offset;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderItem(position)) {
            return HEADER_VIEW_TYPE;
        } else {
            return ITEM_VIEW_TYPE;
        }
    }

    private boolean isHeaderItem(int position) {
        return mIncludeHeader && position == 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private BINDING mBinding;

        ItemViewHolder(BINDING binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(T t, boolean isLastItem, int position) {
            VIEWMODEL viewModel = getBindingViewModel(mBinding);
            if (viewModel != null) {
                viewModel.setItem(t);
                onRebind(viewModel);
            } else {
                viewModel = createViewModel(t, mLauncher, isLastItem, mButtonColor, position);
                mBinding.setVariable(BR.viewModel, viewModel);
            }
            mBinding.executePendingBindings();
        }
    }

    protected abstract VIEWMODEL getBindingViewModel(BINDING binding);

    protected void onRebind(VIEWMODEL viewModel) {}

    protected RecyclerView.ViewHolder getHeaderViewHolder(ViewGroup parent) {
        throw new IllegalStateException("Must be overriden by child when using header");
    }

    protected <S extends RecyclerView.ViewHolder> void bindHeader(S header) {
        throw new IllegalStateException("Must be overriden by child when using header");
    }

    protected abstract VIEWMODEL createViewModel(T item, ActivityLauncher launcher, boolean isLastItem, int color, int position);
}
