package com.example.kevin.fifastatistics.viewmodels;

import android.widget.EditText;
import android.widget.TextView;

import com.example.kevin.fifastatistics.interfaces.Consumer;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

public class UpdateStatsCardViewModel extends FifaBaseViewModel {

    private Match mMatch;
    private MatchUpdate mUpdate;
    private MatchUpdate.Builder mUpdateBuilder;
    private User mUser;

    public UpdateStatsCardViewModel(Match match, MatchUpdate update, User user) {
        mMatch = match;
        mUpdate = update;
        mUpdateBuilder = new MatchUpdate.Builder(update);
        mUser = user;
    }

    public void onGoalsForUpdate(EditText old, EditText update) {
        onUpdate(old, update, i -> mUpdateBuilder.statsFor().goals(i));
    }

    public void onGoalsAgainstUpdate(EditText old, EditText update) {
        onUpdate(old, update, i -> mUpdateBuilder.statsAgainst().goals(i));
    }

    private void onUpdate(EditText old, EditText update, Consumer<Integer> consumer) {
        Integer oldVal = Integer.valueOf(old.getText().toString());
        Integer newVal = Integer.valueOf(update.getText().toString());
        if (oldVal.equals(newVal)) {
            update.setText("");
            old.setAlpha(1f);
        } else if (newVal == null) {
            consumer.accept(MatchUpdate.Builder.REMOVE_VAL);
        } else {
            consumer.accept(newVal);
            old.setAlpha(0.5f);
        }
    }

    public String getLeftHeader() {
        return mMatch.didWin(mUser) ? "You" : mMatch.getWinnerFirstName();
    }

    public String getRightHeader() {
        return !mMatch.didWin(mUser) ? "You" : mMatch.getLoserFirstName();
    }

    public MatchUpdate build() {
        return mUpdateBuilder
                .creatorId(mUser.getId())
                .matchId(mMatch.getId())
                .build();
    }
}
