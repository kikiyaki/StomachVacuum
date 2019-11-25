package com.sportsandhealth.iamkerel.stomachvacuum.lib;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.sportsandhealth.iamkerel.stomachvacuum.DB;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Помощник при создании напоминания
 */
public class NotificationHelper {

    private static String channelId = "MY_CHANNEL_ID";
    private static String name = "StomachVacuumChannel";
    private static String description = "StomachVacuumChannel";
    private static int notificationId = 777;

    // Тэг задачи воркера для уведомления
    private static final String workTag = "notificationWork";


    /**
     * Назначает повторяющееся каждый день уведомление в определенное время
     *
     * @param context контекст активности откуда выполняется метод
     * @param date Date время показа уведомления
     */
    public static void schedule(Context context, Date date) {

        // Для начала удалим существующие напоминания
        deleteNotification(context);


        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(NotifyWorker.class,
                30, TimeUnit.MINUTES)
                .setInitialDelay(9, TimeUnit.HOURS)
                .addTag(workTag)
                .build();


        /*
        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWorker.class)
                .setInitialDelay(5, TimeUnit.SECONDS)
                .addTag(workTag)
                .build();


         */

        WorkManager.getInstance(context).enqueueUniquePeriodicWork("notificationWork",
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWorkRequest);

        // Запись в БД
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
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
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
        MyNotification myNotification = new MyNotification(context, channelId, notificationId);
        myNotification.make();
    }


    /**
     * Отменить напоминание
     */
    public static void deleteNotification(Context context) {
        Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class) ;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

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
