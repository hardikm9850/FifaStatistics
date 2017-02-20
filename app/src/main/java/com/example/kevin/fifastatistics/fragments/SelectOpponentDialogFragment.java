package com.example.kevin.fifastatistics.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.adapters.ImageListAdapter;

import java.util.List;

import lombok.NoArgsConstructor;

/**
 * Dialog for selecting an opponent to play against when playing a new series or match.
 */
@NoArgsConstructor
public class SelectOpponentDialogFragment extends FifaBaseDialogFragment {

    private static final String TAG = "opponents";

    private SelectOpponentListener mListener;
    private List<Friend> mFriends;

    public static SelectOpponentDialogFragment newInstance(User user, SelectOpponentListener listener) {
        SelectOpponentDialogFragment dialog = new SelectOpponentDialogFragment();
        dialog.mFriends = user.getFriends();
        dialog.mListener = listener;
        return dialog;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setAdapter(new ImageListAdapter(getActivity(), mFriends), (dialog, which) ->
            mListener.onOpponentClick(mFriends.get(which))
        );
        builder.setCancelable(true);
        builder.setTitle("Select Opponent");
        return builder.create();
    }

    public void show(FragmentManager manager) {
        show(manager, TAG);
    }

    public interface SelectOpponentListener {
        void onOpponentClick(Friend friend);
    }
}
