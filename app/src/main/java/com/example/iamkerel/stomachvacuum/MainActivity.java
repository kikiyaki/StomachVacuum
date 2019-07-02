package com.example.iamkerel.stomachvacuum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.iamkerel.stomachvacuum.Entry.ScreenSlidePagerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, ScreenSlidePagerActivity.class);
        // Intent intent = new Intent(this, AndroidDatabaseManager.class);
        startActivity(intent);
    }
}
