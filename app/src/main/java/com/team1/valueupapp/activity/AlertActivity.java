package com.team1.valueupapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.team1.valueupapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by songmho on 2016-05-07.
 */
public class AlertActivity extends AppCompatActivity {

    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        ButterKnife.bind(this);     //Butterknife 사용

        toolbar.setTitle("알림");     //toolbar title 설정
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }
}
