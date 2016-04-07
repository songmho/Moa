package com.team1.valueupapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.team1.valueupapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by songmho on 2015-07-21.
 */
public class TeamDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    @Bind(R.id.bt_join) Button btnJoin;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.admin_name) TextView txtAdminName;
    @Bind(R.id.detail) TextView txtDetail;
    @Bind(R.id.admin_profile) ImageView imgAdminProfile;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.tag) TextView tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        ButterKnife.bind(this);

        intent = getIntent();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle(intent.getStringExtra("title"));
        txtAdminName.setText(intent.getStringExtra("name"));
        txtDetail.setText(intent.getStringExtra("detail"));

        Glide.with(getApplicationContext()).load(R.drawable.ic_user).placeholder(R.drawable.ic_user).
                bitmapTransform(new CropCircleTransformation(getApplicationContext())).into(imgAdminProfile);     //팀장 얼굴 가져오는 것. 플레이스홀더 지정해둠.

        if (ParseUser.getCurrentUser() != null &&
                intent.getStringExtra("username").equals(ParseUser.getCurrentUser().getUsername())) {         //로그인이 되어 있고 현재 유저가 팀장일 때
            btnJoin.setVisibility(View.GONE);           //참여하기 버튼 보이지 않게 함.
        }

        changeBtnText();        //팀에 참여중일 시 버튼에 글 내용 바꾸는 메소드

        getTag(intent);     //태그 가져오는 메소드
        btnJoin.setOnClickListener(this);
    }

    private void changeBtnText() {        //팀에 참여중일 시 버튼에 글 내용 바꾸는 메소드
        if(ParseUser.getCurrentUser()!=null) {      //현재 로그인이 되어 있을 때만 실행
            ParseQuery<ParseObject> query = new ParseQuery<>("Team");     //ParseQuery
            query.whereEqualTo("intro", intent.getStringExtra("title")); //그룹 소개와 parse에 있는 intro를 매칭 시켜 같은거 찾음
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    ParseObject o = list.get(0);      //조건에 맞는 오브젝트 찾음
                    ParseRelation<ParseUser> input_user = o.getRelation("member_doing");        // 신청자 현황 릴레이션 불러옴
                    ParseQuery<ParseUser> user = input_user.getQuery();             //릴레이션을 가지고 쿼리문 돌림
                    user.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());        //현재 유저 id와 릴레이션 안에 있는 유저들의 id 비교
                    user.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> list, ParseException e) {
                            if (!list.isEmpty()) {            //member_doing에 존재할 때
                                btnJoin.setText("그룹에 참여 중입니다.");       //텍스트 변경
                                btnJoin.setClickable(false);        //터치 불가능하게 변경
                            }       //end if
                        }       //end done method
                    });
                }       //end done method
            });
        }       //end if
    }       //end method

    private void getTag(Intent intent) {                    //태그가져오는 메소드
        ParseQuery<ParseObject> query = new ParseQuery<>("Team");
        query.whereEqualTo("intro", intent.getStringExtra("title")); //그룹 소개와 parse에 있는 intro를 매칭 시켜 같은거 찾음
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (!list.isEmpty() && list.get(0).getJSONArray("tag") != null) {      //리스트가 비어 있지 않고, 태그가 존재할 때
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
                } else        //리스트가 비어있거나(리스트가 비어있을 경우에 대한 예외처리 필요) 태그가 등록 되어 있지 않으면
                    tag.setText("");
            }
        });
    }       //end Method

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_join:                          //팀참여하기 버튼을 클릭했을 때
                if (ParseUser.getCurrentUser() == null) {           //로그인을 하지 않은 경우
                    Toast.makeText(TeamDetailActivity.this, "팀에 들어가시려면 로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TeamDetailActivity.this, LoginActivity.class));
                } else {      //로그인이 되어 있을 경우 (팀에 참여되어있을 경우와 팀에 참여되지 않은 경우 나눠야 됨)
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("참여 신청");          //AlertDialog Title
                    builder.setMessage("참여 신청하시겠습니까?");     //AlertDialog Message
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {        //Back key입력 시
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    });
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {     //확인버튼 클릭 시
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ParseQuery<ParseObject> query=new ParseQuery<>("Team");     //ParseQuery
                            query.whereEqualTo("intro", intent.getStringExtra("title")); //그룹 소개와 parse에 있는 intro를 매칭 시켜 같은거 찾음
                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> list, ParseException e) {
                                    if(!list.isEmpty()){        //리스트가 비어있지 않을 때
                                        ParseObject o=list.get(0);      //조건에 맞는 오브젝트 찾음
                                        ParseRelation<ParseUser> input_user = o.getRelation("member_doing");        // 신청자 현황 릴레이션 불러옴
                                        input_user.add(ParseUser.getCurrentUser());     //현재 로그인된 유저를 릴레이션에 추가
                                        o.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if(e==null)     //저장이 오류 없이 되었을 경우
                                                    Toast.makeText(TeamDetailActivity.this, "신청완료되었습니다.", Toast.LENGTH_SHORT).show();
                                                else        //저장이 제대로 되지 않은 경우
                                                    Toast.makeText(TeamDetailActivity.this, "오류발생!", Toast.LENGTH_SHORT).show();
                                            }   //end done method
                                        });     //end method
                                    }   //end if
                                }   //end done method
                            }); //end Query
                        }   //endOnClick
                    }); //endSetter

                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {     //취소버튼 클릭 시
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }      //AlertDialog 보여 주기
                break;
        }
    }
}
