package com.example.kevin.fifastatistics;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.restclient.RestJsonInterpreter;
import com.example.kevin.fifastatistics.user.User;
import com.example.kevin.fifastatistics.utils.PreferenceHandler;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    private ProgressDialog pDialog;
    private RestJsonInterpreter reader = RestJsonInterpreter.getInstance();
    private ArrayList<User> friendsList = new ArrayList<>();
    private View view = null;

    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_second, container, false);

        PreferenceHandler handler = PreferenceHandler.getInstance(getContext());
        User user = handler.getUser();
        user.deleteIncomingRequests();
        handler.storeUser(user);

        return view;
    }
}
