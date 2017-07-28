package com.example.kevin.fifastatistics.interfaces;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public interface ActivityLauncher {

    void launchActivity(Intent intent, int requestCode, Bundle options);
    Context getContext();
}
