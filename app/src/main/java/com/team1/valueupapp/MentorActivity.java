package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by hyemi on 2015-08-07.
 */
public class MentorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor);

        final Intent intent=getIntent();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsing_toolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsing_toolbar.setTitle(intent.getStringExtra("mentor_name"));

        TextView mentor_field = (TextView) findViewById(R.id.mentor_field);
        TextView company = (TextView) findViewById(R.id.company);
        TextView email = (TextView) findViewById(R.id.email);

        mentor_field.setText(intent.getStringExtra("mentor_field"));
        mentor_field.setTextColor(getResources().getColor(R.color.tab_color));
        mentor_field.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
        mentor_field.setPadding(30, 15, 30, 15);

        company.setText(intent.getStringExtra("company"));
        email.setText(intent.getStringExtra("email"));

    }//onCreate

}//class
