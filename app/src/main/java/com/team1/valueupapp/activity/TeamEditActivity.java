package com.team1.valueupapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.team1.valueupapp.R;
import com.team1.valueupapp.adapter.TeamAddAdapter;
import com.team1.valueupapp.item.TeamAddItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyemi on 2015-07-26.
 */
public class TeamEditActivity extends AppCompatActivity {            //동명이인 처리가능하게 변경해야 됨.

    EditText title;
    EditText detail;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<TeamAddItem> items;
    ProgressBar progressBar;
    ArrayList<ParseUser> s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teamadd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("팀 정보 수정");

        Intent intent = getIntent();

        title=(EditText)findViewById(R.id.title);
        detail=(EditText)findViewById(R.id.detail);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        ImageView add=(ImageView)findViewById(R.id.add);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

//        title.setText(intent.getStringExtra("title"));
//        detail.setText(intent.getStringExtra("detail"));
        add.setVisibility(View.GONE);
    }//onCreate

    @Override
    protected void onResume() {
        super.onResume();
        items = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = getIntent();
                        progressBar.setVisibility(View.VISIBLE);
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
                        query.whereEqualTo("admin_member", ParseUser.getCurrentUser());
//                        query.whereEqualTo("idea", intent.getStringExtra("title"));
//                        query.whereEqualTo("idea_info", intent.getStringExtra("detail"));
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                title.setText(list.get(0).getString("idea"));
                                detail.setText(list.get(0).getString("idea_info"));
                                ParseRelation<ParseUser> relation = list.get(0).getRelation("member");
                                relation.getQuery().findInBackground(new FindCallback<ParseUser>() {
                                    @Override
                                    public void done(List<ParseUser> list, ParseException e) {
                                        for (int i = 0; i < list.size(); i++) {
                                            TeamAddItem item = new TeamAddItem(null, list.get(i).getString("name"),list.get(i).getObjectId());
                                            items.add(item);
                                        }
                                    }
                                });
                                recyclerView.setAdapter(new TeamAddAdapter(getApplicationContext(), items));
                                progressBar.setVisibility(View.GONE);

                            }
                        });
                    }
                });
            }
        }).start();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_teamadd, menu);
        MenuItem additem = menu.findItem(R.id.action_add);
        additem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
                query.whereEqualTo("admin_member", ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (!list.isEmpty()) {
                            ParseObject object = list.get(0);
//                            object.remove("idea");
                            object.put("idea", String.valueOf(title.getText()));
                            object.remove("idea_info");
                            object.put("idea_info", String.valueOf(detail.getText()));
                            object.remove("ismade");
                            object.put("ismade", true);
                            object.saveInBackground();
                        }
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
        switch (id){
            case android.R.id.home:
                ParseQuery<ParseObject> query=ParseQuery.getQuery("Team");
                query.whereEqualTo("admin_member", ParseUser.getCurrentUser());
                query.whereEqualTo("ismade",false);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        try {
                            if(!list.isEmpty())
                            list.get(0).delete();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                ParseQuery<ParseObject> query=ParseQuery.getQuery("Team");
                query.whereEqualTo("admin_member", ParseUser.getCurrentUser());
                query.whereEqualTo("ismade",false);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        try {
                            if(!list.isEmpty())
                            list.get(0).delete();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                break;
        }

        return true;
    }

}//class
