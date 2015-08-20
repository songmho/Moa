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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songmho on 15. 8. 20.
 */
public class Team_Member_Add_Activity extends AppCompatActivity {
    ArrayList<Team_Member_add_item> items;
    RecyclerView recyclerview;
    Team_Member_add_Adapter adapter;
    ProgressBar progressBar;

    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_member_add);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("팀원추가");

        final EditText search_edit=(EditText)findViewById(R.id.search_edit);
        Button search=(Button)findViewById(R.id.search);
        recyclerview=(RecyclerView)findViewById(R.id.recyclerview);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        Button cancel=(Button)findViewById(R.id.cancel);

        items=new ArrayList<>();
        makeList("");

        recyclerview.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(layoutManager);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeList("");
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = String.valueOf(search_edit.getText());
                items.clear();
                makeList(result);

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team_member_add, menu);
        MenuItem addItem = menu.findItem(R.id.action_add);
        addItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ArrayList<Team_Member_add_item>items=adapter.getPick_items();
                finish();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void makeList(final String i) {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ParseQuery<ParseUser> query=ParseUser.getQuery();
                        query.addAscendingOrder("name");
                        if(i.length()>0)
                            query.whereContains("name", i);
                        query.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> list, ParseException e) {
                                if (list.isEmpty() || list.size() == 0)
                                    Toast.makeText(getApplicationContext(), "목록이 없습니다.", Toast.LENGTH_SHORT).show();
                                for (ParseUser p : list) {
                                    Team_Member_add_item item = new Team_Member_add_item(null, p.getString("name"), false);
                                    items.add(item);
                                }
                                progressBar.setVisibility(View.GONE);
                                adapter=new Team_Member_add_Adapter(getApplicationContext(), items);
                                recyclerview.setAdapter(adapter);
                            }
                        });
                    }
                });
            }
        }).start();
    }
}