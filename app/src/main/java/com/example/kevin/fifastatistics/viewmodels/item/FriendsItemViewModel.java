package com.example.kevin.fifastatistics.viewmodels.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.PlayerActivty;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.utils.ContextUtils;
import com.example.kevin.fifastatistics.utils.IntentFactory;
import com.example.kevin.fifastatistics.utils.TransitionUtils;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;

public class FriendsItemViewModel extends FifaBaseViewModel {

    private Player mFriend;
    private ActivityLauncher mLauncher;

    public FriendsItemViewModel(Player friend, ActivityLauncher launcher) {
        mLauncher = launcher;
        initFriend(friend);
    }

    public void setFriend(Player friend) {
        initFriend(friend);
    }

    private void initFriend(Player friend) {
        mFriend = friend;
        notifyChange();
    }

    @Bindable
    public String getImageUrl() {
        return mFriend != null ? mFriend.getImageUrl() : null;
    }

    @Bindable
    public String getName() {
        return mFriend != null ? mFriend.getName() : null;
    }

    public void onClick(View view) {
        if (mLauncher != null) {
            Context context = view.getContext();
            Intent intent = IntentFactory.createPlayerActivityIntent(ContextUtils.getActivityFromContext(context), mFriend);
            intent.putExtra(PlayerActivty.EXTRA_ENTERED_FROM_SEARCH_BAR, false);
            ActivityOptionsCompat options = TransitionUtils.getSceneTransitionOptions(context, view, R.string.transition_profile_image);
            mLauncher.launchActivity(intent, Activity.RESULT_OK, options.toBundle());
        }
    }
}
