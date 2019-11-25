package com.sportsandhealth.iamkerel.stomachvacuum.lib;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


/**
 * Воркер для показа уведомления даже при не запущенном приложении
 */
public class NotifyWorker extends Worker {

    public NotifyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Method to trigger an instant notification
        NotificationHelper.showNotification(getApplicationContext());

        return Result.success();
    }

}
