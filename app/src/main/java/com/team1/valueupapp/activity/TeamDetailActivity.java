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

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by songmho on 2015-07-21.
 */
public class TeamDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.bt_join) Button bt_join;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.admin_name) TextView admin_name;
    @Bind(R.id.detail) TextView detail;
    @Bind(R.id.admin_profile) ImageView admin_profile;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsing_toolbar;
    @Bind(R.id.tag) TextView tag;

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
                bitmapTransform(new CropCircleTransformation(getApplicationContext())).into(admin_profile);     //팀장 얼굴 가져오는 것. 플레이스홀더 지정해둠.

        if(ParseUser.getCurrentUser()!=null &&
                intent.getStringExtra("username").equals(ParseUser.getCurrentUser().getUsername())){         //로그인이 되어 있고 현재 유저가 팀장일 때
            bt_join.setVisibility(View.GONE);           //참여하기 버튼 보이지 않게 함.
        }

        getTag(intent);     //태그 가져오는 메소드
        bt_join.setOnClickListener(this);
    }

    private void getTag(Intent intent) {                    //태그가져오는 메소드
        ParseQuery<ParseObject> query = new ParseQuery<>("Team");
        query.whereEqualTo("intro",intent.getStringExtra("title")); //그룹 소개와 parse에 있는 intro를 매칭 시켜 같은거 찾음
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(!list.isEmpty() && list.get(0).getJSONArray("tag")!=null) {      //리스트가 비어 있지 않고, 태그가 존재할 때
                    String strTag = "";
                    JSONArray tagArray = list.get(0).getJSONArray("tag");       //태그를 JsonArray형식으로 가져옴
                    for (int i = 0; i < tagArray.length(); i++) {
                        try {
                            if (!tagArray.getString(i).equals(""))
                                strTag += "#" + tagArray.getString(i) + " ";
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    tag.setText(strTag);
                }
                else        //리스트가 비어있거나(리스트가 비어있을 경우에 대한 예외처리 필요) 태그가 등록 되어 있지 않으면
                    tag.setText("");
            }
        });
    }       //end Method

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_join:                          //팀참여하기 버튼을 클릭했을 때
                if(ParseUser.getCurrentUser()==null){           //로그인을 하지 않은 경우
                    Toast.makeText(TeamDetailActivity.this, "팀에 들어가시려면 로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TeamDetailActivity.this, LoginActivity.class));
                }
                else {      //로그인이 되어 있을 경우 (팀에 참여되어있을 경우와 팀에 참여되지 않은 경우 나눠야 됨)
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("참여 신청");          //AlertDialog Title
                    builder.setMessage("참여 신청하시겠습니까?");     //AlertDialog Message
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {        //Back key입력 시
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    });
                    builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {     //취소버튼 클릭 시
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {     //확인버튼 클릭 시
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();  }      //AlertDialog 보여 주기
                break;
        }
    }
}
