package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.Bindable;

import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import java.util.ArrayList;
import java.util.List;

public class UserOverviewViewModel extends FifaBaseViewModel {

    private User mUser;
    private RecordsCardViewModel mRecords;

    public UserOverviewViewModel(User user) {
        mUser = user;
        mRecords = new RecordsCardViewModel(user);
    }

    public RecordsCardViewModel getRecords() {
        return mRecords;
    }

    @Bindable
    public List<User.StatsPair> getStats() {
        if (mUser != null) {
            List<User.StatsPair> stats = new ArrayList<>();
            stats.add(mUser.getAverageStats());
            stats.add(mUser.getRecordStats());
            return stats;
        } else {
            return null;
        }
    }

    @Bindable
    public String getName() {
        return mUser != null ? mUser.getName() : null;
    }

    @Bindable
    public String getImageUrl() {
        return mUser != null ? mUser.getImageUrl() : null;
    }
}
