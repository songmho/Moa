package com.team1.valueupapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyemi on 2015-07-26.
 */
public class AddActivity extends ActionBarActivity {
    RecyclerView add_recyclerView;
    LinearLayout container;
    ArrayList<Add_item> items;
    LinearLayoutManager layoutmanager;
    int cur_job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teamadd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("팀개설");


        add_recyclerView = (RecyclerView)findViewById(R.id.add_recyclerview);

        items = new ArrayList<Add_item>();

        layoutmanager = new LinearLayoutManager(AddActivity.this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);

        allList();

//        basketList();

    }//onCreate

    public void basketList() {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("ValueUp_people");
        query.whereEqualTo("pick", ParseUser.getCurrentUser().getString("name"));       //pick array에 현재 유저네임 있으면 그사람 출력.
        final List<Add_item> items=new ArrayList<>();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getString("job").equals("plan"))
                        cur_job = 0;
                    else if (list.get(i).getString("job").equals("dev"))
                        cur_job = 1;
                    else
                        cur_job = 2;
                    Add_item item = new Add_item(list.get(i).getString("name"), R.drawable.splash_logo, false);
                    items.add(item);
                }
                add_recyclerView.setAdapter(new Add_RecyclerAdapter(AddActivity.this, items, R.layout.add_recycler));
            }
        });

        add_recyclerView.setLayoutManager(layoutmanager);
        add_recyclerView.setItemAnimator(new DefaultItemAnimator());

    }//bastetList

    public void allList() {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("ValueUp_people");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        ParseObject ob = list.get(i);
                        Add_item item = new Add_item(ob.getString("name"), R.drawable.splash_logo, false);
                        items.add(item);
                    }
                    add_recyclerView.setAdapter(new Add_RecyclerAdapter(AddActivity.this, items, R.layout.add_recycler));
                }//end if
            }
        });//end query

        add_recyclerView.setLayoutManager(layoutmanager);
        add_recyclerView.setItemAnimator(new DefaultItemAnimator());

    }//allList

}//class
