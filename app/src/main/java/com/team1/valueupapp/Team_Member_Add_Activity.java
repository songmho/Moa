package com.team1.valueupapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.parse.ParseObject;
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
    List<String> completed;

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

        completed = new ArrayList<String>();
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
                finish();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void makeList(final String i) {
        progressBar.setVisibility(View.VISIBLE);

        items.clear();

        final ArrayList<String> member=new ArrayList<>();
        ParseQuery<ParseObject> query1=ParseQuery.getQuery("ValueUp_team");
        query1.whereEqualTo("admin_member",ParseUser.getCurrentUser().getString("name"));
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(!list.isEmpty()) {
                    for (int i = 0; i < list.get(0).getList("member").size(); i++) {
                        member.add(String.valueOf(list.get(0).getList("member").get(i)));
                    }//end for
                }//end if
            }
        });

        ParseQuery<ParseObject> query2=ParseQuery.getQuery("ValueUp_team");
        query2.whereNotEqualTo("admin_member", ParseUser.getCurrentUser().getString("name"));
        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        for (int j = 0; j < list.get(i).getList("member").size(); j++) {
                            completed.add("" + list.get(i).getList("member").get(j));
                        }//end for
                    }
                }//end if
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.addAscendingOrder("name");
                if (i.length() > 0)
                    query.whereContains("name", i);
                query.whereNotContainedIn("name", completed);
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        Log.d("aa", "" + list.size());
                        if (list.isEmpty() || list.size() == 0)
                            Toast.makeText(getApplicationContext(), "목록이 없습니다.", Toast.LENGTH_SHORT).show();
                        for (ParseUser p : list) {
                            for (int i = 0; i <member.size(); i++) {
                                String s = member.get(i);
//                                Log.d("aaaSS", ""+s);
//                                Log.d("aaaPP", ""+p.getString("name"));

                                if (s.equals(p.getString("name"))) {
                                    Team_Member_add_item item = new Team_Member_add_item(null, p.getString("name"), true);
                                    items.add(item);
                                    break;
                                } else {
                                    Team_Member_add_item item = new Team_Member_add_item(null, p.getString("name"), false);
                                    items.add(item);
                                    break;
                                }
                            }
                            Log.d("aaa1P", ""+items.size());
                        }
                        if (items.size()>0) {
                            progressBar.setVisibility(View.GONE);
//                        Log.d("aaa2", ""+items.size());
                            recyclerview.setAdapter(new Team_Member_add_Adapter(getApplicationContext(), items));
                        } else {
                            makeList("");
                        }
                    }
                });
            }
        });

    }
}
