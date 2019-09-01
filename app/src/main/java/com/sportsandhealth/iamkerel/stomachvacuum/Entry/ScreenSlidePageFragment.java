package com.sportsandhealth.iamkerel.stomachvacuum.Entry;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;
import android.widget.TextView;

import com.sportsandhealth.iamkerel.stomachvacuum.R;

public class ScreenSlidePageFragment extends Fragment {

    public int NUM_SLIDE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        TextView entryText1 = (TextView) rootView.findViewById(R.id.entry_text1);
        TextView entryText2 = (TextView) rootView.findViewById(R.id.entry_text2);

        switch (NUM_SLIDE) {
            case 0:
                entryText1.setText(getString(R.string.entry_first0));
                entryText2.setText(getString(R.string.entry_second0));
                break;
            case 1:
                entryText1.setText(getString(R.string.entry_first1));
                entryText2.setText(getString(R.string.entry_second1));
                break;
            case 2:
                entryText1.setText(getString(R.string.entry_first2));
                entryText2.setText(getString(R.string.entry_second2));
                break;
        }

        return rootView;
    }

}