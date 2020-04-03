package com.sportsandhealth.iamkerel.stomachvacuum;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.sportsandhealth.iamkerel.stomachvacuum.Entry.ScreenSlidePagerActivity;
import com.sportsandhealth.iamkerel.stomachvacuum.Promo.PromoCode;
import com.sportsandhealth.iamkerel.stomachvacuum.Promo.PromoCodeCreated;
import com.sportsandhealth.iamkerel.stomachvacuum.lib.NotificationHelper;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Создаем базу данных
        DB.getInstance(this);

        PromoCode promoCode = new PromoCode(this);
        if (!promoCode.isExist()) {
            PromoCodeCreated promoCodeCreated = new PromoCodeCreated(this);
            promoCodeCreated.create();
        }

        if (isFirstTime()) {
            // При первом запуске ставим уведомления на текущее время,
            // Первое через 24 часа
            long time = System.currentTimeMillis();
            time += 24*60*60*1000;
            Date date = new Date();
            date.setTime(time);
            NotificationHelper.schedule(this, date);

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
