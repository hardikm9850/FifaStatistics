package com.example.kevin.fifastatistics.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.animation.AnimationBindingAdapter;
import com.example.kevin.fifastatistics.databinding.FragmentMatchUpdateBinding;
import com.example.kevin.fifastatistics.event.EventBus;
import com.example.kevin.fifastatistics.event.UpdateRemovedEvent;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.managers.RetrofitErrorManager;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.utils.BuildUtils;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.utils.ToastUtils;
import com.example.kevin.fifastatistics.utils.TransitionUtils;
import com.example.kevin.fifastatistics.viewmodels.MatchUpdateFragmentViewModel;
import com.example.kevin.fifastatistics.views.notifications.FifaNotification;
import com.example.kevin.fifastatistics.views.notifications.FifaNotificationFactory;

import java.util.HashMap;
import java.util.Map;

import static com.example.kevin.fifastatistics.activities.MatchUpdateActivity.MatchEditType;

public class MatchUpdateFragment extends FifaBaseFragment implements OnBackPressedHandler,
        MatchUpdateFragmentViewModel.MatchUpdateInteraction {

    private static final String ARG_TYPE = "isResponse";

    private Match mMatch;
    private MatchUpdate mUpdate;
    private User mUser;
    private String mUpdateId;
    private MatchUpdateFragmentViewModel mViewModel;
    private FragmentMatchUpdateBinding mBinding;
    private MatchEditType mType;

    public static MatchUpdateFragment newInstance(@Nullable MatchUpdate update, User user, @Nullable Match match,
                                                  MatchEditType type, String updateId) {
        Bundle args = new Bundle();
        MatchUpdateFragment fragment = new MatchUpdateFragment();
        args.putSerializable(MATCH_UPDATE, update);
        args.putSerializable(MATCH, match);
        args.putSerializable(USER, user);
        args.putSerializable(ARG_TYPE, type);
        args.putString(UPDATE_ID, updateId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreInstance(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void restoreInstance(Bundle savedInstanceState) {
        Bundle b = savedInstanceState == null ? getArguments() : savedInstanceState;
        mUser = (User) b.getSerializable(USER);
        mMatch = (Match) b.getSerializable(MATCH);
        mUpdate = (MatchUpdate) b.getSerializable(MATCH_UPDATE);
        mType = (MatchEditType) b.getSerializable(ARG_TYPE);
        mUpdateId = b.getString(UPDATE_ID);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(USER, mUser);
        outState.putSerializable(MATCH, mMatch);
        outState.putSerializable(MATCH_UPDATE, mUpdate);
        outState.putSerializable(ARG_TYPE, mType);
        outState.putString(UPDATE_ID, mUpdateId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_match_update, container, false);
        mViewModel = new MatchUpdateFragmentViewModel(mMatch, mUpdate, mUser, getContext(), this, mBinding, mType, mUpdateId);
        TransitionUtils.addTransitionCallbackToBinding(mBinding.cardUpdateStatsLayout);
        mBinding.setViewModel(mViewModel);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mViewModel.load();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mViewModel != null) {
            mViewModel.destroy();
        }
    }

    @Override
    public boolean handleBackPress() {
        if (mType == MatchEditType.UPDATE && mViewModel.getMatchUpdate().hasUpdates()) {
            showConfirmationDialog();
            return true;
        }
        return false;
    }

    private void showConfirmationDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setMessage(getString(R.string.confirm_discard_update));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.keep_editing), (d, w) -> dialog.dismiss());
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.discard), (d, w) -> {
            dialog.dismiss();
            getActivity().finish();
        });
        dialog.show();
    }

    @Override
    public void onUpdateLoaded(Match match, MatchUpdate update) {
        mMatch = match;
        mUpdate = update;
        AnimationBindingAdapter.alphaScale(mBinding.updateLayout, View.VISIBLE);
        mViewModel.setFooterVisibility(true);
        addUpdateToStoredPendingUpdates(update);
    }

    private void addUpdateToStoredPendingUpdates(MatchUpdate update) {
        RetrievalManager.getLocalPendingUpdates().subscribe(updates -> {
            if (update != null && !updates.contains(update)) {
                SharedPreferencesManager.addMatchUpdate(update);
            }
        });
    }

    @Override
    public void onUpdateLoadFailed(Throwable e) {
        RetrofitErrorManager.showToastForError(e, getActivity());
    }

    @Override
    public void onUpdateCreated() {
        ToastUtils.showShortToast(getContext(), R.string.request_sent);
        showNotificationIfDebugging(FifaNotificationFactory.UPDATE_MATCH);
    }

    @Override
    public void onUpdateCreateFailed(Throwable e) {
        RetrofitErrorManager.showToastForError(e, getActivity());
    }

    @Override
    public void onUpdateAccepted(MatchUpdate update) {
        onUpdateRemoved(update, R.string.request_approved);
    }

    private void onUpdateRemoved(MatchUpdate update, @StringRes int message) {
        ToastUtils.showShortToast(getContext(), message);
        EventBus bus = EventBus.getInstance();
        bus.post(new UpdateRemovedEvent(update));
        getActivity().finish();
    }

    @Override
    public void onUpdateAcceptFailed(Throwable e) {
        RetrofitErrorManager.showToastForError(e, getActivity());
    }

    @Override
    public void onUpdateDeclined(MatchUpdate update) {
        onUpdateRemoved(update, R.string.request_declined);
    }

    @Override
    public void onUpdateDeclineFailed(Throwable e) {
        RetrofitErrorManager.showToastForError(e, getActivity());
    }

    private void showNotificationIfDebugging(String tag) {
        if (BuildUtils.isDebug()) {
            FifaApi.getMatchApi().getMatch(mMatch.getId())
                    .compose(ObservableUtils.applySchedulers())
                    .subscribe(match -> {
                        Map<String, String> data = new HashMap<>();
                        data.put("tag", tag);
                        data.put("id", match.getUpdateId());
                        data.put("title", "Kevin wants to update a match");
                        FifaNotification n = FifaNotificationFactory.createNotification(getContext(), data);
                        n.build();
                    });
        } else {
            getActivity().finish();
        }
    }
}
