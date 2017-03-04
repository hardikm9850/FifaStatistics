package com.example.kevin.fifastatistics.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.views.UserOverviewLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * The main overview fragment for the current user.
 */
public class UserOverviewFragment extends FifaBaseFragment implements OnBackPressedHandler {

    private User mUser;

    public UserOverviewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharedPreferencesManager.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_overview, container, false);
        initializeHeader(view);
        initializeContent(view);
        return view;
    }

    private void initializeHeader(View view) {
        TextView name = (TextView) view.findViewById(R.id.user_header_name_text);
        name.setText(mUser.getName());

        ImageView profileImage = (ImageView) view.findViewById(R.id.user_header_profile_image);
        ImageLoader.getInstance().displayImage(mUser.getImageUrl(), profileImage);
    }

    private void initializeContent(View view) {
        UserOverviewLayout overview = (UserOverviewLayout) view.findViewById(R.id.useroverviewdata);
        overview.setUser(mUser);
    }

    @Override
    public boolean handleBackPress() {
        return false;
    }
}
