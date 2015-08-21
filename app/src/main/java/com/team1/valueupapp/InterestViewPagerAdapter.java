package com.team1.valueupapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by hyemi on 2015-08-18.
 */
public class InterestViewPagerAdapter extends FragmentStatePagerAdapter {
    Fragment[] fragments = new Fragment[2];
    Bundle[] bundle=new Bundle[2];

    public InterestViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[0] = new BasketFragment();
        bundle[0]=new Bundle();
        bundle[0].putInt("cur_job", 0);
        fragments[0].setArguments(bundle[0]);

        fragments[1] = new PickedFragment();
        bundle[1]=new Bundle();
        bundle[1].putInt("cur_job", 1);
        fragments[1].setArguments(bundle[1]);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "내가 찜한 멤버";
            case 1:
                return "나를 찜한 멤버";
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }
}
