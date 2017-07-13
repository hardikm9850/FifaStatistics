package com.example.kevin.fifastatistics.viewmodels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.Bindable;
import android.view.View;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.FragmentMatchUpdateBinding;
import com.example.kevin.fifastatistics.interfaces.Consumer;
import com.example.kevin.fifastatistics.listeners.SimpleObserver;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdateResponse;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.models.databasemodels.user.records.Streak;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.network.MatchApi;
import com.example.kevin.fifastatistics.network.MatchUpdateApi;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.utils.ToastUtils;

import retrofit2.Response;
import rx.Observable;
import rx.Subscription;

import static com.example.kevin.fifastatistics.activities.MatchUpdateActivity.MatchEditType;

public class MatchUpdateFragmentViewModel extends FooterButtonsViewModel {

    private static final String APPROVE;
    private static final String REJECT;
    private static final String REQUEST_CHANGE;

    static {
        Resources resources = FifaApplication.getContext().getResources();
        APPROVE = resources.getString(R.string.approve);
        REJECT = resources.getString(R.string.reject);
        REQUEST_CHANGE = resources.getString(R.string.create_request);
    }

    private Context mContext;
    private MatchUpdate mUpdate;
    private Match mMatch;
    private MatchUpdateInteraction mInteraction;
    private UpdateStatsCardViewModel mUpdateStatsCardViewModel;
    private User mUser;
    private String mUpdateId;
    private final MatchEditType mType;
    private boolean mIsFooterVisible = false;

    public MatchUpdateFragmentViewModel(Match match, MatchUpdate update, User user, Context context,
                                        MatchUpdateInteraction interaction, FragmentMatchUpdateBinding binding,
                                        MatchEditType type, String updateId) {
        mUpdate = update;
        mMatch = match;
        mContext = context;
        mType = type;
        mUser = user;
        mInteraction = interaction;
        mUpdateId = updateId;
        mUpdateStatsCardViewModel = new UpdateStatsCardViewModel(mMatch, mUpdate, user, binding.cardUpdateStatsLayout, mType);
    }

    public void load() {
        if (isCreatingNewUpdate()) {
            hideProgressBar();
            mInteraction.onUpdateLoaded(mMatch, mUpdate);
        } else if (mUpdate == null && mMatch != null) {
            load(FifaApi.getUpdateApi().getUpdate(mMatch.getUpdateId()), this::setMatchUpdate);
        } else if (mMatch == null && mUpdate != null) {
            load(FifaApi.getMatchApi().getMatch(mUpdate.getMatchId()), this::setMatch);
        } else {
            loadMatchAndUpdate();
        }
    }

    private boolean isCreatingNewUpdate() {
        return mType == MatchEditType.CREATE;
    }

    private <T> void load(Observable<T> observable, final Consumer<T> consumer) {
        showProgressBar();
        Subscription s = observable.compose(ObservableUtils.applySchedulers()).subscribe(
                new SimpleObserver<T>() {
                    @Override
                    public void onError(Throwable e) {
                        handleLoadFailure(e);
                    }

                    @Override
                    public void onNext(T t) {
                        hideProgressBar();
                        consumer.accept(t);
                        mUpdateStatsCardViewModel.init(mMatch, mUpdate);
                        if (mInteraction != null) {
                            mInteraction.onUpdateLoaded(mMatch, mUpdate);
                        }
                    }
                }
        );
        addSubscription(s);
    }

    private void handleLoadFailure(Throwable t) {
        hideProgressBar();
        showRetryButton();
        if (mInteraction != null) {
            mInteraction.onUpdateLoadFailed(t);
        }
    }

    private void loadMatchAndUpdate() {
        showProgressBar();
        MatchApi matchApi = FifaApi.getMatchApi();
        Subscription s = FifaApi.getUpdateApi().getUpdate(mUpdateId)
                .compose(ObservableUtils.applySchedulers())
                .doOnError(this::handleLoadFailure)
                .map(update -> mUpdate = update)
                .flatMap(update ->
                        matchApi.getMatch(update.getMatchId())
                                .compose(ObservableUtils.applySchedulers()))
                .subscribe(new SimpleObserver<Match>() {
                    @Override
                    public void onError(Throwable e) {
                        handleLoadFailure(e);
                    }

                    @Override
                    public void onNext(Match match) {
                        hideProgressBar();
                        mMatch = match;
                        mUpdateStatsCardViewModel.init(mMatch, mUpdate);
                        mInteraction.onUpdateLoaded(mMatch, mUpdate);
                    }
                });
        addSubscription(s);
    }

    @Override
    public void destroy() {
        super.destroy();
        mInteraction = null;
        mContext = null;
    }

    @Override
    public String getRightButtonText() {
        return mType == MatchEditType.REVIEW ? APPROVE : REQUEST_CHANGE;
    }

    @Override
    public void onRightButtonClick(View button) {
        MatchUpdate update = mUpdateStatsCardViewModel.build();
        if (mType == MatchEditType.REVIEW) {
            approveUpdate(update.getId());
        } else {
            if (!update.hasUpdates()) {
                ToastUtils.showShortToast(mContext, R.string.error_empty_update);
            } else if (mUpdateStatsCardViewModel.validate()) {
                createUpdate();
            }
        }
    }

    private void approveUpdate(String updateId) {
        Subscription s = FifaApi.getUpdateApi().acceptUpdate(updateId, new MatchUpdateResponse())
                .compose(ObservableUtils.applySchedulers())
                .subscribe(new SimpleObserver<Response<Void>>() {
                    @Override
                    public void onNext(Response<Void> v) {
                        mInteraction.onUpdateAccepted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mInteraction.onUpdateAcceptFailed(e);
                    }
                });
        addSubscription(s);
    }

    private void createUpdate() {
        ProgressDialog d = ProgressDialog.show(mContext, "loading", "ok", true);
        Subscription s = FifaApi.getUpdateApi().createUpdate(mUpdateStatsCardViewModel.build())
                .compose(ObservableUtils.applySchedulers())
                .subscribe(new SimpleObserver<Response<Void>>() {
                    @Override
                    public void onError(Throwable e) {
                        d.dismiss();
                        if (mInteraction != null) {
                            mInteraction.onUpdateCreateFailed(e);
                        }
                    }

                    @Override
                    public void onNext(Response<Void> response) {
                        d.dismiss();
                        if (mInteraction != null) {
                            mInteraction.onUpdateCreated();
                        }
                    }
                });
        addSubscription(s);
    }

    @Override
    public String getLeftButtonText() {
        return REJECT;
    }

    @Override
    public int getLeftButtonVisibility() {
        return mType == MatchEditType.REVIEW ? View.VISIBLE : View.GONE;
    }

    @Override
    public void onLeftButtonClick(View button) {
        MatchUpdate update = mUpdateStatsCardViewModel.build();
        Subscription s =FifaApi.getUpdateApi().declineUpdate(update.getId(), new MatchUpdateResponse())
                .compose(ObservableUtils.applySchedulers())
                .subscribe(new SimpleObserver<Response<Void>>() {
                    @Override
                    public void onNext(Response<Void> response) {
                        mInteraction.onUpdateDeclined();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mInteraction.onUpdateDeclineFailed(e);
                    }
                });
        addSubscription(s);
    }

    @Bindable
    @Override
    public int getFooterVisibility() {
        return mIsFooterVisible && !shouldFooterStayHidden() ? View.VISIBLE : View.GONE;
    }

    public void setFooterVisibility(boolean isVisible) {
        mIsFooterVisible = isVisible;
        notifyPropertyChanged(BR.footerVisibility);
    }

    private boolean shouldFooterStayHidden() {
        boolean isCreatedByUser = mUpdate != null && mUser.getId().equals(mUpdate.getCreatorId());
        return mType == MatchEditType.REVIEW && isCreatedByUser;
    }

    @Override
    public void onRetryButtonClick() {
        load();
    }

    public void setMatchUpdate(MatchUpdate update) {
        mUpdate = update;
        mUpdateStatsCardViewModel.init(mMatch, mUpdate);
    }

    private void setMatch(Match match) {
        mMatch = match;
        mUpdateStatsCardViewModel.init(mMatch, mUpdate);
    }

    public MatchUpdate getMatchUpdate() {
        return mUpdateStatsCardViewModel.build();
    }

    public UpdateStatsCardViewModel getUpdateStatsCardViewModel() {
        return mUpdateStatsCardViewModel;
    }

    public interface MatchUpdateInteraction {
        void onUpdateLoaded(Match match, MatchUpdate matchUpdate);
        void onUpdateLoadFailed(Throwable e);
        void onUpdateCreated();
        void onUpdateCreateFailed(Throwable e);
        void onUpdateAccepted();
        void onUpdateAcceptFailed(Throwable e);
        void onUpdateDeclined();
        void onUpdateDeclineFailed(Throwable e);
    }
}
