package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
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

        final Intent intent=getIntent();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(intent.getStringExtra("idea"));

        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        container_prog=(FrameLayout)findViewById(R.id.container_prog);
        container_prog.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        TextView idea=(TextView)findViewById(R.id.info);
        people_add=(ImageButton)findViewById(R.id.people_add);

        if (intent.getStringExtra("constructor").equals(ParseUser.getCurrentUser().getString("name")))
            people_add.setVisibility(View.VISIBLE);


        people_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"준비중입니다.",Toast.LENGTH_SHORT).show();
            }
        });

        idea.setText(intent.getStringExtra("info"));
        final String[] name_list=intent.getStringArrayExtra("list");
        LinearLayout[] v=new LinearLayout[6];
        final TextView[] j=new TextView[name_list.length];
        TextView[] n=new TextView[name_list.length];
        CircleImageView[] c=new CircleImageView[name_list.length];
        info=new String[name_list.length];
        cur_job=new int[name_list.length];

        v[0]=(LinearLayout)findViewById(R.id.people1);
        v[1]=(LinearLayout)findViewById(R.id.people2);
        v[2]=(LinearLayout)findViewById(R.id.people3);
        v[3]=(LinearLayout)findViewById(R.id.people4);
        v[4]=(LinearLayout)findViewById(R.id.people5);
        v[5]=(LinearLayout)findViewById(R.id.people6);

        for (int k=0;k<6;k++)
            v[k].setVisibility(View.GONE);

        for(int i=0;i<name_list.length;i++) {
            v[i].setVisibility(View.VISIBLE);
            n[i] = (TextView) v[i].findViewById(R.id.name);
            j[i] = (TextView) v[i].findViewById(R.id.job);
            c[i]=(CircleImageView)v[i].findViewById(R.id.profile);
            n[i].setText(name_list[i]);
            ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
            parseQuery.whereContains("name", name_list[i]);
            final int finalI = i;
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    info[finalI] = list.get(0).getString("info");
                    if (list.get(0).getString("job").equals("plan")) {
                        j[finalI].setText("기획자");
                        cur_job[finalI] = 0;
                    } else if (list.get(0).getString("job").equals("dev")) {
                        j[finalI].setText("개발자");
                        cur_job[finalI] = 1;
                    } else {
                        j[finalI].setText("디자이너");
                        cur_job[finalI] = 2;
                    }

                    if(finalI>=name_list.length-1) {
                        container_prog.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
        for(int i=0;i<name_list.length;i++) {
            final int finalI = i;
            v[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goto_info = new Intent(TeamDetailActivity.this, InfoActivity.class);
                    goto_info.putExtra("name", name_list[finalI]);
                    goto_info.putExtra("cur_job", cur_job[finalI]);
                    goto_info.putExtra("idea", info[finalI]);
                    startActivity(goto_info);
                }
            });
        }

    }
}
