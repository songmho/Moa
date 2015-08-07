package com.team1.valueupapp;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * Created by eugene on 2015-08-08.
 */
public class MypageActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acvtivity_mypage);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsing_toolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        TextView myjob=(TextView)findViewById(R.id.myjob);
        TextView title=(TextView)findViewById(R.id.info);
        TextView myinfo=(TextView)findViewById(R.id.myinfo);
        TextView mydetail=(TextView)findViewById(R.id.mydetail);

        ParseUser parseUser=ParseUser.getCurrentUser();
        collapsing_toolbar.setTitle(parseUser.getString("name"));

        switch (parseUser.getString("job")){
            case "plan":
                myjob.setText("기획자");
                title.setText("아이디어");
                break;
            case "dev":
                myjob.setText("개발자");
                title.setText("스킬");
                break;
            case "dis":
                myjob.setText("디자이너");
                title.setText("스킬");
                break;
        }

        myinfo.setText(parseUser.getString("info"));
        mydetail.setText(parseUser.getString("detail"));

    }

}