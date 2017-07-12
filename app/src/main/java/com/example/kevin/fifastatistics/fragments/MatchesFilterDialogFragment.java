package com.example.kevin.fifastatistics.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.example.kevin.fifastatistics.interfaces.OnFilterCreatedListener;

public class MatchesFilterDialogFragment extends FifaBaseDialogFragment {

    private OnFilterCreatedListener mOnFilterCreatedListener;

    public static MatchesFilterDialogFragment newInstance() {
        return new MatchesFilterDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        attachOnFilterCreatedListener();
        return super.onCreateDialog(savedInstanceState);
    }

    private void attachOnFilterCreatedListener() {
        Fragment fragment = getTargetFragment();
        if (fragment instanceof OnFilterCreatedListener) {
            mOnFilterCreatedListener = (OnFilterCreatedListener) fragment;
        }
    }
}
