package com.example.kevin.fifastatistics.fragments.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.views.adapters.ImageListAdapter;

import java.util.List;

import lombok.NoArgsConstructor;

/**
 * Dialog for selecting an opponent to play against when playing a new series or match.
 */
@NoArgsConstructor
public class SelectOpponentDialog extends DialogFragment {

    private static final String TAG = "opponents";

    private SelectOpponentListener mListener;
    private List<Friend> mFriends;

    public static SelectOpponentDialog newInstance(User user, SelectOpponentListener listener) {
        SelectOpponentDialog dialog = new SelectOpponentDialog();
        dialog.mFriends = user.getFriends();
        dialog.mListener = listener;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setAdapter(new ImageListAdapter(getActivity(), mFriends), (dialog, which) -> {
            mListener.onOpponentClick(mFriends.get(which));
        });
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
