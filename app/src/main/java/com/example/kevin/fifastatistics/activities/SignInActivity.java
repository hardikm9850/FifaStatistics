package com.example.kevin.fifastatistics.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.network.UserApi;
import com.example.kevin.fifastatistics.network.gcmnotifications.RegistrationIntentService;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private UserApi api;
    private String mRegistrationToken;
    private boolean doCreateUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkSignedIn();
//        _debugging_setUserToAccount();
        setContentView(R.layout.activity_login);

        maybeRegisterGcm();
        setButtonListeners();
        initializeApiClient();
        initializeFirebaseAuth();
        initializeSignInButton();
    }

    private void checkSignedIn()
    {
        if (SharedPreferencesManager.isSignedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void maybeRegisterGcm() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private void setButtonListeners() {
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);
    }

    private void initializeApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void initializeFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        };
    }

    private void initializeSignInButton() {
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mProgressDialog = null;
    }

    private void _debugging_setUserToAccount() {
        String personal = "5704926828878a8c6266cba2";
        FifaApi.getUserApi().getUser(personal)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(user -> {
                    SharedPreferencesManager.storeUser(user);
                    SharedPreferencesManager.setSignedIn(true);
                    checkSignedIn();
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            System.out.println(result.getStatus().toString());
            handleSignInResult(result);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String photoUrl = acct.getPhotoUrl() == null ? "" : acct.getPhotoUrl().toString();
            getOrCreateUser(
                    acct.getDisplayName(), acct.getId(),
                    acct.getEmail(), photoUrl);
        } else {
            // TODO
            Log.d(TAG, "FAILED TO SIGN IN: " + result.getStatus().getStatusMessage());
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(status -> {/*TODO*/});
        SharedPreferencesManager.setSignedIn(false);
    }

    private void revokeAccess() {
        mAuth.signOut();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(status -> {/*TODO*/});
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Unresolvable error has occurred and Google APIs (including Sign-In) won't be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
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
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void getOrCreateUser(String name, String googleId, String email,
                                 String imageUrl)
    {
        Log.d(TAG, "Beginning process");
        startProcessDialog();

        try {
            getRegistrationToken();
        } catch (IOException e) {
            Log.e(TAG, "Message: " + e.getMessage());
            return;
        }

        api = FifaApi.getUserApi();

        api.getUserWithGoogleId(googleId)
                .onErrorReturn(u -> createNewUser(name, email, googleId, imageUrl))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleGetUserResult);
    }

    private User createNewUser(String name, String email, String googleId, String imageUrl) {
        doCreateUser = true;
        User user = new User(name, email, googleId, imageUrl);
        user.setRegistrationToken(mRegistrationToken);

        return user;
    }


    private void handleGetUserResult(User user)
    {
        Log.i(TAG, "BEGINNING HANDLING RESULT OF GET USER");

        if (doCreateUser) {
            Log.i(TAG, "CREATING USER");
            api.createUser(user)
                    .flatMap(response -> api.lookupUser(response.headers().get("Location")))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(e -> Log.i(TAG, "ERROR: " + e.getMessage()))
                    .subscribe(this::startMainActivity);
        }
        else {
            Log.i(TAG, "NOT CREATING USER");
            startMainActivity(user);
        }
    }


    private void startProcessDialog()
    {
        mProgressDialog = new ProgressDialog(SignInActivity.this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    // TODO change to observable
    private void getRegistrationToken()
            throws IOException
    {
        while (mRegistrationToken == null)
        {
            if (!SharedPreferencesManager.getRegistrationFailed()) {
                mRegistrationToken = SharedPreferencesManager.getRegistrationToken();
            } else {
                Log.e(TAG, "failed to retrieve registration token");
                throw new IOException();
            }
        }
    }

    private void startMainActivity(User user)
    {
        SharedPreferencesManager.storeUserSync(user);
        SharedPreferencesManager.setSignedIn(true);
        mProgressDialog.dismiss();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}