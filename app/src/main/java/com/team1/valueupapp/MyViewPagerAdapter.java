package com.team1.valueupapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by eugene on 2015-06-30.
 */
public class MyViewPagerAdapter extends FragmentStatePagerAdapter {


    Fragment[] fragments = new Fragment[3];


    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[0] = new MainFragment();
        fragments[1] = new MainFragment();
        fragments[2] = new MainFragment();
    }


    public Fragment getItem(int arg0) {
        return fragments[arg0];
    }


    public int getCount() {
        return fragments.length;
    }

}