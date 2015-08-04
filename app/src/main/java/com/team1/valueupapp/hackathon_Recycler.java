package com.team1.valueupapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugene on 2015-08-02.
 */
public class hackathon_Recycler extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hackathon);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.hkt_recyclerview);

        List<HackathonItem> dataItems = new ArrayList<>();
        dataItems.add(new HackathonItem("test", "test"));


        HackathonAdapter myAdapter = new HackathonAdapter(dataItems);
        recyclerView.setAdapter(myAdapter);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }
}