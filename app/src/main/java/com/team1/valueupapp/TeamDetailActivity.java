package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by songmho on 2015-07-21.
 */
public class TeamDetailActivity extends AppCompatActivity {
    int[] cur_job;
    String[] info;
    ProgressBar progressBar;
    FrameLayout container_prog;
    ImageButton people_add;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        Intent intent=getIntent();

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsing_toolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsing_toolbar.setTitle(intent.getStringExtra("title"));

        TextView admin_name=(TextView)findViewById(R.id.admin_name);
        TextView detail=(TextView)findViewById(R.id.detail);
        CircleImageView admin_profile=(CircleImageView)findViewById(R.id.admin_profile);
        ImageView add=(ImageView)findViewById(R.id.add);

        if(ParseUser.getCurrentUser()!=null && ParseUser.getCurrentUser().getString("name").equals(intent.getStringExtra("name")))
            add.setVisibility(View.VISIBLE);
        else
            add.setVisibility(View.GONE);

        final TextView member_num=(TextView)findViewById(R.id.member_num);
        final LinearLayout[] member=new LinearLayout[6];
        CircleImageView [] member_profile=new CircleImageView[6];
        final TextView[] member_name=new TextView[6];
        member[0]=(LinearLayout)findViewById(R.id.member1);
        member[1]=(LinearLayout)findViewById(R.id.member2);
        member[2]=(LinearLayout)findViewById(R.id.member3);
        member[3]=(LinearLayout)findViewById(R.id.member4);
        member[4]=(LinearLayout)findViewById(R.id.member5);
        member[5]=(LinearLayout)findViewById(R.id.member6);
        member_profile[0]=(CircleImageView)findViewById(R.id.member_profile_1);
        member_profile[1]=(CircleImageView)findViewById(R.id.member_profile_2);
        member_profile[2]=(CircleImageView)findViewById(R.id.member_profile_3);
        member_profile[3]=(CircleImageView)findViewById(R.id.member_profile_4);
        member_profile[4]=(CircleImageView)findViewById(R.id.member_profile_5);
        member_profile[5]=(CircleImageView)findViewById(R.id.member_profile_6);
        member_name[0]=(TextView)findViewById(R.id.member_name_1);
        member_name[1]=(TextView)findViewById(R.id.member_name_2);
        member_name[2]=(TextView)findViewById(R.id.member_name_3);
        member_name[3]=(TextView)findViewById(R.id.member_name_4);
        member_name[4]=(TextView)findViewById(R.id.member_name_5);
        member_name[5]=(TextView)findViewById(R.id.member_name_6);

        admin_name.setText(intent.getStringExtra("name"));
        detail.setText(intent.getStringExtra("detail"));

        ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery("ValueUp_team");
        parseQuery.whereEqualTo("idea", intent.getStringExtra("title"));
        parseQuery.whereEqualTo("admin_member", intent.getStringExtra("name"));
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (!list.isEmpty()) {
                    List<String> mem_name = list.get(0).getList("member");
                    member_num.setText("" + mem_name.size());
                    for (int i = 0; i < mem_name.size(); i++) {
                        member[i].setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < mem_name.size(); i++) {
                        member_name[i].setText(mem_name.get(i));
                    }
                }
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeamDetailActivity.this,Team_Member_Add_Activity.class));
            }
        });
    }
}
