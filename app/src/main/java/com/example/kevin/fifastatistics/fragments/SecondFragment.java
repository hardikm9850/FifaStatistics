package com.example.kevin.fifastatistics.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    private View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_second, container, false);

//        SharedPreferencesManager handler = SharedPreferencesManager.getBuilder(getContext());
//        User user = handler.getUser();
//        user.deleteIncomingRequests();
//        handler.storeUserSync(user);
//

        return view;
    }
}
