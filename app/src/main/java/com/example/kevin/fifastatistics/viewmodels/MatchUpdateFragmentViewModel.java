package com.example.kevin.fifastatistics.viewmodels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.FragmentMatchUpdateBinding;
import com.example.kevin.fifastatistics.interfaces.Consumer;
import com.example.kevin.fifastatistics.listeners.SimpleObserver;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.utils.ToastUtils;

import retrofit2.Response;
import rx.Observable;

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
    private final boolean mIsResponse;

    public MatchUpdateFragmentViewModel(Match match, MatchUpdate update, User user, Context context,
                                        MatchUpdateInteraction interaction, FragmentMatchUpdateBinding binding) {
        mUpdate = update;
        mMatch = match;
        mContext = context;
        mIsResponse = !isCreatingNewUpdate();
        mInteraction = interaction;
        mUpdateStatsCardViewModel = new UpdateStatsCardViewModel(mMatch, mUpdate, user, binding.cardUpdateStatsLayout);
    }

    public void load() {
        if (isCreatingNewUpdate()) {
            hideProgressBar();
            mInteraction.onUpdateLoaded();
        } else if (mUpdate == null && mMatch != null) {
            load(FifaApi.getUpdateApi().getUpdate(mMatch.getUpdateId()), this::setMatchUpdate);
        } else if (mMatch == null && mUpdate != null) {
            load(FifaApi.getMatchApi().getMatch(mUpdate.getMatchId()), this::setMatch);
        } else {

        }
    }

    private boolean isCreatingNewUpdate() {
        return mUpdate == null && mMatch != null && mMatch.getUpdateId() == null;
    }

    private <T> void load(Observable<T> observable, final Consumer<T> consumer) {
        showProgressBar();
        observable.subscribe(
                new SimpleObserver<T>() {
                    @Override
                    public void onError(Throwable e) {
                        hideProgressBar();
                        showRetryButton();
                        if (mInteraction != null) {
                            mInteraction.onUpdateLoadFailed(e);
                        }
                    }

                    @Override
                    public void onNext(T t) {
                        hideProgressBar();
                        consumer.accept(t);
                        if (mInteraction != null) {
                            mInteraction.onUpdateLoaded();
                        }
                    }
                }
        );
    }

    @Override
    public void destroy() {
        super.destroy();
        mInteraction = null;
    }

    @Override
    public String getRightButtonText() {
        return mIsResponse ? APPROVE : REQUEST_CHANGE;
    }

    @Override
    public void onRightButtonClick(View button) {
        if (mIsResponse) {
            // approve
        } else {
            MatchUpdate update = mUpdateStatsCardViewModel.build();
            if (!update.hasUpdates()) {
                ToastUtils.showShortToast(mContext, R.string.error_empty_update);
            } else if (mUpdateStatsCardViewModel.validate()) {
                createUpdate();
            }
        }
    }

    private void createUpdate() {
        ProgressDialog d = ProgressDialog.show(mContext, "loading", "ok", true);
        FifaApi.getUpdateApi().createUpdate(mUpdateStatsCardViewModel.build())
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
    }

    @Override
    public String getLeftButtonText() {
        return REJECT;
    }

    @Override
    public int getLeftButtonVisibility() {
        return mIsResponse ? View.VISIBLE : View.GONE;
    }

    @Override
    public void onLeftButtonClick(View button) {
        // reject
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

    public UpdateStatsCardViewModel getUpdateStatsCardViewModel() {
        return mUpdateStatsCardViewModel;
    }

    public interface MatchUpdateInteraction {
        void onUpdateLoaded();
        void onUpdateLoadFailed(Throwable e);
        void onUpdateCreated();
        void onUpdateCreateFailed(Throwable e);
    }
}
