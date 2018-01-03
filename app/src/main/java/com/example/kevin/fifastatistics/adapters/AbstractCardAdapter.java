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
        extends RecyclerView.Adapter<AbstractCardAdapter.ItemViewHolder> {

    protected List<T> mItems;
    private ActivityLauncher mLauncher;
    private int mButtonColor;
    private int mLayout;

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

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BINDING binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                mLayout, parent, false);
        return new ItemViewHolder(binding);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(AbstractCardAdapter.ItemViewHolder holder, int position) {
        int size = CollectionUtils.getSize(mItems);
        if (position < size) {
            holder.bind(mItems.get(position), position == size - 1);
        }
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.getSize(mItems);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private BINDING mBinding;

        ItemViewHolder(BINDING binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(T t, boolean isLastItem) {
            VIEWMODEL viewModel = getBindingViewModel(mBinding);
            if (viewModel != null) {
                viewModel.setItem(t);
                onRebind(viewModel);
            } else {
                viewModel = createViewModel(t, mLauncher, isLastItem, mButtonColor);
                mBinding.setVariable(BR.viewModel, viewModel);
            }
            mBinding.executePendingBindings();
        }
    }

    protected abstract VIEWMODEL getBindingViewModel(BINDING binding);

    protected void onRebind(VIEWMODEL viewModel) {}

    protected abstract VIEWMODEL createViewModel(T item, ActivityLauncher launcher, boolean isLastItem, int color);
}
