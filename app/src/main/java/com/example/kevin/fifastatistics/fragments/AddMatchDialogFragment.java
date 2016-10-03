package com.example.kevin.fifastatistics.fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.example.kevin.fifastatistics.activities.FifaActivity;
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
import com.example.kevin.fifastatistics.views.AddMatchListLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;

/**
 * Dialog for adding matches. Should be shown as a fullscreen dialog.
 * <p>
 * See https://developer.android.com/guide/topics/ui/dialogs.html#FullscreenDialog for details.
 */
public class AddMatchDialogFragment extends DialogFragment
        implements CameraFragment.ImageCaptureListener, FifaActivity.OnBackPressedHandler {

    private ImageLoader mImageLoader;
    private Toolbar mToolbar;
    private AppCompatActivity mActivity;

    private User mUser;
    private Friend mOpponent;
    private Fragment mCameraFragment;
    private AddMatchDialogSaveListener mListener;
    private AddMatchListLayout mAddMatchList;
    private ImageView mLeftImage;
    private ImageView mRightImage;

    private int mOldStatusBarColor;
    private boolean mDidSwapSides;

    public static AddMatchDialogFragment newInstance(User user, Friend opponent,
                                                     AddMatchDialogSaveListener listener,
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
    public void onDestroyView() {
        resetStatusBarColor();
        System.gc();
        super.onDestroyView();
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
        mToolbar.setNavigationOnClickListener(v -> showConfirmationDialog());

        initializeDoneMenuItem();
        initializeCameraMenuItem();
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
            Penalties penalties = mAddMatchList.getPenalties();
            boolean userDidWin = userDidWin(stats, penalties);
            Match match = Match.builder()
                    .stats(stats)
                    .penalties(penalties)
                    .winner(userDidWin ? Friend.fromUser(mUser) : mOpponent)
                    .loser(userDidWin ? mOpponent : Friend.fromUser(mUser))
                    .build();

            if (MatchUtils.validateMatch(match)) {
                mListener.onSaveMatch(match);
            } else {
                ToastUtils.showShortToast(mActivity, "Match is not valid.");
            }
        });
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

    private void initializeCameraMenuItem() {
        ImageButton b = (ImageButton) mToolbar.findViewById(R.id.camera_button);
        b.setOnClickListener(view -> {
            System.gc(); // want to clear memory before heavy bitmap operations
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
        ProgressDialog dialog = new ProgressDialog(mActivity);
        dialog.setTitle("Processing Image");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Log.d("STARTING", "PROCESSING");
        preprocessor.processBitmap(bitmap)
                .map(b -> {
                    OcrManager manager = OcrManager.getInstance(b);
                    try {
                        return manager.retrieveFacts();
                    } catch (IOException e) {
                        return null;
                    }
                })
                .compose(ObservableUtils.applySchedulers())
                .subscribe(facts -> {
                    if (facts != null) {
                        mAddMatchList.setValues(facts);
                    } else {
                        ToastUtils.showShortToast(mActivity, "Failed to parse image.");
                    }
                    dialog.cancel();
                    System.gc(); // cleanup bitmap memory
        });
        Log.d("FINISHED", "PROCESSING");
    }

    @Override
    public boolean handleBackPress() {
        showConfirmationDialog();
        return true;
    }

    private void closeCameraFragment() {
        if (mCameraFragment != null) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove(mCameraFragment);
            trans.commit();
            manager.popBackStack();
        }
    }

    public interface AddMatchDialogSaveListener {
        void onSaveMatch(Match match);
    }
}
