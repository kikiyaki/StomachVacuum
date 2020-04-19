package com.sportsandhealth.iamkerel.stomachvacuum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HelpActivity extends Activity {
    private int pauseTime;
    private int relaxTime;
    private int exercise;
    private int level;
    private int day;
    private FloatingActionButton backBtn;

    private TextView title;
    private ImageView imageView;
    private TextView text;
    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        title = (TextView) findViewById(R.id.help_title);
        imageView = (ImageView) findViewById(R.id.help_image);
        text = (TextView) findViewById(R.id.help_text);

        Intent intent = getIntent();
        pauseTime = intent.getIntExtra("PAUSE_TIME", 0);
        relaxTime = intent.getIntExtra("RELAX_TIME", 0);

        exercise = intent.getIntExtra("EXERCISE", 0);
        level = intent.getIntExtra("LEVEL", 0);
        day = intent.getIntExtra("DAY", 0);

        switch (exercise) {
            case 0:
                title.setText(getResources().getString(R.string.lying_down));
                imageView.setBackgroundResource(R.drawable.lying_down);
                text.setText(getResources().getString(R.string.lying_down_descr));
                break;
            case 1:
                title.setText(getResources().getString(R.string.stand_up));
                imageView.setBackgroundResource(R.drawable.stand_up);
                text.setText(getResources().getString(R.string.stand_up_descr));
                break;
            case 2:
                title.setText(getResources().getString(R.string.table_top));
                imageView.setBackgroundResource(R.drawable.table_top);
                text.setText(getResources().getString(R.string.table_top_descr));
                break;
        }

        animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.setExitFadeDuration(400);
        animationDrawable.start();

        backBtn = (FloatingActionButton) findViewById(R.id.help_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
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
        Intent intent = new Intent(HelpActivity.this, TrainingActivity.class);
        intent.putExtra("AFTER_HELP", true);
        intent.putExtra("PAUSE_TIME", pauseTime);
        intent.putExtra("RELAX_TIME", relaxTime);
        intent.putExtra("LEVEL", level);
        intent.putExtra("DAY", day);
        startActivity(intent);
    }
}
