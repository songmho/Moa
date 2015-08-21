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


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> s=new ArrayList<>();
                for(Teamadd_item i:items){
                    s.add(i.getName());
                }
                Intent intent=new Intent(TeamAddActivity.this,Team_Member_Add_Activity.class);
                startActivity(intent);
                ParseObject object=new ParseObject("ValueUp_team");
                object.put("idea",String.valueOf(title.getText()));
                object.put("idea_info",String.valueOf(title.getText()));
                object.put("ismade",false);
                object.put("admin_member",ParseUser.getCurrentUser().getString("name"));
                object.put("member", s);
                object.saveInBackground();
            }
        });
    }//onCreate

    @Override
    protected void onResume() {
        super.onResume();

        title.setText(ParseUser.getCurrentUser().getString("info"));
        detail.setText(ParseUser.getCurrentUser().getString("detail"));
        byte[] bytes=null;
        items=new ArrayList<>();

        if(items.size()==0){
            Teamadd_item item=new Teamadd_item(bytes,ParseUser.getCurrentUser().getString("name"));
            items.add(item);}
        else if(items.size()>0){
            ParseQuery<ParseObject> query=ParseQuery.getQuery("ValueUp_team");
            query.whereEqualTo("admin_member",ParseUser.getCurrentUser().getString("name"));
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if(list.isEmpty())
                        return;
                    else{
                        for(int i=0;i<list.get(0).getList("member").size();i++){
                            Teamadd_item item=new Teamadd_item(null,String.valueOf(list.get(0).getList("member").get(i)));
                            items.add(item);}
                    }
                }
            });
        }
        recyclerView.setAdapter(new TeamAddAdapter(getApplicationContext(), items));

        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_teamadd, menu);
        MenuItem additem=menu.findItem(R.id.action_add);
        additem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ParseQuery<ParseObject> query=ParseQuery.getQuery("ValueUp_team");
                query.whereEqualTo("admin_member",ParseUser.getCurrentUser().getString("name"));
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        ParseObject object=list.get(0);
                        object.put("idea",String.valueOf(title.getText()));
                        object.put("idea_info",String.valueOf(title.getText()));
                        object.put("ismade",true);
                        object.saveInBackground();
                    }
                });
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
