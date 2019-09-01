package com.sportsandhealth.iamkerel.stomachvacuum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgramSelection extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("ddddd", "sdfsdfsdfsdfc");

        setContentView(R.layout.activity_program_selection);

        CardView card0 = (CardView) findViewById(R.id.level_card_0);
        CardView card1 = (CardView) findViewById(R.id.level_card_1);
        CardView card2 = (CardView) findViewById(R.id.level_card_2);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.exit_btn);

        card0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProgramSelection.this, ProgramActivity.class);
                intent.putExtra("LEVEL", 0);
                startActivity(intent);
            }
        });

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProgramSelection.this, ProgramActivity.class);
                intent.putExtra("LEVEL", 1);
                startActivity(intent);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProgramSelection.this, ProgramActivity.class);
                intent.putExtra("LEVEL", 2);
                startActivity(intent);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        ProgressBar bar0 = (ProgressBar) findViewById(R.id.level_progress_0);
        ProgressBar bar1 = (ProgressBar) findViewById(R.id.level_progress_1);
        ProgressBar bar2 = (ProgressBar) findViewById(R.id.level_progress_2);

        // TextViews inside progress bars
        TextView text0 = (TextView) findViewById(R.id.level_progress_text_0);
        TextView text1 = (TextView) findViewById(R.id.level_progress_text_1);
        TextView text2 = (TextView) findViewById(R.id.level_progress_text_2);

        DB db = DB.getInstance(this);
        int progress[] = db.getLevelProgress();

        bar0.setProgress(progress[0]);
        bar1.setProgress(progress[1]);
        bar2.setProgress(progress[2]);

        String prog0 = String.valueOf(progress[0])+"%";
        text0.setText(prog0);
        String prog1 = String.valueOf(progress[1])+"%";
        text1.setText(prog1);
        String prog2 = String.valueOf(progress[2])+"%";
        text2.setText(prog2);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    /**
     * Метод для кнопки назад или кнопки выхода на макете
     */
    public void goBack() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProgramSelection.this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.exit_from_app));
        alertDialogBuilder
                .setMessage("")
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //moveTaskToBack(true);
                                //android.os.Process.killProcess(android.os.Process.myPid());
                                //System.exit(1);
                                ProgramSelection.this.finishAffinity();
                                System.exit(0);
                            }
                        })

                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
