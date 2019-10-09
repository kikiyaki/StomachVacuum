package com.sportsandhealth.iamkerel.stomachvacuum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotificationActivity extends Activity {

    private TimePicker timePicker;

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

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
    }

    public void notificationButtonOnClick(View view) {

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
