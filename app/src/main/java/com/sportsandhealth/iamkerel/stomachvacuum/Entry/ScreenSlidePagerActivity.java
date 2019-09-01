package com.sportsandhealth.iamkerel.stomachvacuum.Entry;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.sportsandhealth.iamkerel.stomachvacuum.AndroidDatabaseManager;
import com.sportsandhealth.iamkerel.stomachvacuum.MainActivity;
import com.sportsandhealth.iamkerel.stomachvacuum.ProgramSelection;
import com.sportsandhealth.iamkerel.stomachvacuum.R;


public class ScreenSlidePagerActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

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

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPager.getCurrentItem() < 2) {
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                } else {
                    Intent intent = new Intent(ScreenSlidePagerActivity.this, ProgramSelection.class);
                    startActivity(intent);
                }
            }
        });
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
                break;

                case 1:
                    dot0.setImageResource(R.drawable.dot0);
                    dot1.setImageResource(R.drawable.dot1);
                    dot2.setImageResource(R.drawable.dot0);
                    break;

                case 2:
                    dot0.setImageResource(R.drawable.dot0);
                    dot1.setImageResource(R.drawable.dot0);
                    dot2.setImageResource(R.drawable.dot1);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
