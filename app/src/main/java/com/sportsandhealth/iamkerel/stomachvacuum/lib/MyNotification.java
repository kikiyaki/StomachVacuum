package com.sportsandhealth.iamkerel.stomachvacuum.lib;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

/**
 * Уведомление
 */
public class MyNotification {

    private Context context;

    public MyNotification(Context context) {
        this.context = context;
    }

    /**
     * Регистрирует канал уведомлений. Необходимо для Android 8.0 и выше
     * Выполнять при первом запуске приложения
     */
    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "StomachVacuumChannel";
            String description = "StomachVacuumChannel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("MY_CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = this.context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
