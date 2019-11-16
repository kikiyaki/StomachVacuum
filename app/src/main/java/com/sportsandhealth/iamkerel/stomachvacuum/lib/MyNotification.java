package com.sportsandhealth.iamkerel.stomachvacuum.lib;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.sportsandhealth.iamkerel.stomachvacuum.R;
import com.sportsandhealth.iamkerel.stomachvacuum.ProgramSelection;


/**
 * Уведомление
 */
public class MyNotification {

    /**
     * Контекст из которого выполняется создание уведомления
     */
    private Context context;

    private String channelId;
    private int notificationId;

    public MyNotification(Context context, String channelId, int notificationId) {
        this.context = context;
        this.channelId = channelId;
        this.notificationId = notificationId;
    }

    /**
     * Назначить новое уведомление
     * Показать уведомление
     */
    public void make() {

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this.context, ProgramSelection.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(R.string.notification))
                .setContentText(context.getString(R.string.time_to_training))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
    }


}
