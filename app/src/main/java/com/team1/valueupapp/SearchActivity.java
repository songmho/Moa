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
    List<ListRecyclerItem> items;
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

        int page = 0;

        if(intent.getStringExtra("page").equals("main"))
            page=0;
        else if(intent.getStringExtra("page").equals("interest"))
            page=1;
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

        makeList(intent.getStringExtra("query"), page);

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
