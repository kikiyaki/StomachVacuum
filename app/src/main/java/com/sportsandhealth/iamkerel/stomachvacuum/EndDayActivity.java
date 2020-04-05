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
import com.sportsandhealth.iamkerel.stomachvacuum.Promo.PromoCode;
import com.sportsandhealth.iamkerel.stomachvacuum.Promo.PromoCodeUnlock;

public class EndDayActivity extends Activity {
    private int level;
    private InterstitialAd mInterstitialAd;
    // По умолчанию не показывать рекламу
    private boolean showAd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_end_day);

        Log.e("QQQ", "Start End Day");

        Intent intent = getIntent();
        level = intent.getIntExtra("LEVEL", 0);


        if (showAd) {
            // Реклама
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {}
            });

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.end_day_interstitial_ad_unit_id));
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

    }

    public void toProgram(View view) {
        Intent intent = new Intent(this, ProgramActivity.class);
        intent.putExtra("LEVEL", level);
        startActivity(intent);
    }
}
