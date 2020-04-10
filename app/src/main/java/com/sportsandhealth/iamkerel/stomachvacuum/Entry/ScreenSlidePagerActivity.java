package com.sportsandhealth.iamkerel.stomachvacuum.Entry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sportsandhealth.iamkerel.stomachvacuum.AndroidDatabaseManager;
import com.sportsandhealth.iamkerel.stomachvacuum.MainActivity;
import com.sportsandhealth.iamkerel.stomachvacuum.ProgramSelection;
import com.sportsandhealth.iamkerel.stomachvacuum.R;
import com.sportsandhealth.iamkerel.stomachvacuum.lib.MyNotification;
import com.sportsandhealth.iamkerel.stomachvacuum.lib.NotificationHelper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class ScreenSlidePagerActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 4;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;

    ImageView dot0;
    ImageView dot1;
    ImageView dot2;
    ImageView dot3;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        mPager.addOnPageChangeListener(onPageChangeListener);

        dot0 = findViewById(R.id.dot0);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPager.getCurrentItem() < 3) {
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                } else {
                    Intent intent = new Intent(ScreenSlidePagerActivity.this, ProgramSelection.class);
                    startActivity(intent);
                }
            }
        });

        // Регистрируем канал уведомлений
        NotificationHelper.createNotificationChannel(this);

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // Ничего не делаем, никуда не выходим
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
            fragment.NUM_SLIDE = position;
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            switch (i) {
                case 0:
                    dot0.setImageResource(R.drawable.dot1);
                    dot1.setImageResource(R.drawable.dot0);
                    dot2.setImageResource(R.drawable.dot0);
                    dot3.setImageResource(R.drawable.dot0);
                    break;

                case 1:
                    dot0.setImageResource(R.drawable.dot0);
                    dot1.setImageResource(R.drawable.dot1);
                    dot2.setImageResource(R.drawable.dot0);
                    dot3.setImageResource(R.drawable.dot0);
                    break;

                case 2:
                    dot0.setImageResource(R.drawable.dot0);
                    dot1.setImageResource(R.drawable.dot0);
                    dot2.setImageResource(R.drawable.dot1);
                    dot3.setImageResource(R.drawable.dot0);
                    break;
                case 3:
                    dot0.setImageResource(R.drawable.dot0);
                    dot1.setImageResource(R.drawable.dot0);
                    dot2.setImageResource(R.drawable.dot0);
                    dot3.setImageResource(R.drawable.dot1);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
