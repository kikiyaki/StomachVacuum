package com.sportsandhealth.iamkerel.stomachvacuum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sportsandhealth.iamkerel.stomachvacuum.lib.NotificationHelper;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Ставим нужный статус настройками
        TextView notificationStatusText = (TextView) findViewById(R.id.notificationStatus);
        if (NotificationHelper.isSet(this)) {
            notificationStatusText.setText(R.string.switch_on);
            notificationStatusText.setTextColor(getResources().getColor(R.color.basic));
        } else {
            notificationStatusText.setText(R.string.switch_off);
            notificationStatusText.setTextColor(getResources().getColor(R.color.light_grey_blue));
        }

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.settings_to_home);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        CardView card0 = (CardView) findViewById(R.id.notificationCard);
        card0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    /**
     * Метод для кнопки назад или кнопки выхода на макете
     */
    public void goBack() {
        Intent intent = new Intent(SettingsActivity.this, ProgramSelection.class);
        startActivity(intent);
    }
}
