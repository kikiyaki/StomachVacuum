package com.sportsandhealth.iamkerel.stomachvacuum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;

public class EndDayActivity extends Activity {
    private int level;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_day);

        Log.e("QQQ", "Start End Day");

        Intent intent = getIntent();
        level = intent.getIntExtra("LEVEL", 0);


        // Реклама
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8275330758485766/2616787844");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        // Показывает просто после загрузки
        // Сработает даже после перехода на другую активность
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
        });

    }

    public void toProgram(View view) {
        Intent intent = new Intent(this, ProgramActivity.class);
        intent.putExtra("LEVEL", level);
        startActivity(intent);
    }
}
