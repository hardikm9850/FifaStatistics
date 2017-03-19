package com.example.kevin.fifastatistics.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.example.kevin.fifastatistics.adapters.ImageListAdapter;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;

import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;

/**
 * Dialog for selecting an opponent to play against when playing a new series or match.
 */
@NoArgsConstructor
public class SelectOpponentDialogFragment extends FifaBaseDialogFragment {

    private static final String TAG = "opponents";

    private SelectOpponentListener mListener;
    private ImageListAdapter mAdapter;
    private List<? extends Player> mPlayers = new ArrayList<>();

    public static SelectOpponentDialogFragment newInstance(SelectOpponentListener listener) {
        SelectOpponentDialogFragment dialog = new SelectOpponentDialogFragment();
        dialog.mListener = listener;
        return dialog;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        initAdapter();
        builder.setAdapter(mAdapter, (dialog, which) ->
            mListener.onOpponentClick(mPlayers.get(which))
        );
        builder.setCancelable(true);
        builder.setTitle("Select Opponent");
        return builder.create();
    }

    private void initAdapter() {
        mAdapter = new ImageListAdapter(getActivity(), mPlayers);
        RetrievalManager.getUsersWithoutCurrentUser().subscribe(users -> {
            mPlayers = users;
            mAdapter.setPlayers(users);
        });
    }

    public void show(FragmentManager manager) {
        show(manager, TAG);
    }

    public interface SelectOpponentListener {
        void onOpponentClick(Player player);
    }
}
