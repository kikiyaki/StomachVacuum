package com.sportsandhealth.iamkerel.stomachvacuum.lib;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.sportsandhealth.iamkerel.stomachvacuum.R;
import com.sportsandhealth.iamkerel.stomachvacuum.SettingsActivity;

import java.util.Date;

/**
 * Уведомление
 */
public class MyNotification {

    /**
     * Контекст из которого выполняется создание уведомления
     */
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

    /**
     * Назначить новое уведомление
     *
     * @param date
     */
    public void create(Date date) {

        Log.e("QQQ", "Create notification");

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this.context, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, "MY_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(7777777, builder.build());
    }


    /**
     * Удаляет все созданные уведомления
     */
    private void delete(){

    }

}
