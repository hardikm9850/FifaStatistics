package com.example.kevin.fifastatistics.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.example.kevin.fifastatistics.interfaces.OnMatchUpdatedListener;
import com.example.kevin.fifastatistics.interfaces.OnSeriesScoreUpdateListener;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CreateSeriesScoreViewModel extends BaseObservable implements OnMatchUpdatedListener {

    private Player mUser;
    private Player mOpponent;
    private OnSeriesScoreUpdateListener mListener;
    private int mUserWins;
    private int mOpponentWins;

    public CreateSeriesScoreViewModel(Player user, Player opponent, OnSeriesScoreUpdateListener listener) {
        mUser = user;
        mOpponent = opponent;
        mListener = listener;
    }

    public String getUserImageUrl() {
        return mUser.getImageUrl();
    }

    public String getOpponentImageUrl() {
        return mOpponent.getImageUrl();
    }

    public void incrementUserWins() {
        mListener.onUserScoreUpdate(mUserWins, mUserWins + 1);
        mUserWins++;
    }

    public void decrementUserWins() {
        mListener.onUserScoreUpdate(mUserWins, mUserWins - 1);
        mUserWins--;
    }

    public void incrementOpponentWins() {
        mListener.onOpponentScoreUpdate(mOpponentWins, mOpponentWins + 1);
        mOpponentWins++;
    }

    public void decrementOpponentWins() {
        mListener.onOpponentScoreUpdate(mOpponentWins, mOpponentWins - 1);
        mOpponentWins--;
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        ImageLoader.getInstance().displayImage(imageUrl, view);
    }

    public void setSeriesScoreUpdateListener(OnSeriesScoreUpdateListener listener) {
        mListener = listener;
    }

    @Override
    public void onMatchUpdated(Match oldMatch, Match newMatch) {
        Friend oldWinner = oldMatch.getWinner();
        Friend newWinner = newMatch.getWinner();
        if (!oldWinner.equals(newWinner)) {
            if (mUser.equals(newWinner)) {
                incrementUserWins();
                decrementOpponentWins();
            } else {
                decrementUserWins();
                incrementOpponentWins();
            }
        }
    }
}
