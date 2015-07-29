package com.team1.valueupapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

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
public class AddActivity extends AppCompatActivity {            //동명이인 처리가능하게 변경해야 됨.
    RecyclerView add_recyclerView;
    LinearLayout container;
    ArrayList<Add_item> items;
    LinearLayoutManager layoutmanager;
    int cur_job;
    List<String> members=new ArrayList<>();
    List<String> plan=new ArrayList<>();
    List<String> dev=new ArrayList<>();
    List<String> des=new ArrayList<>();
    String[] members_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teamadd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("팀개설");


        add_recyclerView = (RecyclerView)findViewById(R.id.add_recyclerview);

        items = new ArrayList<>();

        layoutmanager = new LinearLayoutManager(AddActivity.this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);

        allList();

//        basketList();

    }//onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_teamadd, menu);
        MenuItem additem=menu.findItem(R.id.action_add);
        additem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MakeTeam();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void MakeTeam() {
        members_str = new String[members.size()];
        members_str = members.toArray(members_str);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ValueUp_people");
        query.whereContainedIn("name", Arrays.asList(members_str));
        final ParseObject parseObject = new ParseObject("ValueUp_team");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getString("job").equals("plan"))
                        plan.add(list.get(i).getString("name"));
                    else if (list.get(i).getString("job").equals("dev"))
                        dev.add(list.get(i).getString("name"));
                    else if (list.get(i).getString("job").equals("dis"))
                        des.add(list.get(i).getString("name"));
                    if (list.get(i).getString("name").equals(ParseUser.getCurrentUser().getString("name"))) {
                        parseObject.put("idea", list.get(i).getString("info"));
                        parseObject.put("info", list.get(i).getString("detail"));
                    }
                    String[] plan_str = new String[plan.size()];
                    String[] dev_str = new String[dev.size()];
                    String[] des_str = new String[des.size()];
                    plan_str = plan.toArray(plan_str);
                    dev_str = dev.toArray(dev_str);
                    des_str = des.toArray(des_str);
                    parseObject.put("state", "팀빌딩 중");
                    parseObject.put("name", Arrays.asList(members_str));
                    parseObject.put("dev", Arrays.asList(dev_str));
                    parseObject.put("plan", Arrays.asList(plan_str));
                    parseObject.put("dis", Arrays.asList(des_str));
                    parseObject.put("constructor", ParseUser.getCurrentUser().getString("name"));

                    parseObject.saveInBackground();
                }

            }
        });

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void addMembers(String m){
        members.add(m);
    }
    public  void delMembers(String m){
        members.remove(m);
    }

    public void basketList() {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("ValueUp_people");
        query.whereEqualTo("pick", ParseUser.getCurrentUser().getString("name"));       //pick array에 현재 유저네임 있으면 그사람 출력.
        final List<Add_item> items=new ArrayList<>();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getString("job").equals("plan"))
                        cur_job = 0;
                    else if (list.get(i).getString("job").equals("dev"))
                        cur_job = 1;
                    else
                        cur_job = 2;
                    Add_item item = new Add_item(list.get(i).getString("name"), R.drawable.splash_logo, false);
                    items.add(item);
                }
                add_recyclerView.setAdapter(new Add_RecyclerAdapter(AddActivity.this, items, R.layout.add_recycler));
            }
        });

        add_recyclerView.setLayoutManager(layoutmanager);
        add_recyclerView.setItemAnimator(new DefaultItemAnimator());

    }//bastetList

    public void allList() {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("ValueUp_people");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        ParseObject ob = list.get(i);
                        Add_item item;
                        if(list.get(i).getString("name").equals(ParseUser.getCurrentUser().getString("name")))
                            item = new Add_item(list.get(i).getString("name"), R.drawable.splash_logo, true);
                        else
                            item = new Add_item(ob.getString("name"), R.drawable.splash_logo, false);
                        items.add(item);
                    }
                    add_recyclerView.setAdapter(new Add_RecyclerAdapter(AddActivity.this, items, R.layout.add_recycler));
                }//end if
            }
        });//end query

        add_recyclerView.setLayoutManager(layoutmanager);
        add_recyclerView.setItemAnimator(new DefaultItemAnimator());

    }//allList

}//class
