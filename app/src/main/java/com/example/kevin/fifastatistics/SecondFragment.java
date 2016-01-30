package com.example.kevin.fifastatistics;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kevin.fifastatistics.RestClient.RestJsonInterpreter;
import com.example.kevin.fifastatistics.User.User;

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
        new GetUsers().execute();
        return view;
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetUsers extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(view.getContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

//            friendsList = reader.getUserList();

            if (friendsList == null) {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Handle null friends list
                // ...
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
//            Button button = (Button) view.findViewById(R.id.textView);
//            button.setText(friendsList.get(0).getName());
        }

    }
}
