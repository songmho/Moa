package com.team1.valueupapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.team1.valueupapp.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by songmho on 2015-07-21.
 */
public class TeamDetailActivity extends AppCompatActivity implements View.OnClickListener {
    int[] cur_job;
    String[] info;
    ProgressBar progressBar;
    FrameLayout container_prog;
    ImageButton people_add;
    private MenuItem item;


    @Bind(R.id.bt_join) Button bt_join;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        ButterKnife.bind(this);

        Intent intent=getIntent();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsing_toolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsing_toolbar.setTitle(intent.getStringExtra("title"));

        TextView admin_name=(TextView)findViewById(R.id.admin_name);
        TextView detail=(TextView)findViewById(R.id.detail);
        CircleImageView admin_profile=(CircleImageView)findViewById(R.id.admin_profile);

        admin_name.setText(intent.getStringExtra("name"));
        detail.setText(intent.getStringExtra("detail"));

        bt_join.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_join:                          //팀참여하기 버튼을 클릭했을 때
                if(ParseUser.getCurrentUser()==null){           //로그인을 하지 않은 경우
                    Toast.makeText(TeamDetailActivity.this, "팀에 들어가시려면 로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TeamDetailActivity.this, LoginActivity.class));
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("참여 신청");
                    builder.setMessage("참여 신청하시겠습니까?");
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    });
                    builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();  }
                break;
        }
    }
}
