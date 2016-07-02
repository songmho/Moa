package com.team1.valueupapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.team1.valueupapp.R;
import com.team1.valueupapp.adapter.MemberWaitingThumbnailAdapter;
import com.team1.valueupapp.adapter.MemberThumbnailAdapter;
import com.team1.valueupapp.item.UserItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by songmho on 2015-07-21.
 */
public class TeamDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    @Bind(R.id.layout_all) LinearLayout layoutAll;   //전체 레이아웃
    @Bind(R.id.bt_join) Button btnJoin;     //팀 참여하기 버튼
    @Bind(R.id.layout_waiting) RelativeLayout layoutWaiting;    //참여 신청 중 버튼 레이아웃
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.layout_admin) LinearLayout layoutAdmin;
    @Bind(R.id.admin_name) TextView txtAdminName;
    @Bind(R.id.detail) TextView txtDetail;
    @Bind(R.id.admin_profile) ImageView imgAdminProfile;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.tag) TextView tag;
    @Bind(R.id.progressbar) ProgressBar progressBar;
    @Bind(R.id.btn_cancel) Button btnCancel;    //팀 참여 신청 취소
    @Bind(R.id.member_num) TextView txtNumMember;  //팀원 수
    @Bind(R.id.header_member_applied) LinearLayout headerMemberApplied;  //참여 신청자 헤더
    @Bind(R.id.member_applied_num) TextView txtNumMemberWaiting;    //신청자 수
    @Bind(R.id.list_member) RecyclerView listMember;    //팀원 리스트
    @Bind(R.id.list_member_applied) RecyclerView listMemberWaiting;     //신청자 리스트

    ArrayList<UserItem> memberArrayList = new ArrayList<>();
    ArrayList<UserItem> memberWaitingArrayList = new ArrayList<>();

    MemberThumbnailAdapter memberThumbnailAdapter;
    MemberWaitingThumbnailAdapter memberWaitingThumbnailAdapter;

    private static final int TYPE_OWNER = 0;
    private static final int TYPE_MEMBER = 1;
    private static final int TYPE_MEMBER_WAITING = 2;
    private static final int TYPE_NONE = 3;

    private static final int RESULT_LOGIN = 11;

    ParseUser currentUser;
    ParseQuery<ParseObject> teamQuery;
    boolean isMyOwnTeam = false;    //내가 팀장인지 여부 저장

    String teamName, adminName, adminUsername;
    Context mContext;
    String strTag = "";

    private static final String TAG = "TeamDetailActivity";
    public static final int RESULT_EDIT = 52;
    public static final int RESULT_EDIT_FINISH = 53;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_team_detail);
        ButterKnife.bind(this);

        intent = getIntent();
        teamQuery = new ParseQuery<>("Team");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle(intent.getStringExtra("title"));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listMember.setHasFixedSize(true);
        listMember.setLayoutManager(layoutManager);
        listMemberWaiting.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        listMemberWaiting.setLayoutManager(layoutManager2);

        adminName = intent.getStringExtra("name");
        adminUsername = intent.getStringExtra("username");
        txtAdminName.setText(adminName);
        txtDetail.setText(intent.getStringExtra("detail"));

        imgAdminProfile.setImageResource(R.drawable.ic_user); //todo 팀장 얼굴 가져오는 것으로 변경이 필요.

        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null && adminUsername.equals(currentUser.getUsername())) {         //로그인이 되어 있고 현재 유저가 팀장일 때
            isMyOwnTeam = true;     //내가 팀장
        }

        teamName = intent.getStringExtra("title");
        initDataAndView();
        btnJoin.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        layoutAdmin.setOnClickListener(this);
    }

    //데이터 및 뷰 초기화
    public void initDataAndView() {
        progressBar.setVisibility(View.VISIBLE);
        teamQuery.whereEqualTo("intro", intent.getStringExtra("title")); //그룹 소개와 parse에 있는 intro를 매칭 시켜 같은거 찾음
        teamQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (!list.isEmpty()) {
                    final ParseObject teamObject = list.get(0);
                    ParseRelation<ParseUser> member = teamObject.getRelation("member");        // 멤버 현황 릴레이션 불러옴
                    final ParseQuery<ParseUser> user = member.getQuery();             //릴레이션을 가지고 쿼리문 돌림


                    if (currentUser == null) {   //1. 로그인 하지 않은 상태인 경우
                        user.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> list, ParseException e) {
                                memberArrayList.clear();
                                if (list != null && list.size() > 1) {
                                    txtNumMember.setText(list.size() - 1 + "명");
                                    for (int i = 0; i < list.size(); i++) {
                                        Log.e(TAG, i + "번째 멤버 : " + list.get(i).getUsername());
                                        ParseUser user = list.get(i);
                                        if (user.getUsername().equals(adminUsername))
                                            continue;     //팀장 제외
                                        UserItem userItem = new UserItem(user.getUsername(), user.get("name").toString(), user.get("info").toString());
                                        memberArrayList.add(userItem);
                                    }
                                    if (memberThumbnailAdapter == null)
                                        memberThumbnailAdapter = new MemberThumbnailAdapter(mContext, memberArrayList);
                                    listMember.setAdapter(memberThumbnailAdapter);
                                    memberThumbnailAdapter.notifyDataSetChanged();
                                    listMember.setVisibility(View.VISIBLE);
                                } else {
                                    if (memberThumbnailAdapter == null)
                                        memberThumbnailAdapter = new MemberThumbnailAdapter(mContext, memberArrayList);
                                    listMember.setAdapter(memberThumbnailAdapter);
                                    memberThumbnailAdapter.notifyDataSetChanged();
                                    txtNumMember.setText("0명");
                                }
                                setBottomButtonLayout(TYPE_NONE);   //하단 버튼 설정
                            }
                        });
                    } else if (isMyOwnTeam) {      //2. 내가 만든 팀일 경우
                        setMemberListForOwner(user);        //팀원 리스트 설정

                        //대기중인 팀원 리스트 설정
                        ParseRelation<ParseUser> memberWaiting = teamObject.getRelation("member_doing");
                        ParseQuery<ParseUser> userParseQuery = memberWaiting.getQuery();
                        userParseQuery.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> list, ParseException e) {
                                memberWaitingArrayList.clear();
                                if (list != null && list.size() > 0) {
                                    txtNumMemberWaiting.setText(list.size() + "명");
                                    for (int i = 0; i < list.size(); i++) {
                                        Log.e(TAG, i + "번째 대기 멤버 : " + list.get(i).getUsername());
                                        ParseUser user = list.get(i);
                                        UserItem userItem = new UserItem(user.getUsername(), user.get("name").toString(), user.get("info").toString());
                                        memberWaitingArrayList.add(userItem);
                                    }
                                    if (memberWaitingThumbnailAdapter == null) {
                                        memberWaitingThumbnailAdapter = new MemberWaitingThumbnailAdapter(mContext, memberWaitingArrayList);
                                    }
                                    listMemberWaiting.setAdapter(memberWaitingThumbnailAdapter);
                                    memberWaitingThumbnailAdapter.notifyDataSetChanged();
                                    listMemberWaiting.setVisibility(View.VISIBLE);
                                } else {
                                    if (memberWaitingThumbnailAdapter == null) {
                                        memberWaitingThumbnailAdapter = new MemberWaitingThumbnailAdapter(mContext, memberWaitingArrayList);
                                    }
                                    listMemberWaiting.setAdapter(memberWaitingThumbnailAdapter);
                                    memberWaitingThumbnailAdapter.notifyDataSetChanged();
                                    listMemberWaiting.setVisibility(View.GONE);
                                    txtNumMemberWaiting.setText("0명");
                                }
                                headerMemberApplied.setVisibility(View.VISIBLE);
                                txtNumMemberWaiting.setVisibility(View.VISIBLE);

                                setBottomButtonLayout(TYPE_OWNER);
                            }
                        });
                    } else {        //3. 내가 만든 팀이 아닐 경우 버튼 설정한다.
                        user.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> list, ParseException e) {
                                //팀원 리스트 설정
                                memberArrayList.clear();
                                if (list != null && list.size() > 1) {
                                    txtNumMember.setText(list.size() - 1 + "명");
                                    for (int i = 0; i < list.size(); i++) {
                                        Log.e(TAG, i + "번째 멤버 : " + list.get(i).getUsername());
                                        ParseUser user = list.get(i);
                                        if (user.getUsername().equals(adminUsername)) {     //팀장은 팀원에서 제외해서 보여준다.
                                            continue;
                                        }
                                        UserItem userItem = new UserItem(user.getUsername(), user.get("name").toString(), user.get("info").toString());
                                        memberArrayList.add(userItem);
                                    }
                                    if (memberThumbnailAdapter == null)
                                        memberThumbnailAdapter = new MemberThumbnailAdapter(mContext, memberArrayList);
                                    listMember.setAdapter(memberThumbnailAdapter);
                                    memberThumbnailAdapter.notifyDataSetChanged();
                                    listMember.setVisibility(View.VISIBLE);
                                } else {
                                    if (memberThumbnailAdapter == null)
                                        memberThumbnailAdapter = new MemberThumbnailAdapter(mContext, memberArrayList);
                                    listMember.setAdapter(memberThumbnailAdapter);
                                    memberThumbnailAdapter.notifyDataSetChanged();
                                    txtNumMember.setText("0명");
                                }
                                user.whereEqualTo("objectId", currentUser.getObjectId());        //현재 유저 id와 릴레이션 안에 있는 유저들의 id 비교
                                user.findInBackground(new FindCallback<ParseUser>() {
                                    @Override
                                    public void done(List<ParseUser> list, ParseException e) {
                                        if (!list.isEmpty()) {            //member에 존재할 때
                                            setBottomButtonLayout(TYPE_MEMBER);
                                        } else {
                                            ParseRelation<ParseUser> appliedUser = teamObject.getRelation("member_doing");        // 신청자 현황 릴레이션 불러옴
                                            ParseQuery<ParseUser> memberDoing = appliedUser.getQuery();             //릴레이션을 가지고 쿼리문 돌림
                                            memberDoing.whereEqualTo("objectId", currentUser.getObjectId());        //현재 유저 id와 릴레이션 안에 있는 유저들의 id 비교
                                            memberDoing.findInBackground(new FindCallback<ParseUser>() {
                                                @Override
                                                public void done(List<ParseUser> list, ParseException e) {
                                                    if (!list.isEmpty()) {                  //1. member_doing에 존재할 때
                                                        setBottomButtonLayout(TYPE_MEMBER_WAITING);
                                                    } else {                                //2. member_doing에 존재하지 않을 때
                                                        setBottomButtonLayout(TYPE_NONE);
                                                    }
                                                }       //end done method
                                            });
                                        }
                                    }       //end done method
                                });
                            }
                        });
                    }
                    //태그 설정
                    if (teamObject.getJSONArray("tag") != null) {      //리스트가 비어 있지 않고, 태그가 존재할 때
                        JSONArray tagArray = teamObject.getJSONArray("tag");       //태그를 JsonArray형식으로 가져옴
                        for (int i = 0; i < tagArray.length(); i++) {
                            try {
                                if (!tagArray.getString(i).equals(""))
                                    strTag += "#" + tagArray.getString(i) + " ";
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                        tag.setText(strTag);
                    } else {        //리스트가 비어있거나(리스트가 비어있을 경우에 대한 예외처리 필요) 태그가 등록 되어 있지 않으면
                        tag.setText("");
                    }
                }
            }
        });
    }       //end Method

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //1. 팀 참여 신청하기 버튼
            case R.id.bt_join: {
                if (currentUser == null) {           //로그인을 하지 않은 경우
                    Toast.makeText(TeamDetailActivity.this, "팀에 들어가시려면 로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(mContext, LoginActivity.class);
                    loginIntent.putExtra("goBackPreviousPage", true);
                    startActivityForResult(loginIntent, RESULT_LOGIN);
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
                            ParseQuery<ParseObject> query = new ParseQuery<>("Team");     //ParseQuery
                            query.whereEqualTo("intro", intent.getStringExtra("title")); //그룹 소개와 parse에 있는 intro를 매칭 시켜 같은거 찾음
                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> list, ParseException e) {
                                    if (!list.isEmpty()) {        //리스트가 비어있지 않을 때
                                        ParseObject o = list.get(0);      //조건에 맞는 오브젝트 찾음
                                        ParseRelation<ParseUser> memberDoing = o.getRelation("member_doing");        // 신청자 현황 릴레이션 불러옴
                                        memberDoing.add(currentUser);     //현재 로그인된 유저를 릴레이션에 추가
                                        o.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {    //저장이 오류 없이 되었을 경우
                                                    Toast.makeText(TeamDetailActivity.this, "신청완료되었습니다.", Toast.LENGTH_SHORT).show();
                                                    btnJoin.setVisibility(View.GONE);
                                                    layoutWaiting.setVisibility(View.VISIBLE);
                                                } else        //저장이 제대로 되지 않은 경우
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

            //2. 팀 참여 신청 취소 버튼
            case R.id.btn_cancel:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("참여 신청 취소");          //AlertDialog Title
                builder.setMessage("참여 신청을 취소하시겠습니까?");     //AlertDialog Message
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {        //Back key입력 시
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {     //확인버튼 클릭 시
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParseQuery<ParseObject> query = new ParseQuery<>("Team");     //ParseQuery
                        query.whereEqualTo("intro", intent.getStringExtra("title")); //그룹 소개와 parse에 있는 intro를 매칭 시켜 같은거 찾음
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                if (!list.isEmpty()) {        //리스트가 비어있지 않을 때
                                    ParseObject o = list.get(0);      //조건에 맞는 오브젝트 찾음
                                    ParseRelation<ParseUser> memberDoing = o.getRelation("member_doing");        // 신청자 현황 릴레이션 불러옴
                                    memberDoing.remove(currentUser);
                                    o.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {    //저장이 오류 없이 되었을 경우
                                                Toast.makeText(TeamDetailActivity.this, "신청이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                                                layoutWaiting.setVisibility(View.GONE);
                                                btnJoin.setVisibility(View.VISIBLE);

                                            } else        //저장이 제대로 되지 않은 경우
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
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            //팀장 프로필 조회
            case R.id.layout_admin:
                Intent intent = new Intent(mContext, UserDetailActivity.class);
                intent.putExtra("name", adminName);
                intent.putExtra("username", adminUsername);
                startActivity(intent);
                break;
        }
    }

    //하단 버튼 레이아웃 설정
    public void setBottomButtonLayout(int type) {
        switch (type) {
            //1. 팀장
            case TYPE_OWNER:
                toolbar.inflateMenu(R.menu.menu_team_edit);
                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent i = new Intent(TeamDetailActivity.this, TeamEditActivity.class);
                        i.putExtra("objId",intent.getStringExtra("objId"));
                        i.putExtra("title", intent.getStringExtra("title"));
                        i.putExtra("detail", intent.getStringExtra("detail"));
                        i.putExtra("tag", strTag);
                        startActivityForResult(i, RESULT_EDIT);
                        return true;
                    }
                });
                break;
            //2. 멤버
            case TYPE_MEMBER:
                btnJoin.setClickable(false);        //터치 불가능하게 변경
                btnJoin.setText("그룹에 참여중입니다.");
                toolbar.inflateMenu(R.menu.menu_team_detail);
                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_out) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("그룹 나가기");          //AlertDialog Title
                            builder.setMessage("그룹에서 나가시겠습니까?");     //AlertDialog Message
                            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {        //Back key입력 시
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                }
                            });
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {     //확인버튼 클릭 시
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ParseQuery<ParseObject> query = new ParseQuery<>("Team");     //ParseQuery
                                    query.whereEqualTo("intro", intent.getStringExtra("title")); //그룹 소개와 parse에 있는 intro를 매칭 시켜 같은거 찾음
                                    query.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> list, ParseException e) {
                                            if (!list.isEmpty()) {        //리스트가 비어있지 않을 때
                                                ParseObject o = list.get(0);      //조건에 맞는 오브젝트 찾음
                                                ParseRelation<ParseUser> member = o.getRelation("member");        // 신청자 현황 릴레이션 불러옴
                                                member.remove(currentUser);     //현재 로그인된 유저를 멤버에서 제거
                                                o.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {    //저장이 오류 없이 되었을 경우
                                                            Toast.makeText(TeamDetailActivity.this, "그룹에서 탈퇴하였습니다.", Toast.LENGTH_SHORT).show();
                                                            initDataAndView();
                                                        } else        //저장이 제대로 되지 않은 경우
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
                            return true;
                        }
                        return false;
                    }
                });
                btnJoin.setVisibility(View.VISIBLE);
                break;
            //3. 신청 대기중 멤버
            case TYPE_MEMBER_WAITING:
                toolbar.getMenu().clear();
                btnJoin.setClickable(false);
                btnJoin.setText("참여 신청 중입니다.");
                layoutWaiting.setVisibility(View.VISIBLE);
                break;
            //4. 아무것도 아닌 제3자
            case TYPE_NONE:
                toolbar.getMenu().clear();
                btnJoin.setClickable(true);
                btnJoin.setText("그룹 참여하기");
                btnJoin.setVisibility(View.VISIBLE);
                break;
        }
        progressBar.setVisibility(View.GONE);
        layoutAll.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //참여완료된 팀원 리스트 설정
    public void setMemberListForOwner(ParseQuery<ParseUser> user) {
        user.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                memberArrayList.clear();
                if (list != null && list.size() > 1) {
                    txtNumMember.setText(list.size() - 1 + "명");
                    for (int i = 0; i < list.size(); i++) {
                        Log.e(TAG, i + "번째 멤버 : " + list.get(i).getUsername());
                        ParseUser user = list.get(i);
                        if (user.getUsername().equals(adminUsername)) {
                            continue;
                        }
                        UserItem userItem = new UserItem(user.getUsername(), user.get("name").toString(), user.get("info").toString());
                        memberArrayList.add(userItem);
                    }
                    if (memberThumbnailAdapter == null)
                        memberThumbnailAdapter = new MemberThumbnailAdapter(mContext, memberArrayList);
                    listMember.setAdapter(memberThumbnailAdapter);
                    memberThumbnailAdapter.notifyDataSetChanged();
                    listMember.setVisibility(View.VISIBLE);
                } else {
                    if (memberThumbnailAdapter == null)
                        memberThumbnailAdapter = new MemberThumbnailAdapter(mContext, memberArrayList);
                    listMember.setAdapter(memberThumbnailAdapter);
                    memberThumbnailAdapter.notifyDataSetChanged();
                    txtNumMember.setText("0명");
                }
            }
        });
    }

    //팀명 리턴
    public String getTeamName() {
        return teamName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //로그인 후
        if (requestCode == RESULT_LOGIN && resultCode == RESULT_OK) {
            initDataAndView();
            //팀장, 혹은 이미 참여한 팀, 차단(?) 당한 팀일 수도 있으므로 자동으로 버튼 다시 클릭하게는 하지 않았다.
        }
        else if(requestCode == RESULT_EDIT && resultCode == RESULT_EDIT_FINISH){
            Log.e("Dfafadsf","adsfadfads");
            if(data.getStringExtra("objId")==null)
                Log.e("Dfafadsf","빡침");
            collapsingToolbarLayout.setTitle(data.getStringExtra("title"));
            txtDetail.setText(data.getStringExtra("detail"));
            tag.setText(data.getStringExtra("tag"));
        }
    }
}
