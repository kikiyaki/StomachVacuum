package com.sportsandhealth.iamkerel.stomachvacuum.lib;

import android.app.Activity;

public class CurrentContext {
    public static Activity currentActivity;

    public static void set(Activity activity) {
        currentActivity = activity;
    }

    public static Activity get() {
        return currentActivity;
    }
}
