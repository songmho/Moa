package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by songmho on 2015-07-21.
 */
public class TeamActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        final Intent intent=getIntent();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(intent.getStringExtra("idea"));

        CollapsingToolbarLayout collapsing_toolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsing_toolbar.setTitle(intent.getStringExtra("idea"));

        TextView idea=(TextView)findViewById(R.id.idea);

        View[] v=new View[6];
        TextView[] j=new TextView[6];
        TextView[] n=new TextView[6];
        CircleImageView[] c=new CircleImageView[6];

        v[0]=findViewById(R.id.people1);
        v[1]=findViewById(R.id.people2);
        v[2]=findViewById(R.id.people3);
        v[3]=findViewById(R.id.people4);
        v[4]=findViewById(R.id.people5);
        v[5]=findViewById(R.id.people6);

        for(int i=0;i<6;i++) {
            n[i] = (TextView) v[i].findViewById(R.id.name);
            j[i] = (TextView) v[i].findViewById(R.id.job);
            c[i]=(CircleImageView)v[i].findViewById(R.id.profile);
        }
    }
}
