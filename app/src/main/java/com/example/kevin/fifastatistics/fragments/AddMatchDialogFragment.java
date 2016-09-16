package com.example.kevin.fifastatistics.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.ResourceUtils;
import com.example.kevin.fifastatistics.utils.ToastUtils;
import com.example.kevin.fifastatistics.views.AddMatchListLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Dialog for adding matches. Should be shown as a fullscreen dialog.
 * <p>
 * See https://developer.android.com/guide/topics/ui/dialogs.html#FullscreenDialog for details.
 */
public class AddMatchDialogFragment extends DialogFragment {

    private User mUser;
    private Friend mOpponent;
    private AddMatchDialogSaveListener mListener;
    private ImageLoader mImageLoader;
    private Toolbar mToolbar;
    private Activity mActivity;
    private AddMatchListLayout mAddMatchList;
    private int mOldStatusBarColor;

    public static AddMatchDialogFragment newInstance(User user, Friend opponent,
                                                     AddMatchDialogSaveListener listener,
                                                     Activity activity) {
        AddMatchDialogFragment fragment = new AddMatchDialogFragment();
        fragment.mUser = user;
        fragment.mOpponent = opponent;
        fragment.mListener = listener;
        fragment.mImageLoader = ImageLoader.getInstance();
        fragment.mActivity = activity;

        return fragment;
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
        mToolbar.setNavigationOnClickListener(v -> dismiss());

        initializeDoneMenuItem();
        initializeCameraMenuItem();
    }

    private void initializeDoneMenuItem() {
        ImageButton b = (ImageButton) mToolbar.findViewById(R.id.done_button);
        b.setOnClickListener(v -> {
            try {
                User.StatsPair stats = mAddMatchList.getValues();
            } catch (NumberFormatException nfe) {
                ToastUtils.showShortToast(mActivity, "All values must be integers.");
                return;
            }
            // TODO match
            mListener.onSave();
        });
    }

    private void initializeCameraMenuItem() {
        ImageButton b = (ImageButton) mToolbar.findViewById(R.id.camera_button);
        //TODO
    }

    private void setStatusBarColor() {
        mOldStatusBarColor = mActivity.getWindow().getStatusBarColor();
        mActivity.getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccentDark));
    }

    private void initializeLeftUserImage(View view) {
        ImageView leftImage = (ImageView) view.findViewById(R.id.left_image);
        mImageLoader.displayImage(mUser.getImageUrl(), leftImage);
    }

    private void initializeRightUserImage(View view) {
        ImageView rightImage = (ImageView) view.findViewById(R.id.right_image);
        mImageLoader.displayImage(mOpponent.getImageUrl(), rightImage);
    }

    private void initializeAddMatchList(View view) {
        mAddMatchList = (AddMatchListLayout) view.findViewById(R.id.add_match_list_layout);
    }

    private void initializeSwitchSidesButton(View view) {
        ImageButton b = (ImageButton) view.findViewById(R.id.switch_sides_button);
        b.setOnClickListener(v -> {
            // TODO
        });
    }

    public interface AddMatchDialogSaveListener {
        void onSave(); // onSave(Match match);
    }
}
