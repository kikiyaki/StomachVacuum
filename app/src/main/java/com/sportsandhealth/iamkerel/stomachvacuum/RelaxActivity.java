package com.sportsandhealth.iamkerel.stomachvacuum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.sportsandhealth.iamkerel.stomachvacuum.Promo.PromoCode;
import com.sportsandhealth.iamkerel.stomachvacuum.Promo.PromoCodeUnlock;


public class RelaxActivity extends Activity {
    int level;
    int day;
    int nextExercise;
    int nextExerciseTime;

    Cursor cursor;
    Handler handler = new Handler();
    int time = 30;
    int totalTime = 30;

    ProgressBar progressBar;
    TextView relaxTime;
    TextView relaxSkip;
    TextView relaxNextEx;
    TextView relaxNextTime;

    Intent intentNext;
    boolean showAd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        if (showAd) {
            setContentView(R.layout.activity_relax);

            MobileAds.initialize(this);

            AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.relax_native_ad_unit_id))
                    .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            UnifiedNativeAdView unifiedNativeAdView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.native_ad_layout, null);
                            mapUnifiedNativeAdToLayout(unifiedNativeAd, unifiedNativeAdView);

                            FrameLayout nativeAdLayout = findViewById(R.id.id_native_ad);
                            nativeAdLayout.removeAllViews();
                            nativeAdLayout.addView(unifiedNativeAdView);
                        }
                    })
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());
        } else {
            setContentView(R.layout.activity_relax_without_ads);
        }

        progressBar = (ProgressBar) findViewById(R.id.relax_progress);
        relaxTime = (TextView) findViewById(R.id.relax_time);
        relaxSkip = (TextView) findViewById(R.id.relax_skip);
        relaxNextEx = (TextView) findViewById(R.id.relax_next_exercise);
        relaxNextTime = (TextView) findViewById(R.id.relax_next_time);

        Intent intent = getIntent();
        level = intent.getIntExtra("LEVEL", 0);
        day = intent.getIntExtra("DAY", 0);

        Log.e("QQQ", String.valueOf(level)+"-"+String.valueOf(day));

        DB db = DB.getInstance(this);
        cursor = db.getNotDoneExercise(level, day);
        cursor.moveToFirst();
        nextExercise = cursor.getInt(0);
        nextExerciseTime = cursor.getInt(1);

        intentNext = new Intent(RelaxActivity.this, TrainingActivity.class);
        intentNext.putExtra("LEVEL", level);
        intentNext.putExtra("DAY", day);
        intentNext.putExtra("AFTER_RELAX", true);

        relaxSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacksAndMessages(null);
                startActivity(intentNext);
            }
        });

        setNextExercise();

        handler.postDelayed(timeUpdaterRunnable, 100);

    }

    Runnable timeUpdaterRunnable = new Runnable() {
        @Override
        public void run() {

            relaxTime.setText(String.valueOf(time));
            progressBar.setProgress(100-(100*time)/totalTime);

            if (time < 1) {
                handler.removeCallbacksAndMessages(null);
                startActivity(intentNext);
            } else {
                time--;
                // Loop handler
                handler.postDelayed(this, 1000);
            }
        }
    };

    public void setNextExercise() {
        switch (nextExercise) {
            case 0:
                relaxNextEx.setText(getResources().getString(R.string.lying_down));
                break;
            case 1:
                relaxNextEx.setText(getResources().getString(R.string.stand_up));
                break;
            case 2:
                relaxNextEx.setText(getResources().getString(R.string.table_top));
                break;
        }

        String next_time = String.valueOf(nextExerciseTime) + " " + getResources().getString(R.string.seconds);
        relaxNextTime.setText(next_time);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    /**
     * Метод для кнопки назад или кнопки выхода на макете
     */
    public void goBack() {
        handler.removeCallbacksAndMessages(null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RelaxActivity.this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.end_training));
        alertDialogBuilder
                .setMessage("")
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                handler.removeCallbacksAndMessages(null);

                                Intent intent = new Intent(RelaxActivity.this, DayActivity.class);
                                intent.putExtra("LEVEL", level);
                                intent.putExtra("DAY", day);

                                startActivity(intent);
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        handler.postDelayed(timeUpdaterRunnable, 100);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void mapUnifiedNativeAdToLayout(UnifiedNativeAd adFromGoogle, UnifiedNativeAdView myAdView) {
        MediaView mediaView = myAdView.findViewById(R.id.ad_media);
        myAdView.setMediaView(mediaView);

        myAdView.setHeadlineView(myAdView.findViewById(R.id.ad_headline));
        myAdView.setBodyView(myAdView.findViewById(R.id.ad_body));
        myAdView.setCallToActionView(myAdView.findViewById(R.id.ad_call_to_action));
        myAdView.setIconView(myAdView.findViewById(R.id.ad_icon));
        myAdView.setPriceView(myAdView.findViewById(R.id.ad_price));
        myAdView.setStarRatingView(myAdView.findViewById(R.id.ad_rating));
        myAdView.setStoreView(myAdView.findViewById(R.id.ad_store));
        myAdView.setAdvertiserView(myAdView.findViewById(R.id.ad_advertiser));

        ((TextView) myAdView.getHeadlineView()).setText(adFromGoogle.getHeadline());

        if (adFromGoogle.getBody() == null) {
            myAdView.getBodyView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getBodyView()).setText(adFromGoogle.getBody());
        }

        if (adFromGoogle.getCallToAction() == null) {
            myAdView.getCallToActionView().setVisibility(View.GONE);
        } else {
            ((Button) myAdView.getCallToActionView()).setText(adFromGoogle.getCallToAction());
        }

        if (adFromGoogle.getIcon() == null) {
            myAdView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) myAdView.getIconView()).setImageDrawable(adFromGoogle.getIcon().getDrawable());
        }

        if (adFromGoogle.getPrice() == null) {
            myAdView.getPriceView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getPriceView()).setText(adFromGoogle.getPrice());
        }

        if (adFromGoogle.getStarRating() == null) {
            myAdView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) myAdView.getStarRatingView()).setRating(adFromGoogle.getStarRating().floatValue());
        }

        if (adFromGoogle.getStore() == null) {
            myAdView.getStoreView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getStoreView()).setText(adFromGoogle.getStore());
        }

        if (adFromGoogle.getAdvertiser() == null) {
            myAdView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getAdvertiserView()).setText(adFromGoogle.getAdvertiser());
        }

        myAdView.setNativeAd(adFromGoogle);
    }
}
