package com.example.kevin.fifastatistics.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import me.tatarka.bindingcollectionadapter.ItemView;

public class CreateSeriesMatchListViewModel extends BaseObservable {

    private final ObservableList<CreateSeriesListItemViewModel> mItems;
    private final ItemView mItemView;
    private FifaActivity mActivity;
    private User mUser;
    private Friend mOpponent;

    public CreateSeriesMatchListViewModel(FifaActivity activity, User user, Friend opponent) {
        mItems = new ObservableArrayList<>();
        mItemView = ItemView.of(BR.listItemViewModel, R.layout.item_create_series_match_list);
        mActivity = activity;
        mUser = user;
        mOpponent = opponent;
    }

    public void add(Match match) {
        mItems.add(new CreateSeriesListItemViewModel(mActivity, match, mUser, mOpponent, mItems.size() + 1));
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CreateSeriesListItemViewModel item = (CreateSeriesListItemViewModel) parent.getAdapter().getItem(position);
        item.onItemClicked();
    }

    public ObservableList<CreateSeriesListItemViewModel> getItems() {
        return mItems;
    }

    public ItemView getItemView() {
        return mItemView;
    }
}
