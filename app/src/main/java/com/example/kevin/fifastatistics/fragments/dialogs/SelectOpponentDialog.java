package com.example.kevin.fifastatistics.fragments.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.views.adapters.ImageListAdapter;

import lombok.NoArgsConstructor;

/**
 * Dialog for selecting an opponent to play against when playing a new series or match.
 */
@NoArgsConstructor
public class SelectOpponentDialog extends DialogFragment {

    private User mUser;

    public static SelectOpponentDialog newInstance(User user) {
        SelectOpponentDialog dialog = new SelectOpponentDialog();
        dialog.mUser = user;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setAdapter(new ImageListAdapter(getActivity(), mUser.getFriends()), (d, w) -> {});
        builder.setCancelable(true);
        builder.setTitle("Select Opponent");
        return builder.create();
    }
}
