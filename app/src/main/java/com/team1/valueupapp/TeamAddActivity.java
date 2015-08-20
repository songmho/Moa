package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hyemi on 2015-07-26.
 */
public class TeamAddActivity extends AppCompatActivity {            //동명이인 처리가능하게 변경해야 됨.

    EditText title;
    EditText detail;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Teamadd_item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teamadd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("팀개설");

        title=(EditText)findViewById(R.id.title);
        detail=(EditText)findViewById(R.id.detail);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        ImageView add=(ImageView)findViewById(R.id.add);
        if(getIntent().getStringExtra("info")!=null){
            title.setText(getIntent().getStringExtra("info"));
            detail.setText(getIntent().getStringExtra("detail"));
        }
        else {
            title.setText(ParseUser.getCurrentUser().getString("info"));
            detail.setText(ParseUser.getCurrentUser().getString("detail"));
        }
        byte[] bytes=null;
        items=new ArrayList<>();
        Teamadd_item item=new Teamadd_item(bytes,"송명호");
        items.add(item);items.add(item);items.add(item);items.add(item);items.add(item);items.add(item);
        recyclerView.setAdapter(new TeamAddAdapter(getApplicationContext(), items));

        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TeamAddActivity.this,Team_Member_Add_Activity.class);
                ParseObject object=new ParseObject("ValueUp_team");
            //    object.
                startActivity(intent);
            }
        });
    }//onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_teamadd, menu);
        MenuItem additem=menu.findItem(R.id.action_add);
        additem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

}//class
