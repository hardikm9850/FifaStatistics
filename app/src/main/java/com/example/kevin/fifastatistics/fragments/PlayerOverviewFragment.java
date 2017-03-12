package com.example.kevin.fifastatistics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.managers.RetrofitErrorManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.views.UserOverviewLayout;

import rx.Subscription;

public class PlayerOverviewFragment extends FifaProgressFragment implements OnBackPressedHandler {

    private static final String ARG_USER_ID = "id";

    private String mUserId;
    private View mContentView;
    private OnPlayerFragmentInteractionListener mListener;

    public static PlayerOverviewFragment newInstance(String userId) {
        PlayerOverviewFragment fragment = new PlayerOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserId = getArguments().getString(ARG_USER_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_player_overview, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(mContentView);
        setContentShown(false);
        UserOverviewLayout overview = (UserOverviewLayout) mContentView.findViewById(R.id.useroverviewdata);
        Subscription userSub = RetrievalManager.getUser(mUserId)
                .onErrorReturn(t -> {
                    Log.e("ERROR", "getUserError: " + t.getMessage());
                    RetrofitErrorManager.showToastForError(t, getActivity());
                    return null;
                })
                .subscribe(user -> {
                    if (user == null) {
                        getActivity().finish();
                    } else {
                        overview.setUsername(user.getName());
                        overview.setUser(user);
                        setContentShown(true);
                    }
                });
        addSubscription(userSub);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPlayerFragmentInteractionListener) {
            mListener = (OnPlayerFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean handleBackPress() {
        return false;
    }

    public interface OnPlayerFragmentInteractionListener {
        void onPlayerFragmentInteraction(User user);
    }
}
