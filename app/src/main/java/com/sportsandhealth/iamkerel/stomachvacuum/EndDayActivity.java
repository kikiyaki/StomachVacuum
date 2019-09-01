package com.sportsandhealth.iamkerel.stomachvacuum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class EndDayActivity extends Activity {
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_day);

        Log.e("QQQ", "Start End Day");

        Intent intent = getIntent();
        level = intent.getIntExtra("LEVEL", 0);
    }

    public void toProgram(View view) {
        Intent intent = new Intent(this, ProgramActivity.class);
        intent.putExtra("LEVEL", level);
        startActivity(intent);
    }
}
