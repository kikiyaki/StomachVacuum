package com.sportsandhealth.iamkerel.stomachvacuum.lib;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

/**
 * Помощник при создании напоминания
 */
public class NotificationHelper {

    /**
     * Назначает повторяющееся каждый день уведомление в определенное время
     *
     * @param context контекст активности откуда выполняется метод
     * @param date Date время показа уведомления
     */
    public static void schedule(Context context, Date date) {

        Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class) ;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE );
        assert alarmManager != null;

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, date.getTime(),
                1000*60, pendingIntent);
    }

}
