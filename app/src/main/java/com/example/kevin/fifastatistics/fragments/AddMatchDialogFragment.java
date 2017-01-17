package com.example.kevin.fifastatistics.fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.interfaces.OnMatchCreatedListener;
import com.example.kevin.fifastatistics.managers.MatchFactsPreprocessor;
import com.example.kevin.fifastatistics.managers.OcrManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Penalties;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.MatchUtils;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.utils.ResourceUtils;
import com.example.kevin.fifastatistics.utils.ToastUtils;
import com.example.kevin.fifastatistics.utils.UiUtils;
import com.example.kevin.fifastatistics.views.AddMatchListLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;

import rx.Subscription;

/**
 * Dialog for adding matches. Should be shown as a fullscreen dialog.
 * <p>
 * See https://developer.android.com/guide/topics/ui/dialogs.html#FullscreenDialog for details.
 */
public class AddMatchDialogFragment extends FifaBaseDialogFragment
        implements CameraFragment.ImageCaptureListener, OnBackPressedHandler {

    private ImageLoader mImageLoader;
    private Toolbar mToolbar;
    private AppCompatActivity mActivity;

    private Match mMatch;
    private User mUser;
    private Friend mOpponent;
    private Fragment mCameraFragment;
    private OnMatchCreatedListener mListener;
    private AddMatchListLayout mAddMatchList;
    private ImageView mLeftImage;
    private ImageView mRightImage;

    private int mOldStatusBarColor;
    private boolean mDidSwapSides;

    public static AddMatchDialogFragment newInstance(User user, Friend opponent,
                                                     OnMatchCreatedListener listener,
                                                     AppCompatActivity activity) {
        AddMatchDialogFragment fragment = new AddMatchDialogFragment();
        fragment.mUser = user;
        fragment.mOpponent = opponent;
        fragment.mListener = listener;
        fragment.mImageLoader = ImageLoader.getInstance();
        fragment.mActivity = activity;

        return fragment;
    }

    private void showConfirmationDialog() {
        AlertDialog dialog = new AlertDialog.Builder(mActivity).create();
        dialog.setMessage("Are you sure you want to discard this match?");
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "KEEP EDITING", (d, w) -> dialog.dismiss());
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DISCARD", (d, w) -> dismiss());
        dialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_match, container, false);
        initializeToolbar(view);
        initializeLeftUserImage(view);
        initializeRightUserImage(view);
        initializeAddMatchList(view);
        initializeSwitchSidesButton(view);
        setStatusBarColor();

        view = maybeAddPaddingToTop(view);
        return view;
    }

    @Override
    public void onPause() {
        UiUtils.hideKeyboard(mActivity);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        UiUtils.removeFragmentFromBackstack(mActivity, this);
        resetStatusBarColor();
        System.gc();
        super.onDestroyView();
    }

    public void setMatch(Match match) {
        mMatch = match;
    }

    private void resetStatusBarColor() {
        mActivity.getWindow().setStatusBarColor(mOldStatusBarColor);
    }

    private View maybeAddPaddingToTop(View view) {
        boolean isDrawerLayout = mActivity.getWindow().getDecorView().getRootView().findViewById(R.id.drawer_layout) != null;
        if (isDrawerLayout) {
            float requiredTopPadding = ResourceUtils.getDimensionFromResourceId(R.dimen.status_bar_height);
            view.setPadding(0, Math.round(requiredTopPadding), 0 ,0);
        }
        return view;
    }

    private void initializeToolbar(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.dialog_toolbar);
        mToolbar.setTitle("Add Match");
        mToolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        mToolbar.setNavigationOnClickListener(v -> maybeDismiss());

        initializeDoneMenuItem();
        initializeCameraMenuItem();
        _debug_autoPopulateForm();
    }

    private void _debug_autoPopulateForm() {
        ImageButton b = (ImageButton) mToolbar.findViewById(R.id.button_autofill);
        b.setOnClickListener(v -> mAddMatchList.setValues(mUser.getAverageStats()));
    }

    private void maybeDismiss() {
        if (mAddMatchList.isEdited()) {
            showConfirmationDialog();
        } else {
            dismiss();
        }
    }

    private void initializeDoneMenuItem() {
        ImageButton b = (ImageButton) mToolbar.findViewById(R.id.done_button);
        b.setOnClickListener(v -> {
            User.StatsPair stats;
            try {
                stats = mAddMatchList.getValues();
            } catch (NumberFormatException nfe) {
                ToastUtils.showShortToast(mActivity, "All values must be integers.");
                return;
            }

            Match match = buildMatch(stats);
            attemptMatchSave(match);
        });
    }

    private Match buildMatch(User.StatsPair stats) {
        Penalties penalties = mAddMatchList.getPenalties();
        boolean userDidWin = userDidWin(stats, penalties);
        stats = maybeSwapStats(stats, userDidWin);
        return Match.builder()
                .stats(stats)
                .penalties(penalties)
                .winner(userDidWin ? Friend.fromUser(mUser) : mOpponent)
                .loser(userDidWin ? mOpponent : Friend.fromUser(mUser))
                .build();
    }

    private void attemptMatchSave(Match match) {
        if (MatchUtils.validateMatch(match)) {
            mListener.onMatchCreated(match);
            dismiss();
        } else {
            ToastUtils.showShortToast(mActivity, "Match is not valid.");
        }
    }

    private boolean userDidWin(User.StatsPair stats, Penalties penalties) {
        if (penalties == null) {
            int goalsLeft = stats.getStatsFor().getGoals();
            int goalsRight = stats.getStatsAgainst().getGoals();
            return mDidSwapSides != goalsLeft > goalsRight;
        } else {
            return mDidSwapSides != mAddMatchList.isLeftPenaltiesGreater();
        }
    }

    private User.StatsPair maybeSwapStats(User.StatsPair stats, boolean userDidWin) {
        if ((userDidWin && mDidSwapSides) || (!userDidWin && !mDidSwapSides)) {
            return new User.StatsPair(stats.getStatsAgainst(), stats.getStatsFor());
        } else {
            return stats;
        }
    }

    private void initializeCameraMenuItem() {
        ImageButton b = (ImageButton) mToolbar.findViewById(R.id.camera_button);
        b.setOnClickListener(view -> {
            System.gc(); // want to clear memory before heavy bitmap operations
            UiUtils.hideKeyboard(mActivity);
            FragmentTransaction t = mActivity.getSupportFragmentManager().beginTransaction();
            t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            mCameraFragment = CameraFragment.newInstance(this);
            t.add(android.R.id.content, mCameraFragment).addToBackStack(null).commit();
        });
    }

    private void setStatusBarColor() {
        mOldStatusBarColor = mActivity.getWindow().getStatusBarColor();
        mActivity.getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccentDark));
    }

    private void initializeLeftUserImage(View view) {
        mLeftImage = (ImageView) view.findViewById(R.id.left_image);
        setLeftImage(mUser.getImageUrl());
    }

    private void setLeftImage(String imageUrl) {
        mImageLoader.displayImage(imageUrl, mLeftImage);
    }

    private void initializeRightUserImage(View view) {
        mRightImage = (ImageView) view.findViewById(R.id.right_image);
        setRightImage(mOpponent.getImageUrl());
    }

    private void setRightImage(String imageUrl) {
        mImageLoader.displayImage(imageUrl, mRightImage);
    }

    private void initializeAddMatchList(View view) {
        mAddMatchList = (AddMatchListLayout) view.findViewById(R.id.add_match_list_layout);
        if (mMatch != null) {
            if (!mMatch.didWin(mUser)) {
                mMatch = Match.swapStats(mMatch);
                if (mMatch.getPenalties() != null) {
                    int loserPenalties = mMatch.getPenalties().getLoser();
                    mMatch.getPenalties().setLoser(mMatch.getPenalties().getWinner());
                    mMatch.getPenalties().setWinner(loserPenalties);
                }
            }
            mAddMatchList.setValues(mMatch.getStats());
            mAddMatchList.setPenalties(mMatch.getPenalties());
        }
    }

    private void initializeSwitchSidesButton(View view) {
        ImageButton b = (ImageButton) view.findViewById(R.id.switch_sides_button);
        b.setOnClickListener(v -> {
            setLeftImage(mDidSwapSides ? mUser.getImageUrl() : mOpponent.getImageUrl());
            setRightImage(mDidSwapSides ? mOpponent.getImageUrl() : mUser.getImageUrl());
            mDidSwapSides = !mDidSwapSides;
        });
    }

    @Override
    public void onImageCapture(Bitmap bitmap, MatchFactsPreprocessor preprocessor) {
        closeCameraFragment();
        ProgressDialog dialog = showProcessingDialog();
        Subscription bitmapSub = preprocessor.processBitmap(bitmap)
                .map(this::getMatchFacts)
                .compose(ObservableUtils.applySchedulers())
                .subscribe(facts -> {
                    maybeSetListValues(facts);
                    dialog.cancel();
                    System.gc(); // cleanup bitmap memory
        });
        addSubscription(bitmapSub);
    }

    private ProgressDialog showProcessingDialog() {
        ProgressDialog dialog = new ProgressDialog(mActivity);
        dialog.setTitle("Processing Image");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    private User.StatsPair getMatchFacts(Bitmap b) {
        OcrManager manager = OcrManager.getInstance(b);
        try {
            return manager.retrieveFacts();
        } catch (IOException e) {
            return null;
        }
    }

    private void maybeSetListValues(User.StatsPair facts) {
        if (facts != null) {
            mAddMatchList.setValues(facts);
        } else {
            ToastUtils.showShortToast(mActivity, "Failed to parse image.");
        }
    }

    @Override
    public boolean handleBackPress() {
        if (mAddMatchList.isEdited()) {
            showConfirmationDialog();
            return true;
        } else {
            return false;
        }
    }

    private void closeCameraFragment() {
        if (mCameraFragment != null) {
            UiUtils.removeFragmentFromBackstack(mActivity, mCameraFragment);
        }
    }
}
