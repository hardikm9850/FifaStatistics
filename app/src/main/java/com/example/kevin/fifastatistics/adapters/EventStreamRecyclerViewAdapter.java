package com.example.kevin.fifastatistics.adapters;

import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ItemDateHeaderBinding;
import com.example.kevin.fifastatistics.models.databasemodels.match.FifaEvent;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.viewmodels.item.EventViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public abstract class EventStreamRecyclerViewAdapter<BINDING extends ViewDataBinding, EVENT extends FifaEvent>
        extends StickyEndlessProgressAdapter<ItemDateHeaderBinding, BINDING, EVENT> {

    private Player mUser;

    EventStreamRecyclerViewAdapter(Player user, @LayoutRes int eventItem) {
        super(R.layout.item_date_header, eventItem);
        mUser = user;
    }

    @Override
    protected BindingViewHolder<BINDING, EVENT> createEventViewHolder(BINDING binding) {
        return new EventViewHolder(binding);
    }

    @Override
    protected BindingViewHolder<ItemDateHeaderBinding, EVENT> createHeaderViewHolder(ItemDateHeaderBinding binding) {
        return new HeaderViewHolder(binding);
    }

    private class EventViewHolder extends BindingViewHolder<BINDING, EVENT> {

        EventViewHolder(BINDING binding) {
            super(binding);
        }

        @Override
        public void bindItem(EVENT event) {
            EventViewModel<EVENT> viewModel = getBindingViewModel(mBinding);
            if (viewModel != null) {
                viewModel.setEvent(event, mUser);
            } else {
                bindNewEventViewModel(mBinding, event, mUser);
            }
        }
    }

    protected abstract EventViewModel<EVENT> getBindingViewModel(BINDING binding);

    protected abstract void bindNewEventViewModel(BINDING binding, EVENT event, Player user);

    private class HeaderViewHolder extends BindingViewHolder<ItemDateHeaderBinding, EVENT> {

        HeaderViewHolder(ItemDateHeaderBinding binding) {
            super(binding);
        }

        @Override
        void bindItem(EVENT item) {
            if (item.getDate() != null) {
                DateFormat format = SimpleDateFormat.getDateInstance();
                mBinding.setDate(format.format(item.getDate()));
            }
        }
    }
}
