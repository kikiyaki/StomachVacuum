package com.sportsandhealth.iamkerel.stomachvacuum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import java.io.IOException;

public class TrainingActivity extends Activity {
    private boolean afterRelax;
    private boolean afterHelp;
    private int level = 0;
    private int day = 0;
    private Cursor cursor;
    private DB db;

    TextView trainingPrepare;
    TextView trainingTitle;
    ImageView trainingImage;
    TextView prepareTime;
    View progressBack;
    View progressFront;
    int maxWidth;
    TextView trainingTime;
    AnimationDrawable trainingAnimation;

    FloatingActionButton helpBtn;
    FloatingActionButton backBtn;

    private int exercise;
    private int exerciseId;

    private Handler handler = new Handler();
    private int relaxTime = 5;
    private int totalTime;
    private int time;

    private SoundPool sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    private int soundStartId;
    private int soundEndId;
    private int soundCongratId;

    private InterstitialAd mInterstitialAd;
    private boolean adLoadIsFailed = false;
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
        setContentView(R.layout.activity_training);

        Log.e("QQQ", "Start Training");

        loadAd();
        loadSounds();

        trainingPrepare = (TextView) findViewById(R.id.training_prepare);
        trainingTitle = (TextView) findViewById(R.id.training_title);
        trainingImage = (ImageView) findViewById(R.id.training_image);
        prepareTime = (TextView) findViewById(R.id.prepare_time);
        progressBack = (View) findViewById(R.id.training_progress_back);
        progressFront = (View) findViewById(R.id.training_progress_front);
        trainingTime = (TextView) findViewById(R.id.training_time);

        helpBtn = (FloatingActionButton) findViewById(R.id.help);
        backBtn = (FloatingActionButton) findViewById(R.id.training_back);

        Intent intent = getIntent();
        level = intent.getIntExtra("LEVEL", 0);
        day = intent.getIntExtra("DAY", 0);
        afterRelax = intent.getBooleanExtra("AFTER_RELAX", false);
        afterHelp = intent.getBooleanExtra("AFTER_HELP", false);

        Log.e("TTT", String.valueOf(afterRelax));
        Log.e("TTT", String.valueOf(afterHelp));

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacksAndMessages(null);

                Intent intent = new Intent(TrainingActivity.this, HelpActivity.class);

                Log.e("TTT", String.valueOf(time));

                intent.putExtra("PAUSE_TIME", time);
                // relaxTime отсылаем чтобы при возвращении на TrainingActivity
                // если relaxTime > 0 продолжить подготовку к тренировке, либо
                // если relaxTime < 0 запустить таймер самой тренировки
                intent.putExtra("RELAX_TIME", relaxTime);
                intent.putExtra("EXERCISE", exercise);
                intent.putExtra("LEVEL", level);
                intent.putExtra("DAY", day);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        db = DB.getInstance(this);
        cursor = db.getNotDoneExercise(level, day);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {

            exercise = cursor.getInt(0);
            time = cursor.getInt(1);
            totalTime = time;
            exerciseId = cursor.getInt(2);

            if (afterHelp) {
                // Если после HelpActivity
                time = intent.getIntExtra("PAUSE_TIME", time);

                relaxTime = intent.getIntExtra("RELAX_TIME", 0);

                if (relaxTime > 0) {
                    // Если relaxTime еще осталось, то возобновляем таймер подготовки
                    setPrepareGraphics();
                    handler.postDelayed(timeUpdaterPrepare, 100);
                } else {
                    // Если relaxTime не осталось, возобновляем саму тренировку
                    setTrainingGraphics();
                    playStart();
                    handler.postDelayed(timeUpdaterRunnable, 100);
                }

            } else {
                if (!afterRelax) {
                    // Если первая тренировка, то запускаем таймер для подготовки
                    setPrepareGraphics();
                    handler.postDelayed(timeUpdaterPrepare, 100);
                } else {
                    // Если после отдыха, то сразу запускаем тренировку
                    relaxTime = 0;
                    setTrainingGraphics();
                    playStart();
                    handler.postDelayed(timeUpdaterRunnable, 100);
                }
            }


        } else {

            Intent intent1 = new Intent(this, EndDayActivity.class);
            intent.putExtra("LEVEL", level);
            startActivity(intent1);
        }
    }

    private Runnable timeUpdaterRunnable = new Runnable() {
        @Override
        public void run() {

            String training_time = String.valueOf(time)+"/"+String.valueOf(totalTime);
            trainingTime.setText(training_time);

            if (time < 1) {
                progressFront.getLayoutParams().width = maxWidth;
                db.setDone(exerciseId);

                // If it was last exercise for this day
                cursor = db.getNotDoneExercise(level, day);
                if (cursor.getCount() < 1) {
                    playCongrat();
                    Intent intent = new Intent(TrainingActivity.this, EndDayActivity.class);
                    intent.putExtra("LEVEL", level);
                    startActivity(intent);
                } else {
                    playEnd();
                    Intent intent = new Intent(TrainingActivity.this, RelaxActivity.class);
                    intent.putExtra("LEVEL", level);
                    intent.putExtra("DAY", day);
                    startActivity(intent);
                }

            } else {

                maxWidth = progressBack.getWidth();
                Log.e("QQQ", "maxWidth="+String.valueOf(maxWidth));

                progressFront.getLayoutParams().width = maxWidth*(100*(totalTime-time))/100/totalTime;
                time--;
                // Loop handler
                handler.postDelayed(this, 1000);
            }
        }
    };

    private Runnable timeUpdaterPrepare = new Runnable() {
        @Override
        public void run() {

            if (relaxTime < 1) {
                prepareTime.setVisibility(View.INVISIBLE);
                setTrainingGraphics();
                playStart();
                handler.postDelayed(timeUpdaterRunnable, 500);
            } else {
                prepareTime.setText(String.valueOf(relaxTime));
                relaxTime--;
                // Loop handler
                handler.postDelayed(this, 1000);
            }
        }
    };

    private void setTrainingGraphics(){

        trainingPrepare.setVisibility(View.INVISIBLE);

        setExercise();

        progressFront.setVisibility(View.VISIBLE);
        progressBack.setVisibility(View.VISIBLE);

        trainingTime.setVisibility(View.VISIBLE);

    }

    private void setPrepareGraphics(){

        setExercise();

        progressFront.setVisibility(View.INVISIBLE);
        progressBack.setVisibility(View.INVISIBLE);
        trainingTime.setVisibility(View.INVISIBLE);
    }

    public void setExercise() {

        switch (exercise) {
            case 0:
                trainingTitle.setText(getResources().getString(R.string.lying_down));
                trainingImage.setBackgroundResource(R.drawable.lying_down);
                break;
            case 1:
                trainingTitle.setText(getResources().getString(R.string.stand_up));
                trainingImage.setBackgroundResource(R.drawable.stand_up);
                break;
            case 2:
                trainingTitle.setText(getResources().getString(R.string.table_top));
                trainingImage.setBackgroundResource(R.drawable.table_top);
                break;
        }

        trainingAnimation = (AnimationDrawable) trainingImage.getBackground();
        trainingAnimation.setExitFadeDuration(400);
        trainingAnimation.start();
    }

    // Удаляем поток перед скрытием приложки
    @Override
    protected void onPause() {
        super.onPause();

        Log.e("QQQ", "onPause");
        handler.removeCallbacksAndMessages(null);
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        if (relaxTime > 0) {
            Log.e("QQQ", "prepare");
            setPrepareGraphics();
            handler.postDelayed(timeUpdaterPrepare, 100);
        } else {
            Log.e("QQQ", "run");
            setTrainingGraphics();
            playStart();
            handler.postDelayed(timeUpdaterRunnable, 100);
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
        handler.removeCallbacksAndMessages(null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TrainingActivity.this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.end_training));
        alertDialogBuilder
                .setMessage("")
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (adLoadIsFailed || !showAd) {
                                    goToDay();
                                } else {
                                    // Показываем рекламу и переходим в DayActivity по закритии рекламы
                                    mInterstitialAd.setAdListener(new AdListener() {
                                        @Override
                                        public void onAdClosed() {
                                            super.onAdClosed();
                                            goToDay();
                                        }
                                    });

                                    mInterstitialAd.show();
                                }

                            }
                        })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        handler.postDelayed(timeUpdaterPrepare, 100);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void loadSounds(){
        try {
            soundStartId = sp.load(getAssets().openFd("start.ogg"), 1);
            soundEndId = sp.load(getAssets().openFd("end.ogg"), 1);
            soundCongratId = sp.load(getAssets().openFd("congrat.ogg"), 1);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("AAA", "Load sound error");
        }
    }

    public void playStart() {
        Log.e("QQQ", "play start<<");
        sp.play(soundStartId, 1,1,1,0,1);
        Log.e("QQQ", ">>play start");
    }

    public void playEnd() {
        Log.e("QQQ", "play end<<");
        sp.play(soundEndId, 1,1,1,0,1);
        Log.e("QQQ", ">>play end");
    }

    public void playCongrat() {
        Log.e("QQQ", "play congrat<<");
        sp.play(soundCongratId, 1,1,1,0,1);
        Log.e("QQQ", ">>play congrat");
    }


    /**
     * Загрузить рекламу
     */
    private void loadAd() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.end_training_ad_unit_id));

        // При ошибке загрузки меняем глобальную переменную
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adLoadIsFailed = true;
            }
        });

        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }


    /**
     * Убивает потоки и переходит в DayActivity
     */
    private void goToDay() {
        handler.removeCallbacksAndMessages(null);

        Intent intent = new Intent(TrainingActivity.this, DayActivity.class);
        intent.putExtra("LEVEL", level);
        intent.putExtra("DAY", day);
        startActivity(intent);
    }

}
