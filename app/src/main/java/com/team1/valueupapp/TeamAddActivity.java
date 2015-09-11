package com.team1.valueupapp;

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

import java.util.ArrayList;
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
    ProgressBar progressBar;
    ArrayList<ParseUser> s;

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
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        ImageView add=(ImageView)findViewById(R.id.add);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s=new ArrayList<ParseUser>();
                for(Teamadd_item i:items){
                    ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
                    parseQuery.whereEqualTo("objectId", i.getObjectId());
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(final List<ParseUser> list, ParseException e) {
                            s.add(list.get(0));
                        }
                    });
                }

                ParseQuery<ParseObject> query=ParseQuery.getQuery("Team");
                query.whereEqualTo("admin_member", ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (list.isEmpty()) {
                            ParseObject object = new ParseObject("Team");
                            object.put("idea", String.valueOf(title.getText()));
                            object.put("idea_info", String.valueOf(detail.getText()));
                            object.put("ismade", false);
                            object.put("admin_member", ParseUser.getCurrentUser());
                            ParseRelation<ParseUser> parseUsers=object.getRelation("member");
                            parseUsers.add(ParseUser.getCurrentUser());
                            object.saveInBackground();

                            for(ParseUser user : s) {
                                object.getRelation("member").add(user);
                                object.saveInBackground();
                            }
                        } else {
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).deleteInBackground();
                            }//end for
                            ParseObject object = new ParseObject("Team");
                            object.put("idea", String.valueOf(title.getText()));
                            object.put("idea_info", String.valueOf(detail.getText()));
                            object.put("ismade", false);
                            object.put("admin_member", ParseUser.getCurrentUser());
                            ParseRelation<ParseUser> parseUsers=object.getRelation("member");
                            parseUsers.add(ParseUser.getCurrentUser());
                            object.saveInBackground();

                            for(ParseUser user : s) {
                                object.getRelation("member").add(user);
                                object.saveInBackground();
                            }
                        }
                    }
                });

                Intent intent=new Intent(TeamAddActivity.this,Team_Member_Add_Activity.class);
                startActivity(intent);
            }
        });//add.setOnClickListener
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
                        progressBar.setVisibility(View.VISIBLE);
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
                        query.whereEqualTo("admin_member", ParseUser.getCurrentUser());
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                if (list.isEmpty()) {
                                    title.setText(ParseUser.getCurrentUser().getString("info"));
                                    detail.setText(ParseUser.getCurrentUser().getString("detail"));
                                    Teamadd_item item = new Teamadd_item(null, ParseUser.getCurrentUser().getString("name"),ParseUser.getCurrentUser().getObjectId());
                                    items.add(item);
                                } else {
                                    title.setText(list.get(0).getString("idea"));
                                    detail.setText(list.get(0).getString("idea_info"));
                                    ParseRelation<ParseUser> relation = list.get(0).getRelation("member");
                                    relation.getQuery().findInBackground(new FindCallback<ParseUser>() {
                                        @Override
                                        public void done(List<ParseUser> list, ParseException e) {
                                            for (int i = 0; i < list.size(); i++) {
                                                Teamadd_item item = new Teamadd_item(null, list.get(i).getString("name"), list.get(i).getObjectId());
                                                items.add(item);
                                            }
                                        }
                                    });
                                }//end else
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
                            object.put("ismade", false);
                            object.saveInBackground();
                        } else {
                            ParseObject object = new ParseObject("Team");
                            object.put("idea", String.valueOf(title.getText()));
                            object.put("idea_info", String.valueOf(detail.getText()));
                            object.put("ismade", false);
                            object.put("admin_member", ParseUser.getCurrentUser());

                            object.getRelation("member").add(ParseUser.getCurrentUser());
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
