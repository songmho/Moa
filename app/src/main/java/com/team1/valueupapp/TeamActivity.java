package com.team1.valueupapp;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyemi on 2015-08-16.
 */
public class TeamActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Team_item> items;
    SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("팀빌딩하기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.refresh);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        items=new ArrayList<>();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                makeList();
                refreshLayout.setRefreshing(false);
            }
        });

        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ParseUser.getCurrentUser()!=null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("ValueUp_team");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            boolean exist = false;
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getString("admin_member").equals(ParseUser.getCurrentUser().getString("name"))) {
                                    ParseObject parseObject = list.get(i);
                                    if (parseObject.getBoolean("ismade") == true) {
                                        exist = true;
                                    }
                                }//개설중인 방이 있는지 확인
                                else if(list.get(i).getList("member").contains(ParseUser.getCurrentUser().getString("name"))) {
                                    exist = true;
                                }//참여중인 방이 있는지 확인
                            }//end for

                            if (exist == true) {
                                Toast.makeText(getApplicationContext(), "이미 참여중인 방이 있습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(TeamActivity.this, TeamAddActivity.class);
                                startActivity(intent);
                            }//end else

                        }
                    });//query.findInBackground
                }
                else{
                    Toast.makeText(getApplicationContext(),"로그인이 필요합니다.",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TeamActivity.this,LoginActivity.class));
                }
            }
        });//fab.setOnClickListener
    }//onCreate

    @Override
    public void onResume() {
        super.onResume();
        refreshLayout.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeList();
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    public void makeList() {
        items.clear();

        final List<String> list_pick;
        if(ParseUser.getCurrentUser()!=null)
            list_pick = ParseUser.getCurrentUser().getList("pick");
        else
            list_pick=null;
        ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery("ValueUp_team");
        parseQuery.whereEqualTo("ismade", true);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject o : list) {
                    String same_mem = "";
                    List<String> list_member = o.getList("member");
                    for (String s : list_member) {
                        if (ParseUser.getCurrentUser() != null && list_pick.contains(s))
                            same_mem = same_mem + " " + s;
                        else
                            same_mem = "";
                    }
                    Team_item item = new Team_item(o.getString("idea"), o.getString("admin_member"), o.getString("idea_info"), same_mem, o.getList("member").size());
                    items.add(item);
                }
                recyclerView.setAdapter(new Team_RecyclerAdapter(getApplicationContext(), items, R.layout.activity_team));

            }
        });
    }//makeList



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) TeamActivity.this.getSystemService(SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.clearFocus();
            searchView.setQueryHint("팀장 이름 검색");
            searchView.setSearchableInfo(searchManager.getSearchableInfo(TeamActivity.this.getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Intent intent = new Intent(TeamActivity.this, SearchActivity.class);
                    intent.putExtra("query", query);
                    intent.putExtra("page", "team");
                    startActivity(intent);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}//class
