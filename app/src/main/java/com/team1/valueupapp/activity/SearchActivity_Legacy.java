package com.team1.valueupapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.team1.valueupapp.item.ListRecyclerItem;
import com.team1.valueupapp.adapter.MentorAdapter;
import com.team1.valueupapp.item.MentorItem;
import com.team1.valueupapp.adapter.MentoringAdapter;
import com.team1.valueupapp.item.MentoringItem;
import com.team1.valueupapp.R;
import com.team1.valueupapp.adapter.RecyclerAdpater;
import com.team1.valueupapp.adapter.TeamRecyclerAdapter;
import com.team1.valueupapp.item.TeamItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songmho on 15. 8. 19.
 */
public class SearchActivity_Legacy extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    int cur_job;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("검색결과");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

     /*   searchFragment.setArguments(bundle);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, searchFragment);
        fragmentTransaction.commit();*/

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        //search=bundle.getString("search");
        Log.d("search", intent.getStringExtra("query"));
        String page_str = "";
        int page = 0;

        switch(intent.getStringExtra("page")) {
            case "main":
                page = 0;
                makeList(intent.getStringExtra("query"), page);
                break;
            case "interest":
                page = 1;
                makeList(intent.getStringExtra("query"), page);
                break;
            case "mentor":
                page_str = "ValueUp_mentor";
                makeMentorList(intent.getStringExtra("query"), page_str);
                break;
            case "mentoring":
                page_str = "ValueUp_mentoring";
                makeMentorList(intent.getStringExtra("query"), page_str);
                break;
            case "team":
                page_str = "ValueUp_team";
                makeTeamList(intent.getStringExtra("query"));
                break;
        }
    }

    public void makeTeamList(final String query) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                final ArrayList<TeamItem> items = new ArrayList<>();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final List<String> list_pick = ParseUser.getCurrentUser().getList("pick");
                        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("ValueUp_team");
                        parseQuery.whereContains("admin_member", query);
                        parseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                Log.d("dfdfdfdf", "" + list.size());
                                for (ParseObject o : list) {
                                    String same_mem = "";
                                    List<String> list_member = o.getList("member");
                                    for (String s : list_member) {
                                        if (list_pick.contains(s))
                                            same_mem = same_mem + " " + s;
                                    }
                                /*    TeamItem item = new TeamItem(o.getString("idea"), o.getString("admin_member"), o.getString("idea_info"), same_mem, o.getList("member").size());
                                    items.add(item);
                                */}
                                recyclerView.setAdapter(new TeamRecyclerAdapter(getApplicationContext(), items, R.layout.activity_team));

                            }
                        });

                        progressBar.setVisibility(View.GONE);

                    }
                });
            }
        }).start();
    }

    private void makeMentorList(final String query, final String page_str) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(page_str);
                        if (page_str.equals("ValueUp_mentor"))
                            parseQuery.whereContains("mentor_name", query);
                        else if (page_str.equals("ValueUp_mentoring"))
                            parseQuery.whereContains("mentor", query);
                        parseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                if (page_str.equals("ValueUp_mentor")) {
                                    ArrayList<MentorItem> items = new ArrayList<>();
                                    for (int i = 0; i < list.size(); i++) {
                                        MentorItem mentor_item = new MentorItem(list.get(i).getString("mentor_name"), list.get(i).getString("mentor_field"),
                                                list.get(i).getString("company"), list.get(i).getString("email"));
                                        items.add(mentor_item);
                                    }
                                    recyclerView.setAdapter(new MentorAdapter(getApplicationContext(), items, R.layout.activity_mentor));
                                    progressBar.setVisibility(View.GONE);
                                } else if (page_str.equals("ValueUp_mentoring")) {
                                    ArrayList<MentoringItem> items = new ArrayList<>();
                                    for (ParseObject o : list) {
                                        MentoringItem item = new MentoringItem(o.getString("job"),
                                                o.getInt("year"), o.getInt("month"), o.getInt("day"),
                                                o.getString("title"), o.getString("mentor"), o.getString("venue"), o.getString("detail"));
                                        items.add(item);
                                    }
                                    recyclerView.setAdapter(new MentoringAdapter(getApplicationContext(), items, R.layout.activity_mentoring));
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }


    public void makeList(final String search, final int frag) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereContains("name", search);
                        if (frag == 1)
                            query.whereContainedIn("name", ParseUser.getCurrentUser().getList("pick"));
                        query.addAscendingOrder("name");
                        final List<ListRecyclerItem> items = new ArrayList<>();
                        query.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> list, ParseException e) {
                                Log.d("dddd", "" + list.size());
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getString("job").equals("plan"))
                                        cur_job = 0;
                                    else if (list.get(i).getString("job").equals("dev"))
                                        cur_job = 1;
                                    else
                                        cur_job = 2;

                                    ParseFile parse_file = (ParseFile) list.get(i).get("profile");
                                    try {
                                        byte[] bytes;
                                        if (parse_file != null)
                                            bytes = parse_file.getData();
                                        else
                                            bytes = null;
                                        ListRecyclerItem item = new ListRecyclerItem(bytes, list.get(i).getString("info"),
                                                list.get(i).getString("name"), cur_job, recyclerView);
                                        items.add(item);
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                recyclerView.setAdapter(new RecyclerAdpater(getApplicationContext(), items, R.layout.item_listrecycler, 0));
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        }).start();

    }
}
