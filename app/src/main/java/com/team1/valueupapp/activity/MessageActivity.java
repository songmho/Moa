package com.team1.valueupapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.team1.valueupapp.R;
import com.team1.valueupapp.fragment.MessageFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by songmho on 2016-05-08.
 */
public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.tabs) TabLayout tabLayout;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    MessageFragment sendMsgFragment;        //보낸
    MessageFragment resvMsgFragment;        //받은

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("쪽지함");

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        fab.setOnClickListener(this);
    }

    private void setUpViewPager(ViewPager viewPager) {
        MsgViewPagerAdapter adapter = new MsgViewPagerAdapter(getSupportFragmentManager());
        sendMsgFragment = new MessageFragment();
        resvMsgFragment = new MessageFragment();
        adapter.addFragment(resvMsgFragment,"받은쪽지함");
        adapter.addFragment(sendMsgFragment,"보낸쪽지함");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.fab){
            startActivity(new Intent(this,SendMsgActivity.class));
        }
    }


    private class MsgViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public MsgViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);

        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle=new Bundle();
            bundle.putString("cur_state",mFragmentTitleList.get(position));
            mFragmentList.get(position).setArguments(bundle);
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
