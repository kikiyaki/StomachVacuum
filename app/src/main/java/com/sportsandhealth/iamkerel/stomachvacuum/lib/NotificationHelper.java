package com.sportsandhealth.iamkerel.stomachvacuum.lib;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.sportsandhealth.iamkerel.stomachvacuum.DB;

import java.util.Calendar;
import java.util.Date;


/**
 * Помощник при создании напоминания
 */
public class NotificationHelper {

    public static String CHANNEL_ID = "MY_CHANNEL_ID";
    public static String NAME = "StomachVacuumChannel";
    public static String DESCRIPTION = "StomachVacuumChannel";
    public static int NOTIFICATION_ID = 777;


    /**
     * Назначает повторяющееся каждый день уведомление в определенное время
     *
     * @param context контекст активности откуда выполняется метод
     * @param date Date время показа уведомления
     */
    public static void schedule(Context context, Date date) {

        // Создаем канал уведомлений
        createNotificationChannel(context);
        // Удаляем строе напоминание
        deleteNotification(context);

        long time = date.getTime();
        // Округлить до минут
        time = (long) time / 60000;
        time = time * 60000;
        date.setTime(time);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("TIME", time);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, time, 24*60*60*1000, sender);

        setNotificationDatabase(context, date);

    }


    /**
     * Регистрирует канал уведомлений. Необходимо для Android 8.0 и выше
     * Выполнять при первом запуске приложения
     */
    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, NAME, importance);
            channel.setDescription(DESCRIPTION);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    /**
     * Показать уведомление
     */
    public static void showNotification(Context context) {
        MyNotification myNotification = new MyNotification(context, CHANNEL_ID, NOTIFICATION_ID);
        myNotification.make();
    }


    /**
     * Отменить напоминание
     */
    public static void deleteNotification(Context context) {
        Intent notificationIntent = new Intent(context, AlarmReceiver.class) ;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0,
                notificationIntent,
                0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        // Удаляем запись из БД
        deleteNotificationDatabase(context);
    }


    /**
     * Проверяет установлено ли напоминание
     *
     * @return
     */
    public static boolean isSet(Context context) {
        DB db = DB.getInstance(context);
        return  db.isSetNotification();
    }


    /**
     * Сохраняет запись об уведомлении в БД
     */
    public static void setNotificationDatabase(Context context, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        Log.e("QQQ", String.valueOf(hour));
        Log.e("QQQ", String.valueOf(minute));

        DB db = DB.getInstance(context);
        db.setNotification(hour, minute);
    }


    /**
     * Удаляет запись об уведомлении в БД
     */
    public static void deleteNotificationDatabase(Context context) {
        DB db = DB.getInstance(context);
        db.deleteNotification();
    }

}
