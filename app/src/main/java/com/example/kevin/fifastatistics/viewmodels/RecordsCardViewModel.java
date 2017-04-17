package com.example.kevin.fifastatistics.viewmodels;

import com.example.kevin.fifastatistics.models.databasemodels.user.User;

public class RecordsCardViewModel extends FifaBaseViewModel {

    private RecordsItemViewModel mMatchesViewModel;
    private RecordsItemViewModel mSeriesViewModel;

    public RecordsCardViewModel(User user) {
        mMatchesViewModel = new RecordsItemViewModel(user.getMatchRecords());
        mSeriesViewModel = new RecordsItemViewModel(user.getSeriesRecords());
    }

    public RecordsItemViewModel getMatches() {
        return mMatchesViewModel;
    }

    public RecordsItemViewModel getSeries() {
        return mSeriesViewModel;
    }
}
