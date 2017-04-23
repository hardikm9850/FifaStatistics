package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.Bindable;

import com.example.kevin.fifastatistics.models.databasemodels.user.records.UserRecords;

public class RecordsItemViewModel extends FifaBaseViewModel {

    private UserRecords mRecords;

    public RecordsItemViewModel(UserRecords records) {
        mRecords = records;
    }

    @Bindable
    public String getOverall() {
        return mRecords != null && mRecords.getOverallRecord() != null ? mRecords.getOverallRecord().toString() : null;
    }

    @Bindable
    public String getLastTen() {
        return mRecords != null && mRecords.getLastTenRecord() != null ? mRecords.getLastTenRecord().toString() : null;
    }

    @Bindable
    public String getStreak() {
        return mRecords != null ? mRecords.getStreak() : null;
    }
}