package com.sportsandhealth.iamkerel.stomachvacuum;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sportsandhealth.iamkerel.stomachvacuum.Entry.ScreenSlidePagerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isFirstTime()) {
            Intent intent = new Intent(this, ScreenSlidePagerActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ProgramSelection.class);
            startActivity(intent);
        }

    }

    /**
     * Возвращает труе если приложка запущена впервые, иначе фальсе ))
     * @return boolean
     */
    private boolean isFirstTime() {
            SharedPreferences mPreferences = this.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            boolean firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }

        return firstTime;
    }
}
