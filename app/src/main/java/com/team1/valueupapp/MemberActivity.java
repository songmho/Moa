package com.team1.valueupapp;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by songmho on 2015-07-03.
 */
public class MemberActivity extends AppCompatActivity {
    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("참가자소개");
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        TabLayout tabLayout=(TabLayout)findViewById(R.id.tablayout);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
