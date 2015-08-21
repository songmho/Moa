package com.team1.valueupapp;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songmho on 15. 8. 19.
 */
public class SearchActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    int cur_job;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("검색결과");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        String page_str=new String();
        int page = 0;

        if(intent.getStringExtra("page").equals("main"))
            page=0;
        else if(intent.getStringExtra("page").equals("interest"))
            page=1;

        if(intent.getStringExtra("page").equals("mentor"))
            page_str="ValueUp_mentor";
        else if(intent.getStringExtra("page").equals("mentoring"))
            page_str="ValueUp_mentoring";
        else if(intent.getStringExtra("page").equals("team"))
            page_str="ValueUp_team";
     /*   searchFragment.setArguments(bundle);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, searchFragment);
        fragmentTransaction.commit();*/

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);

        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        //search=bundle.getString("search");
        Log.d("search", intent.getStringExtra("query"));
        switch (intent.getStringExtra("page")){
            case "main":
            case "interest":
                makeList(intent.getStringExtra("query"), page);
            break;
            case "mentor":
            case "mentoring":
                makementorList(intent.getStringExtra("query"),page_str);
                break;
            case "team":
                makeTeamList(intent.getStringExtra("query"));
                break;
        }

    }

    private void makeTeamList(final String query) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                final ArrayList<Team_item> items=new ArrayList<>();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final List<String> list_pick= ParseUser.getCurrentUser().getList("pick");
                        ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery("ValueUp_team");
                        parseQuery.whereContains("admin_member",query);
                        parseQuery.whereEqualTo("ismade",true);
                        parseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                Log.d("dfdfdfdf",""+list.size());
                                for (ParseObject o : list) {
                                    String same_mem="";
                                    List<String> list_member=o.getList("member");
                                    for(String s:list_member) {
                                        if(list_pick.contains(s))
                                            same_mem=same_mem+" "+s;
                                    }
                                    Team_item item = new Team_item(o.getString("idea"), o.getString("admin_member"), o.getString("idea_info"), same_mem, o.getList("member").size());
                                    items.add(item);
                                }
                                recyclerView.setAdapter(new Team_RecyclerAdapter(getApplicationContext(), items, R.layout.activity_team));

                            }
                        });

                        progressBar.setVisibility(View.GONE);

                    }
                });
            }
        }).start();
    }

    private void makementorList(final String query, final String page_str) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery(page_str);
                        if(page_str.equals("ValueUp_mentor"))
                            parseQuery.whereContains("mentor_name",query);
                        else if(page_str.equals("ValueUp_mentoring"))
                            parseQuery.whereContains("mentor",query);
                        parseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                if(page_str.equals("ValueUp_mentor")){
                                    ArrayList<Mentor_item> items=new ArrayList<>();
                                    for (int i = 0; i < list.size(); i++) {
                                        Mentor_item mentor_item = new Mentor_item(list.get(i).getString("mentor_name"), list.get(i).getString("mentor_field"),
                                                list.get(i).getString("company"), list.get(i).getString("email"));
                                        items.add(mentor_item);
                                    }
                                    recyclerView.setAdapter(new Mentor_Adapter(getApplicationContext(), items, R.layout.activity_mentor));
                                    progressBar.setVisibility(View.GONE);
                                }

                                else if(page_str.equals("ValueUp_mentoring")){
                                    ArrayList<Mentoring_item> items=new ArrayList<>();
                                    for (ParseObject o : list) {
                                        Mentoring_item item = new Mentoring_item(o.getString("job"),
                                                o.getInt("year"), o.getInt("month"), o.getInt("day"),
                                                o.getString("title"), o.getString("mentor"), o.getString("venue"),o.getString("detail"));
                                        items.add(item);
                                    }
                                    recyclerView.setAdapter(new Mentoring_adapter(getApplicationContext(), items, R.layout.activity_mentoring));
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
                                                list.get(i).getString("name"), true, cur_job, recyclerView);
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
