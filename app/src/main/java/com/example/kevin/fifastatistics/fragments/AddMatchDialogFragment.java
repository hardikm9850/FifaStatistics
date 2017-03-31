package com.example.kevin.fifastatistics.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.CameraActivity;
import com.example.kevin.fifastatistics.interfaces.MatchFactsPreprocessor;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.interfaces.OnMatchCreatedListener;
import com.example.kevin.fifastatistics.managers.OcrManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Penalties;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.ByteHolder;
import com.example.kevin.fifastatistics.utils.MatchUtils;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.utils.ResourceUtils;
import com.example.kevin.fifastatistics.utils.ToastUtils;
import com.example.kevin.fifastatistics.utils.UiUtils;
import com.example.kevin.fifastatistics.views.AddMatchListLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * Dialog for adding matches. Should be shown as a fullscreen dialog.
 * <p>
 * See https://developer.android.com/guide/topics/ui/dialogs.html#FullscreenDialog for details.
 */
public class AddMatchDialogFragment extends FifaBaseDialogFragment implements OnBackPressedHandler {

    private static final int ADD_MATCH_REQUEST_CODE = 466;

    private ImageLoader mImageLoader;
    private FragmentActivity mActivity;
    private Match mMatch;
    private User mUser;
    private Player mOpponent;
    private OnMatchCreatedListener mListener;
    private AddMatchListLayout mAddMatchList;
    private ImageView mLeftImage;
    private ImageView mRightImage;
    private boolean mDidSwapSides;

    public static AddMatchDialogFragment newInstance(User user, Player opponent) {
        AddMatchDialogFragment fragment = new AddMatchDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER, user);
        args.putSerializable(OPPONENT, opponent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMatchCreatedListener) {
            mListener = (OnMatchCreatedListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            initFromBundle(savedInstanceState);
        } else {
            initFromBundle(getArguments());
        }
    }

    private void initFromBundle(Bundle bundle) {
        mImageLoader = ImageLoader.getInstance();
        mActivity = getActivity();
        mUser = (User) bundle.getSerializable(USER);
        mOpponent = (Player) bundle.getSerializable(OPPONENT);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(USER, mUser);
        outState.putSerializable(OPPONENT, mOpponent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_match, container, false);
        initializeToolbar(view);
        initializeLeftUserImage(view);
        initializeRightUserImage(view);
        initializeAddMatchList(view);
        initializeSwitchSidesButton(view);
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
        System.gc();
        super.onDestroyView();
    }

    public void setMatch(Match match) {
        mMatch = match;
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
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.dialog_toolbar);
        toolbar.setTitle("Add Match");
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(v -> maybeDismiss());
        toolbar.setOnMenuItemClickListener(this::onMenuItemSelected);
        toolbar.inflateMenu(R.menu.menu_new_match);
    }

    private boolean onMenuItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.camera:
                onCameraItemClick();
                return true;
            case R.id.confirm:
                onDoneItemClick();
                return true;
            case R.id.autofill:
                mAddMatchList.setValues(mUser.getAverageStats());
            default:
                return false;
        }
    }

    private void onCameraItemClick() {
        System.gc(); // want to clear memory before heavy bitmap operations
        UiUtils.hideKeyboard(mActivity);
        Intent intent = new Intent(mActivity, CameraActivity.class);
        startActivityForResult(intent, ADD_MATCH_REQUEST_CODE);
    }

    private void onDoneItemClick() {
        User.StatsPair stats;
        try {
            stats = mAddMatchList.getValues();
        } catch (NumberFormatException nfe) {
            ToastUtils.showShortToast(mActivity, "All values must be integers.");
            return;
        }

        Match match = buildMatch(stats);
        attemptMatchSave(match);
    }

    private void maybeDismiss() {
        if (mAddMatchList.isEdited()) {
            showConfirmationDialog();
        } else {
            dismiss();
        }
    }

    private void showConfirmationDialog() {
        AlertDialog dialog = new AlertDialog.Builder(mActivity).create();
        dialog.setMessage("Are you sure you want to discard this match?");
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "KEEP EDITING", (d, w) -> dialog.dismiss());
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DISCARD", (d, w) -> dismiss());
        dialog.show();
    }


    private Match buildMatch(User.StatsPair stats) {
        Penalties penalties = mAddMatchList.getPenalties();
        boolean userDidWin = userDidWin(stats, penalties);
        stats = maybeSwapStats(stats, userDidWin);
        return Match.builder()
                .stats(stats)
                .penalties(penalties)
                .winner(userDidWin ? Friend.fromPlayer(mUser) : Friend.fromPlayer(mOpponent))
                .loser(userDidWin ? Friend.fromPlayer(mOpponent) : Friend.fromPlayer(mUser))
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
            float goalsLeft = stats.getStatsFor().getGoals();
            float goalsRight = stats.getStatsAgainst().getGoals();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_MATCH_REQUEST_CODE && resultCode == CameraActivity.PICTURE_TAKEN_RESULT_CODE) {
            byte[] picture = ByteHolder.getImage();
            String preprocessor = data.getStringExtra(CameraActivity.EXTRA_PREPROCESSOR);
            onImageCapture(picture, CameraActivity.Preprocessor.valueOf(preprocessor).getPreprocessor());
        }
    }

    private void onImageCapture(final byte[] picture, final MatchFactsPreprocessor preprocessor) {
        ProgressDialog dialog = showProcessingDialog();
        Subscription sub = Observable.<Bitmap>create(s -> {
            Bitmap result = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            s.onNext(result);
        })
                .flatMap(preprocessor::processBitmap)
                .map(this::getMatchFacts)
                .compose(ObservableUtils.applySchedulers())
                .subscribe(getStatsObserver(dialog));
        addSubscription(sub);
    }

    private Observer<User.StatsPair> getStatsObserver(ProgressDialog dialog) {
        return new ObservableUtils.EmptyOnCompleteObserver<User.StatsPair>() {
            @Override
            public void onError(Throwable e) {
                ByteHolder.dispose();
                dialog.cancel();
                onOcrError();
                System.gc();
            }

            @Override
            public void onNext(User.StatsPair statsPair) {
                ByteHolder.dispose();
                maybeSetListValues(statsPair);
                dialog.cancel();
                System.gc(); // cleanup bitmap memory
            }
        };
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
            onOcrError();
        }
    }

    private void onOcrError() {
        ToastUtils.showShortToast(mActivity, "Failed to parse image.");
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
}
