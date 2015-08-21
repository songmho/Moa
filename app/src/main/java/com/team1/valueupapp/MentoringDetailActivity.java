package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hyemi on 2015-08-05.
 */
public class MentoringDetailActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_mentoring_detail);

        final Intent intent=getIntent();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsing_toolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsing_toolbar.setTitle(intent.getStringExtra("title"));

        TextView mentor = (TextView) findViewById(R.id.mentor);
        TextView date = (TextView) findViewById(R.id.date);
        TextView venue = (TextView) findViewById(R.id.venue);
        TextView mentoring_info = (TextView) findViewById(R.id.mentoring_info);
        ImageView imageView=(ImageView)findViewById(R.id.imageView);

        if(intent.getStringExtra("job").equals("개발자"))
            imageView.setBackgroundResource(R.drawable.img_programming);
        else if(intent.getStringExtra("job").equals("디자이너"))
            imageView.setBackgroundResource(R.drawable.img_design);
        else
            imageView.setBackgroundResource(R.drawable.img_planning);

        mentor.setText(intent.getStringExtra("mentor"));
        date.setText(intent.getStringExtra("date"));
        venue.setText(intent.getStringExtra("venue"));
        mentoring_info.setText(intent.getStringExtra("title"));

    }
}//class
