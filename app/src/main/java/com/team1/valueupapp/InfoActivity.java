package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by songmho on 2015-07-04.
 */
public class InfoActivity extends ActionBarActivity {
    Toolbar toolbar;
    Boolean ischeck;

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
        setContentView(R.layout.activity_info);
        Intent intent=getIntent();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(intent.getStringExtra("name"));

        TextView idea=(TextView)findViewById(R.id.idea);
        TextView detail=(TextView)findViewById(R.id.detail);
        final ImageButton star=(ImageButton)findViewById(R.id.star);
        //ImageView
        idea.setText(intent.getStringExtra("idea"));
        detail.setText(intent.getStringExtra("detail"));
        star.setSelected(intent.getBooleanExtra("star",false));
        ischeck=intent.getBooleanExtra("star",false);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ischeck){
                    ischeck=false;
                    star.setSelected(false);
                    Toast.makeText(getApplicationContext(), "관심멤버에서 제외합니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    ischeck=true;
                    star.setSelected(true);
                    Toast.makeText(getApplicationContext(), "관심멤버에 추가합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
