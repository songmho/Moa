package com.team1.valueupapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugene on 2015-08-07.
 */
public class MentorActivity extends AppCompatActivity {
    List<Mentor_item> items;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("멘토소개");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        items = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ValueUp_mentor");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (int i = 0; i < list.size(); i++) {
                    Mentor_item item = new Mentor_item(list.get(i).getString("mentor_name"), list.get(i).getString("mentor_field"),
                            list.get(i).getString("company"), list.get(i).getString("email"));
                    items.add(item);
                }
                recyclerView.setAdapter(new Mentor_Adapter(getApplicationContext(), items, R.layout.activity_mentor));
            }
        });

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }
}