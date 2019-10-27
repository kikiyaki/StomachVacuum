package com.sportsandhealth.iamkerel.stomachvacuum;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DayActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public Cursor cursor;

    public static int level;
    public static int day;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Как можно раньше начать загрузку рекламы
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.day_interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        // Включить показ рекламы после загрузки
        enableAd();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        Intent intent = getIntent();
        level = intent.getIntExtra("LEVEL", 0);
        day = intent.getIntExtra("DAY", 0);

        // set title
        String title = "";
        switch (level) {
            case 0:
                title = getResources().getString(R.string.beginner) + ": " + String.valueOf(day+1) +
                        getResources().getString(R.string.th_day);
                break;
            case 1:
                title = getResources().getString(R.string.medium) + ": " + String.valueOf(day+1) +
                        getResources().getString(R.string.th_day);
                break;
            case 2:
                title = getResources().getString(R.string.advanced) + ": " + String.valueOf(day+1) +
                        getResources().getString(R.string.th_day);
                break;
        }
        TextView titleText = (TextView) findViewById(R.id.day_number);
        titleText.setText(title);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.day_to_program);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        DB db = DB.getInstance(this);
        cursor = db.getDaylyRoutine(level, day);

        recyclerView = (RecyclerView) findViewById(R.id.my_day_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyCursorAdapter(cursor, getResources(), this);
        recyclerView.setAdapter(mAdapter);
    }

    public void toTraining(View view) {
        // Отключить показ рекламы после загрузки
        // чтобы реклама с DayActivity не показывалась во время тренировки
        disableAd();

        Intent intent = new Intent(this, TrainingActivity.class);
        intent.putExtra("LEVEL", DayActivity.level);
        intent.putExtra("DAY", DayActivity.day);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    /**
     * Метод для кнопки назад или кнопки выхода на макете
     */
    public void goBack() {
        // Отключить показ рекламы после загрузки
        // чтобы реклама с DayActivity не показывалась в другой активности
        disableAd();

        Intent intent = new Intent(DayActivity.this, ProgramActivity.class);
        intent.putExtra("LEVEL", level);
        startActivity(intent);
    }


    /**
     * Включает показ рекламы после загрузки
     */
    private void enableAd() {
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
        });
    }


    /**
     * Отключает показ рекламы после загрузки
     */
    private void disableAd() {
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }
        });
    }
}
