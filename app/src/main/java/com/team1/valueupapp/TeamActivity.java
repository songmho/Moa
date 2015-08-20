package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyemi on 2015-08-16.
 */
public class TeamActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    RecyclerView.LayoutManager layoutManager;
    List<Team_item> items;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("팀빌딩");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);

        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        items=new ArrayList<>();

        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TeamActivity.this,TeamAddActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        final List<String> list_pick= ParseUser.getCurrentUser().getList("pick");
                        ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery("ValueUp_team");
                        parseQuery.whereEqualTo("ismade",true);
                        parseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                for (ParseObject o : list) {
                                    String same_mem="";
                                    List<String> list_member=o.getList("member");
                                    for(String s:list_member) {
                                        if(list_pick.contains(s))
                                            same_mem=same_mem+" "+s;
                                    }
                                    Team_item item = new Team_item(o.getString("idea"), o.getString("admin_member"), o.getString("idea_info"), same_mem, o.getList("member").size());
                                    items.add(item);
                                }
                                recyclerView.setAdapter(new Team_RecyclerAdapter(getApplicationContext(), items, R.layout.activity_team));

                            }
                        });

                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }
}//class
