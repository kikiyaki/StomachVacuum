package com.sportsandhealth.iamkerel.stomachvacuum;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sportsandhealth.iamkerel.stomachvacuum.lib.NotificationHelper;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class NotificationActivity extends Activity {

    private TimePicker timePicker;

    private Button onButton;
    private Button offButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.notifications_back);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        onButton = (Button) findViewById(R.id.onNotification);
        offButton = (Button) findViewById(R.id.offNotification);

        // Если уведомление уже установлено, ставим текст кнопки ВКЛ/СОХР на СОХР
        if (NotificationHelper.isSet(this)) {
            onButton.setText(getString(R.string.save));
        } else {
            onButton.setText(getString(R.string.on));
        }

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // Меняем текст кнопки ВКЛ/СОХР на СОХР
                onButton.setText(R.string.save);
            }
        });
    }


    /**
     * Обрабатываем кнопку ВКЛ/СОХР
     */
    public void onNotificationClick(View view) {

        onButton.setText(getString(R.string.save));

        // Получаем время с таймпикера
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        Date date = calendar.getTime();

        // Назначаем уведомление
        NotificationHelper.schedule(this, date);

        String dateString = DateFormat.getTimeInstance(DateFormat.SHORT).format(date);
        String toastText = getString(R.string.notification_was_created) + " " + dateString;
        Toast.makeText(this, toastText, Toast.LENGTH_LONG)
                .show();
    }


    /**
     * Обрабатываем кнопку ВЫКЛ
     */
    public void offNotificationClick(View view) {

        // Во-первых при выключении меняем кнопку ВКЛ/СОХР на ВКЛ
        onButton.setText(R.string.on);
        NotificationHelper.deleteNotification(this);

        Toast.makeText(this, getString(R.string.notification_was_deleted), Toast.LENGTH_LONG)
                .show();
    }


    @Override
    public void onBackPressed() {
        goBack();
    }


    /**
     * Метод для кнопки назад или кнопки выхода на макете
     */
    public void goBack() {
        Intent intent = new Intent(NotificationActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

}
