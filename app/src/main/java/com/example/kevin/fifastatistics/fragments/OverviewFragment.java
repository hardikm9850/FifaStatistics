package com.example.kevin.fifastatistics.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.views.UserOverview;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment implements FifaFragment{

    private User mUser;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mUser = SharedPreferencesManager.getUser();
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        TextView name = (TextView) view.findViewById(R.id.user_header_name_text);
        name.setText(mUser.getName());

        ImageView profileImage = (ImageView) view.findViewById(R.id.user_header_profile_image);
        ImageLoader.getInstance().displayImage(mUser.getImageUrl(), profileImage);

        UserOverview overview = (UserOverview) view.findViewById(R.id.useroverviewdata);
        overview.setUser(mUser);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.action_search).setVisible(false);
    }

    @Override
    public boolean handleBackPress() {
        return false;
    }
}
