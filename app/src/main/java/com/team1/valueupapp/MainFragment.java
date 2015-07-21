package com.team1.valueupapp;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by songmho on 2015-07-03.
 */
public class MainFragment extends Fragment {
    LinearLayout cur_container;
    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cur_container=(LinearLayout)inflater.inflate(R.layout.fragment_main, container, false);
        viewPager = (ViewPager)cur_container.findViewById(R.id.viewPager);
        TabLayout tabLayout=(TabLayout)cur_container.findViewById(R.id.tablayout);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return cur_container;
    }
}
