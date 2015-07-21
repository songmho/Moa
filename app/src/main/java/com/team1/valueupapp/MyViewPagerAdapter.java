package com.team1.valueupapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by eugene on 2015-06-30.
 */
public class MyViewPagerAdapter extends FragmentStatePagerAdapter {


    Fragment[] fragments = new Fragment[3];
    Bundle[] bundle=new Bundle[3];

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[0] = new ListFragment();
        bundle[0]=new Bundle();
        bundle[0].putInt("cur_job",0);
        fragments[0].setArguments(bundle[0]);

        fragments[1] = new ListFragment();
        bundle[1]=new Bundle();
        bundle[1].putInt("cur_job",1);
        fragments[1].setArguments(bundle[1]);

        fragments[2] = new ListFragment();
        bundle[2]=new Bundle();
        bundle[2].putInt("cur_job",2);
        fragments[2].setArguments(bundle[2]);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "기획자";
            case 1:
                return "개발자";
            case 2:
                return "디자이너";
        }
        return null;
    }

    public Fragment getItem(int arg0) {
        return fragments[arg0];
    }


    public int getCount() {
        return fragments.length;
    }

}