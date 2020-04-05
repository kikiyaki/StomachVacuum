package com.sportsandhealth.iamkerel.stomachvacuum;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sportsandhealth.iamkerel.stomachvacuum.Promo.PromoCode;
import com.sportsandhealth.iamkerel.stomachvacuum.Promo.PromoCodeUnlock;

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
    // При ошибке загрузки ставится true
    // Например если выключен интернет
    private boolean adLoadingIsFailed = false;
    // По умолчанию не показывать рекламу
    private boolean showAd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Как можно раньше начать загрузку рекламы
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.day_interstitial_ad_unit_id));
        // Обработка ошибки загрузки
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adLoadingIsFailed = true;
            }
        });
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        PromoCode promoCode = new PromoCode(this);
        PromoCodeUnlock promoCodeUnlock = new PromoCodeUnlock(this,
                new PromoCodeUnlock.OnResponseListener() {
                    @Override
                    public void onUnlockTrue() {
                        showAd = false;
                    }

                    @Override
                    public void onUnlockFalse() {
                        showAd = true;
                    }

                    @Override
                    public void onError() {
                        showAd = false;
                    }
                });
        if (promoCodeUnlock.isUnlock()) {
            showAd = false;
        } else {
            showAd = true;
            if (promoCode.isExist()) {
                promoCodeUnlock.unlock();
            }
        }

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


    /**
     * Вызывается при нажатии кнопки начать тренировку
     * Если реклама загрузилась, показывает ее, далее после клика переходит на тренировку
     * Если не загрузилась, вешает листнер: после загрузки показать
     *  @param view
     */
    public void toTraining(View view) {

        // Интент перехода на тренировку
        final Intent intent = new Intent(this, TrainingActivity.class);
        intent.putExtra("LEVEL", DayActivity.level);
        intent.putExtra("DAY", DayActivity.day);

        // Если не удалось загрузить рекламу (например выключен инет)
        // Или по логике промокодов не нужно показывать рекламу
        // Сразу переходим в тренировку
        if (adLoadingIsFailed || !showAd) {
            startActivity(intent);
        } else {

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.setAdListener(new AdListener() {
                    // При закрытии рекламы переходим на тренировку
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        startActivity(intent);
                    }
                });

                mInterstitialAd.show();
            } else {
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        mInterstitialAd.show();
                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        startActivity(intent);
                    }

                });
            }

        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    /**
     * Метод для кнопки назад или кнопки выхода на макете
     */
    public void goBack() {
        Intent intent = new Intent(DayActivity.this, ProgramActivity.class);
        intent.putExtra("LEVEL", level);
        startActivity(intent);
    }

}
