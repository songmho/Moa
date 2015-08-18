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
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songmho on 15. 8. 4.
 */
public class MentoringActivity extends AppCompatActivity {
    List<Mentoring_item> items;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentoring);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("멘토링일정");

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);

        items=new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void onResume() {
        super.onResume();

        Makinglist();
    }

    private void Makinglist() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        items.clear();
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("ValueUp_mentoring");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {

                                for (ParseObject o : list) {
                                    Mentoring_item item = new Mentoring_item(o.getString("job"),
                                            o.getInt("year"), o.getInt("month"), o.getInt("day"),
                                            o.getString("title"), o.getString("mentor"), o.getString("venue"));
                                    items.add(item);
                                }
                                recyclerView.setAdapter(new Mentoring_adapter(getApplicationContext(), items, R.layout.activity_mentoring));
                                progressBar.setVisibility(View.GONE);

                            }
                        });

                    }
                });
            }
        }).start();

    }
}
