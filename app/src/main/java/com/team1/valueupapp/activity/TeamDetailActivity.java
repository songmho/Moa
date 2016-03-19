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

import com.bumptech.glide.Glide;
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
import jp.wasabeef.glide.transformations.CropCircleTransformation;

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
    @Bind(R.id.admin_name) TextView admin_name;
    @Bind(R.id.detail) TextView detail;
    @Bind(R.id.admin_profile) ImageView admin_profile;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsing_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        ButterKnife.bind(this);

        Intent intent=getIntent();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsing_toolbar.setTitle(intent.getStringExtra("title"));
        admin_name.setText(intent.getStringExtra("name"));
        detail.setText(intent.getStringExtra("detail"));
        Glide.with(getApplicationContext()).load(R.drawable.ic_user).placeholder(R.drawable.ic_user).
                bitmapTransform(new CropCircleTransformation(getApplicationContext())).into(admin_profile);

        if(intent.getStringExtra("username").equals(ParseUser.getCurrentUser().getUsername())){         //현재 유저가 팀장일 때
            bt_join.setVisibility(View.GONE);           //참여하기 버튼 보이지 않게 함.
        }

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
