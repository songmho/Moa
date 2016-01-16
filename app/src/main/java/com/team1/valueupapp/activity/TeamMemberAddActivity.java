package com.team1.valueupapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.team1.valueupapp.R;
import com.team1.valueupapp.adapter.TeamMemberAddAdapter;
import com.team1.valueupapp.item.TeamMemberAddItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songmho on 15. 8. 20.
 */
public class TeamMemberAddActivity extends AppCompatActivity {
    ArrayList<TeamMemberAddItem> items;
    RecyclerView recyclerview;
    TeamMemberAddAdapter adapter;
    ProgressBar progressBar;
    List<String> completed;

    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_member_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("팀원추가");
        setSupportActionBar(toolbar);

        final EditText search_edit = (EditText) findViewById(R.id.search_edit);
        Button search = (Button) findViewById(R.id.search);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        Button cancel = (Button) findViewById(R.id.cancel);

        completed = new ArrayList<>();
        items = new ArrayList<>();
        makeList("");

        recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
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

    private void makeList(final String search) {
        progressBar.setVisibility(View.VISIBLE);

        items.clear();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("job", "mentor");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(final List<ParseUser> list, ParseException e) {
                for (int i = 0; i < list.size(); i++) {
                    final ParseUser user = list.get(i);
                    if (user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                        TeamMemberAddItem item = new TeamMemberAddItem(null, user.getString("name"), true, user.getObjectId());
                        items.add(item);
                    } else {
                        TeamMemberAddItem item = new TeamMemberAddItem(null, user.getString("name"), false, user.getObjectId());
                        items.add(item);
                    }

                }//end for
                progressBar.setVisibility(View.GONE);
                recyclerview.setAdapter(new TeamMemberAddAdapter(getApplicationContext(), items));

            }
        });
    }
}
