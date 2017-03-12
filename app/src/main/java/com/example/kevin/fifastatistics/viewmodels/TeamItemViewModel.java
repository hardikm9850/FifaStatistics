package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.Bindable;
import android.view.View;

import com.example.kevin.fifastatistics.models.databasemodels.league.Team;

public class TeamItemViewModel extends FifaBaseViewModel {

    private Team mTeam;
    private OnTeamClickListener mListener;

    public TeamItemViewModel(Team team, OnTeamClickListener listener) {
        mTeam = team;
        mListener = listener;
    }

    public void setTeam(Team team) {
        mTeam = team;
        notifyChange();
    }

    @Bindable
    public String getName() {
        return mTeam != null ? mTeam.getShortName() : null;
    }

    @Bindable
    public String getImageUrl() {
        return mTeam != null ? mTeam.getCrestUrl() : null;
    }

    @Bindable
    public String getImageDescription() {
        return mTeam != null ? mTeam.getName() : null;
    }

    public void onClick() {
        if (mListener != null) {
            mListener.onTeamClick(mTeam);
        }
    }

    public interface OnTeamClickListener {
        void onTeamClick(Team team);
    }
}
