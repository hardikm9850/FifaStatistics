package com.example.kevin.fifastatistics.managers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.kevin.fifastatistics.models.databasemodels.match.FifaEvent;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchScoreSummary;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;

import lombok.AllArgsConstructor;
import lombok.Builder;

public class EventPresenter<T extends FifaEvent> {

    private final Player mCurrentUser;
    private T mEvent;
    private EventItem mTopItem;
    private EventItem mBottomItem;
    private MatchScoreSummary mBoxScore;
    private boolean didTopWin;
    private boolean didWin;
    private boolean didParticipate;

    public EventPresenter(@NonNull Player currentUser) {
        mCurrentUser = currentUser;
    }

    public EventPresenter(@Nullable T event, @NonNull Player currentUser) {
        mEvent = event;
        mCurrentUser = currentUser;
        init(event);
    }

    public void init(T event) {
        mEvent = event;
        if (event != null) {
            didWin = event.getWinnerId().equals(mCurrentUser.getId());
            if (didWin || event.getLoserId().equals(mCurrentUser.getId())) {
                didParticipate = true;
                initEventUserPlayedIn();
            } else {
                initEventNotPlayedIn();
            }
            initBoxscores();
        } else {
            initNullEvent();
        }
    }

    private void initEventUserPlayedIn() {
        EventItem.EventItemBuilder topBuilder = EventItem.builder();
        EventItem.EventItemBuilder bottomBuilder = EventItem.builder();
        topBuilder.name(mCurrentUser.getName());
        if (didWin) {
            initEventNotPlayedIn();
        } else {
            didTopWin = false;
            mTopItem = topBuilder
                    .imageUrl(mEvent.getTeamLoserImageUrl())
                    .score(mEvent.getScoreLoser())
                    .id(mEvent.getLoserId())
                    .build();
            mBottomItem = bottomBuilder
                    .name(mEvent.getWinnerName())
                    .imageUrl(mEvent.getTeamWinnerImageUrl())
                    .score(mEvent.getScoreWinner())
                    .id(mEvent.getWinnerId())
                    .build();
        }
    }

    private void initEventNotPlayedIn() {
        didTopWin = true;
        mTopItem = EventItem.builder()
                .name(mEvent.getWinnerName())
                .imageUrl(mEvent.getTeamWinnerImageUrl())
                .score(mEvent.getScoreWinner())
                .id(mEvent.getWinnerId())
                .build();
        mBottomItem = EventItem.builder()
                .name(mEvent.getLoserName())
                .imageUrl(mEvent.getTeamLoserImageUrl())
                .score(mEvent.getScoreLoser())
                .id(mEvent.getLoserId())
                .build();
    }

    private void initBoxscores() {
        if (mEvent instanceof Match) {
            MatchScoreSummary summary = ((Match) mEvent).getSummary();
            if (summary != null) {
                if (didParticipate && !didWin) {
                    mBoxScore = MatchScoreSummary.inverseOf(summary);
                } else {
                    mBoxScore = summary;
                }
            } else {
                mBoxScore = MatchScoreSummary.zeroed();
            }
        }
    }

    private void initNullEvent() {
        mTopItem = new EventItem(null, null, null, 0);
        mBottomItem = new EventItem(null, null, null, 0);
        mBoxScore = MatchScoreSummary.zeroed();
    }

    public boolean didTopWin() {
        return didTopWin;
    }

    public boolean didUserWin() {
        return didWin;
    }

    public boolean didUserParticipate() {
        return didParticipate;
    }

    public String getTopName() {
        return mTopItem.name;
    }

    public String getBottomName() {
        return mBottomItem.name;
    }

    public String getTopUserId() {
        return mTopItem.id;
    }

    public String getBottomUserId() {
        return mBottomItem.id;
    }

    public String getTopScore() {
        return mEvent != null ? String.valueOf(mTopItem.score) : null;
    }

    public String getBottomScore() {
        return mEvent != null ? String.valueOf(mBottomItem.score) : null;
    }

    public String getTopTeamImageUrl() {
        return mTopItem.imageUrl;
    }

    public String getBottomTeamImageUrl() {
        return mBottomItem.imageUrl;
    }

    @NonNull
    public MatchScoreSummary getBoxScore() {
        return mBoxScore;
    }

    @AllArgsConstructor
    @Builder
    private static final class EventItem {
        final String name;
        final String id;
        final String imageUrl;
        final int score;
    }
}
