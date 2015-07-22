package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
        RecyclerView recyclerview=(RecyclerView)findViewById(R.id.recyclerview);

        List<TeamlistItem> items=new ArrayList<>();
        TeamlistItem item=new TeamlistItem(R.drawable.splash_logo,"최에스더",2);
        items.add(item);
        recyclerview.setAdapter(new RecyclerAdpater(getApplicationContext(), items, R.layout.item_teamlist, 0, 0));
        RecyclerAdpater adapter=new RecyclerAdpater(getApplicationContext(), items, R.layout.item_teamlist, 0, 0);

        recyclerview.getLayoutParams().height=adapter.getItemCount()*100;
    }
}
