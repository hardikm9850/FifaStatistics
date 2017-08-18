package com.example.kevin.fifastatistics.viewmodels.card;

import android.databinding.Bindable;

import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;
import com.example.kevin.fifastatistics.viewmodels.item.RecordsItemViewModel;

public class RecordsCardViewModel extends FifaBaseViewModel {

    private RecordsItemViewModel mMatchesViewModel;
    private RecordsItemViewModel mSeriesViewModel;

    public RecordsCardViewModel(User user) {
        mMatchesViewModel = new RecordsItemViewModel(user.getMatchRecords());
        mSeriesViewModel = new RecordsItemViewModel(user.getSeriesRecords());
    }

    @Bindable
    public RecordsItemViewModel getMatches() {
        return mMatchesViewModel;
    }

    @Bindable
    public RecordsItemViewModel getSeries() {
        return mSeriesViewModel;
    }

    public void setUser(User user) {
        if (user != null) {
            mMatchesViewModel.setRecords(user.getMatchRecords());
            mSeriesViewModel.setRecords(user.getSeriesRecords());
            notifyChange();
        }
    }
}
