package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by songmho on 2015-07-04.
 */
public class InfoActivity extends AppCompatActivity {
    FloatingActionButton fab;
    TextView mydetail;

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
        setContentView(R.layout.activity_info);
        final Intent intent=getIntent();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsing_toolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        TextView myjob=(TextView)findViewById(R.id.myjob);
        TextView title=(TextView)findViewById(R.id.info);
        TextView myinfo=(TextView)findViewById(R.id.myinfo);
        mydetail=(TextView)findViewById(R.id.mydetail);

        fab=(FloatingActionButton)findViewById(R.id.fab);
        collapsing_toolbar.setTitle(intent.getStringExtra("name"));

        switch (intent.getIntExtra("cur_job",0)){      //직종과 아이디어 및 스킬 텍스트 설정
            case 0:
                myjob.setText("기획자");
                title.setText("아이디어");
                break;
            case 1:
                myjob.setText("개발자");
                title.setText("스킬");
                break;
            case 2:
                myjob.setText("디자이너");
                title.setText("스킬");
                break;
        }
        String idea=intent.getStringExtra("idea");
            if(intent.getIntExtra("cur_job",0)!=0) {
                idea = intent.getStringExtra("idea").replaceAll(", ", "\n ");
                idea = " " + idea;
            }
        myinfo.setText(idea);

        loadingData(intent, 0);     //detail 불러오기

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingData(intent, 1);
            }
        });


    }

    private void loadingData(Intent intent, final int action) {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("ValueUp_people");
        query.whereEqualTo("name",intent.getStringExtra("name"));
        query.whereEqualTo("info",intent.getStringExtra("idea"));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (int i=0;i<list.size();i++) {
                    final ParseObject parseObject = list.get(i);
                    if(action==0)
                        mydetail.setText(parseObject.getString("detail"));
                    else
                        fab_clicked(parseObject);
                }
            }
        });
    }

    private void fab_clicked(final ParseObject parseObject) {
        View container=findViewById(R.id.container);

        if (parseObject.getList("pick").contains(ParseUser.getCurrentUser().get("name"))) {

            Snackbar snackbar=Snackbar.make(container,"관심멤버에서 제외합니다.",Snackbar.LENGTH_LONG);
            parseObject.getList("pick").remove(ParseUser.getCurrentUser().get("name"));
            parseObject.saveInBackground();
            fab.setImageResource(R.drawable.add);

            snackbar.setAction("실행취소", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parseObject.getList("pick").add(ParseUser.getCurrentUser().get("name"));
                    parseObject.saveInBackground();
                    fab.setImageResource(R.drawable.ic_check_white);
                }
            });
            snackbar.show();

        } else {
            Snackbar snackbar=Snackbar.make(container,"관심멤버에 추가합니다.",Snackbar.LENGTH_LONG);
            parseObject.getList("pick").add(ParseUser.getCurrentUser().get("name"));
            parseObject.saveInBackground();
            fab.setImageResource(R.drawable.ic_check_white);
            snackbar.setAction("실행취소", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parseObject.getList("pick").remove(ParseUser.getCurrentUser().get("name"));
                    parseObject.saveInBackground();
                    fab.setImageResource(R.drawable.add);
                }
            });
            snackbar.show();
        }
    }
}
