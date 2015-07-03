package com.team1.valueupapp;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by songmho on 2015-07-03.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    Button btn[] = new Button[3];
    LinearLayout cur_container;
    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cur_container=(LinearLayout)inflater.inflate(R.layout.fragment_main,container,false);
        viewPager = (ViewPager)cur_container.findViewById(R.id.viewPager);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getActivity().getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        btn[0] = (Button)cur_container.findViewById(R.id.btn_a);
        btn[1] = (Button)cur_container.findViewById(R.id.btn_b);
        btn[2] = (Button)cur_container.findViewById(R.id.btn_c);
        for (Button aBtn : btn) aBtn.setOnClickListener(this);
        return cur_container;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_a:
                viewPager.setCurrentItem(0);
                break;
            case R.id.btn_b:
                viewPager.setCurrentItem(1);
                break;
            case R.id.btn_c:
                viewPager.setCurrentItem(2);
                break;
            default:
                break;

        }
    }
}
