package com.example.kevin.fifastatistics.activities;

import android.os.Bundle;

import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;

public abstract class BasePlayerActivity extends FifaActivity {

    /** The name of the user. */
    public static final String NAME_EXTRA = "name";

    /** The image URL of the user. */
    public static final String IMAGE_URL_EXTRA = "imageurl";

    /** The ID of the user. */
    public static final String ID_EXTRA = "id";

    /** The registration token of the user */
    public static final String REG_TOKEN_EXTRA = "registrationToken";

    private String mPlayerId;
    private String mName;
    private String mImageUrl;
    private String mRegToken;
    private Friend mFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeMembers();
    }

    private void initializeMembers() {
        mPlayerId = getIntent().getStringExtra(ID_EXTRA);
        mName = getIntent().getStringExtra(NAME_EXTRA);
        mImageUrl = getIntent().getStringExtra(IMAGE_URL_EXTRA);
        mRegToken = getIntent().getStringExtra(REG_TOKEN_EXTRA);
    }

    public String getPlayerId() {
        return mPlayerId;
    }

    public String getName() {
        return mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getRegToken() {
        return mRegToken;
    }

    public Friend getFriend() {
        if (mFriend == null) {
            mFriend = Friend.builder().id(mPlayerId).imageUrl(mImageUrl).name(mName).registrationToken(mRegToken).build();
        }
        return mFriend;
    }
}
