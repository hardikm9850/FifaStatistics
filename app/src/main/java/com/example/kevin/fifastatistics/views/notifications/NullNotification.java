package com.example.kevin.fifastatistics.views.notifications;

import android.content.Context;
import android.os.Bundle;

/**
 * Null notification object used when no valid notification can be initialized.
 */
public class NullNotification extends FifaNotification
{
    public NullNotification(Context c, Bundle notification) {
        super(c, notification);
    }

    @Override
    public void performPreSendActions() {
        // do nothing
    }

    @Override
    protected void setContentText(Bundle notification) {
        // do nothing
    }

    @Override
    protected void setContentIntent() {
        // do nothing
    }
}
