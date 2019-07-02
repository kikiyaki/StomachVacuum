package com.example.iamkerel.stomachvacuum;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class DayActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public Cursor cursor;

    public static int level;
    public static int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        Intent intent = new Intent(DayActivity.this, ProgramActivity.class);
        intent.putExtra("LEVEL", level);
        startActivity(intent);
    }
}
