package com.team1.valueupapp.activity;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.team1.valueupapp.R;
import com.team1.valueupapp.adapter.TeamRecyclerAdapter;
import com.team1.valueupapp.item.TeamItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyemi on 2015-08-16.
 */
public class TeamActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<TeamItem> items;
    SwipeRefreshLayout refreshLayout;
    List<ParseUser> list_pick;
    String same_mem = "";
    int member_num;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("그룹 만들기");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        items = new ArrayList<>();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                makeList();
                refreshLayout.setRefreshing(false);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if (ParseUser.getCurrentUser() != null) {
                    Intent intent = new Intent(TeamActivity.this, TeamAddActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TeamActivity.this, LoginActivity.class));
                }*/
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

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Team");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject o : list) {
                    ParseRelation<ParseUser> member_relatrion = o.getRelation("member");
                    ParseUser user = o.getParseUser("admin_member");
                    try {
                        user.fetchIfNeeded();
                        TeamItem item = new TeamItem(o.getString("idea"), user.getString("name"), user.getUsername() , o.getString("idea_info"));
                        items.add(item);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
                recyclerView.setAdapter(new TeamRecyclerAdapter(getApplicationContext(), items, R.layout.activity_team));

            }
        });
    }//getListData


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
                    Intent intent = new Intent(TeamActivity.this, SearchActivity_Legacy.class);
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
