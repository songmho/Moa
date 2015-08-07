package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by eugene on 2015-08-08.
 */
public class MypageActivity extends AppCompatActivity {
    TextView mydetail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acvtivity_mypage);

        final Intent intent=getIntent();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsing_toolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        TextView myjob=(TextView)findViewById(R.id.myjob);
        TextView title=(TextView)findViewById(R.id.info);
        TextView myinfo=(TextView)findViewById(R.id.myinfo);
        mydetail=(TextView)findViewById(R.id.mydetail);

        collapsing_toolbar.setTitle(intent.getStringExtra("name"));

        switch (intent.getIntExtra("cur_job",0)){
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

        loadingData(intent, 0);
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
                    else{}
                }
            }
        });
    }

}
