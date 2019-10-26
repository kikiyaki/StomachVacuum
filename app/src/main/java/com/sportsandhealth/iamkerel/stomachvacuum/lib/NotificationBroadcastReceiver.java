package com.sportsandhealth.iamkerel.stomachvacuum.lib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        MyNotification myNotification = new MyNotification(context);
        myNotification.make();

    }

}
