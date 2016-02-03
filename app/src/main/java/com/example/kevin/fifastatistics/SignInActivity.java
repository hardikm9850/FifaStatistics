package com.example.kevin.fifastatistics;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.kevin.fifastatistics.overview.MainActivity;
import com.example.kevin.fifastatistics.restclient.RestClient;
import com.example.kevin.fifastatistics.user.User;
import com.example.kevin.fifastatistics.utils.PreferenceHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.IOException;

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private PreferenceHandler handler;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = PreferenceHandler.getInstance(getApplicationContext());
        checkSignedIn();
        setContentView(R.layout.activity_login);

        mStatusTextView = (TextView) findViewById(R.id.status);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        showProgressDialog();
        opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
            @Override
            public void onResult(GoogleSignInResult googleSignInResult) {
                hideProgressDialog();
                handleSignInResult(googleSignInResult);
            }
        });

    }

    private void checkSignedIn()
    {
        if (handler.isSignedIn())
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            System.out.println(result.getStatus().toString());
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess())
        {
            GoogleSignInAccount acct = result.getSignInAccount();
            new GetOrCreateUser(this).execute(
                    acct.getDisplayName(), acct.getId(),
                    acct.getEmail(), acct.getPhotoUrl().toString());
        }
        else
        {
            // TODO
            updateUI(false);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
        handler.setSignedIn(false);
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetOrCreateUser extends AsyncTask<String, String, Void>
    {
        private Context context;
        private RestClient client = RestClient.getInstance();
        boolean failedGet = false;
        boolean failedCreate = false;

        public GetOrCreateUser(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(final String... args) {

            String name = args[0];
            String googleId = args[1];
            String email = args[2];
            String imageUrl = args[3];
            JsonNode user;
            try
            {
                publishProgress("Attempting to retrieve user data...");
                user = client.getUserWithGoogleId(googleId);
            }
            catch (IOException e)
            {
                failedGet = true;
                return null;
            }

            if (user == null)
            {
                JsonNode json;
                try
                {
                    publishProgress("Creating new user...");
                    json = client.createUser(name, googleId, email, imageUrl);
                    Log.d("CREATEUSER", "response: " + json);
                }
                catch (IOException e)
                {
                    failedCreate = true;
                    return null;
                }

                try
                {
                    handler.setCurrentUser(name, googleId, email, imageUrl);
                    handler.storeUser(new User(name, googleId, email, imageUrl, json.get("id").asText()));
                    handler.setSignedIn(true);
                }
                catch (NullPointerException e)
                {
                    failedCreate = true;
                    return null;
                }
            }
            else
            {
                handler.setCurrentUser(name, googleId, email, imageUrl);
                handler.storeUser(user);
                handler.setSignedIn(true);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... args)
        {
            mProgressDialog.setMessage(args[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (failedGet)
            {
                mProgressDialog.setMessage("Failed to retrieve user data.");
                mProgressDialog.dismiss();
                // TODO add little popup this saying this failed
            }
            else if (failedCreate)
            {
                mProgressDialog.setMessage("Failed to create user.");
                mProgressDialog.dismiss();
                // TODO add little popup this saying this failed
            }
            else
            {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}