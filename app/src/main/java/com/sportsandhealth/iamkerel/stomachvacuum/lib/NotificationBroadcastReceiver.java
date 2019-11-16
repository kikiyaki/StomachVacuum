package com.sportsandhealth.iamkerel.stomachvacuum.lib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper.showNotification(context);

    }

}