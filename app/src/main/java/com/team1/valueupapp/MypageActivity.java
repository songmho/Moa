package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * Created by eugene on 2015-08-08.
 */
public class MypageActivity extends AppCompatActivity {
    ParseUser parseUser;
    String str_job;
//    String str_info;

    CollapsingToolbarLayout collapsing_toolbar;
    TextView myjob;
    TextView title;
    TextView myinfo;
    TextView mydetail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsing_toolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        myjob=(TextView)findViewById(R.id.myjob);
        title=(TextView)findViewById(R.id.info);
        myinfo=(TextView)findViewById(R.id.myinfo);
        mydetail=(TextView)findViewById(R.id.mydetail);


    }

    @Override
    protected void onResume() {
        super.onResume();
        parseUser=ParseUser.getCurrentUser();
        collapsing_toolbar.setTitle(parseUser.getString("name"));

        switch (parseUser.getString("job")){
            case "plan":
                str_job="기획자";
//                str_info="아이디어";
                myjob.setText("기획자");
//                title.setText("아이디어");
                break;
            case "dev":
                str_job="개발자";
//                str_info="스킬";
                myjob.setText("개발자");
//                title.setText("스킬");
                break;
            case "dis":
                str_job="디자이너";
//                str_info="스킬";
                myjob.setText("디자이너");
//                title.setText("스킬");
                break;
        }

//        myinfo.setText(parseUser.getString("info"));
        mydetail.setText(parseUser.getString("detail"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        MenuItem editItem = menu.findItem(R.id.action_edit);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            Intent intent=new Intent(MypageActivity.this,Mypage_edit_Activity.class);
            intent.putExtra("name",parseUser.getString("name"));
//            intent.putExtra("info",str_info);
            intent.putExtra("job",str_job);
//            intent.putExtra("myinfo",parseUser.getString("info"));
            intent.putExtra("mydetail",parseUser.getString("detail"));
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}