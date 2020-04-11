package com.sportsandhealth.iamkerel.stomachvacuum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sportsandhealth.iamkerel.stomachvacuum.lib.CurrentContext;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProgramActivity extends Activity {
    public static int LEVEL;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton floatingActionButton;

    public int[] progressDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        CurrentContext.set(this);

        // Set selected level
        Intent intent = getIntent();
        LEVEL = intent.getIntExtra("LEVEL", 0);


        TextView levelText = (TextView) findViewById(R.id.program_level);
        switch (LEVEL) {
            case 0:
                levelText.setText(getResources().getString(R.string.beginner));
                break;
            case 1:
                levelText.setText(getResources().getString(R.string.medium));
                break;
            case 2:
                levelText.setText(getResources().getString(R.string.advanced));
                break;
        }

        floatingActionButton = (FloatingActionButton) findViewById(R.id.program_to_program_selection);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        DB db = DB.getInstance(this);
        progressDataset = db.getDailyProgress(LEVEL);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(progressDataset, getResources(), this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    /**
     * Метод для кнопки назад или кнопки выхода на макете
     */
    public void goBack() {
        Intent intent = new Intent(ProgramActivity.this, ProgramSelection.class);
        startActivity(intent);
    }
}
