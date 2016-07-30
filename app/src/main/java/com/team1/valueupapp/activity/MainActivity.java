package com.team1.valueupapp.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.team1.valueupapp.R;
import com.team1.valueupapp.adapter.TeamRecyclerAdapter;
import com.team1.valueupapp.item.TeamItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    CircleImageView profileDrawer;
    Context mContext;
    FragmentTransaction fragmentTransaction;

    CharSequence[] item = {"카메라", "갤러리에서 사진 가져오기", "삭제"};
    ParseUser user = ParseUser.getCurrentUser();

    ArrayList<TeamItem> mainTeamItems, myTeamItems;

    public static final int RESULT_LOGIN = 11;
    public static final int RESULT_LOGIN_MAKE_TEAM = 12;
    public static final int RESULT_MAKE_TEAM = 13;

    public static final String TAG = "MainActivity";
    public static Activity mainActivity;      //signup2Activity에서 finish시키기 위해 쓰는 변수
    int myListSize = 0;       //내그룹 찾는 for 문의 마지막 콜백에서 뷰 처리하기위해서 flag로 둔 값임.

    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.drawerlayout) DrawerLayout drawerLayout;
    @Bind(R.id.navigationView) NavigationView navigationView;
    @Bind(R.id.main_recyclerview) RecyclerView mainRecyclerView;
//    @Bind(R.id.myteam_recyclerview) RecyclerView myTeamRecyclerView;
    @Bind(R.id.progressbar) ProgressBar progressBar;
    @Bind(R.id.layout_refresh) SwipeRefreshLayout swipeRefreshLayout;
//    @Bind(R.id.header_my_team) TextView headerMyTeam;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mainActivity = this;

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("모아");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        toolbar.setNavigationIcon(R.drawable.drawericon);

        fab.setOnClickListener(this);

        setMainRecyclerView();      //메인 리사이클러 뷰 설정
        setUpNavDrawer();       //네비게이션 드로어 설정
        setUpNavDrawerHeader();     //네비게이션 드로어 헤더 설정
        getListData();      //리사이클러 뷰 리스트 데이터 불러오기
    }

    //메인 리사이클러뷰 설정
    private void setMainRecyclerView() {
        swipeRefreshLayout.setOnRefreshListener(this);
        mainRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mainRecyclerView.setLayoutManager(layoutManager);
    }

    //네비게이션 드로어 설정
    private void setUpNavDrawer() {
        //로그인 하지 않은 상태이면 일부 메뉴 숨김.
        if (user == null) {
            navigationView.getMenu().findItem(R.id.apply_condition).setVisible(false);  //신청 현황 숨김
            navigationView.getMenu().findItem(R.id.message).setVisible(false);  //쪽지함 숨김
        } else {
            navigationView.getMenu().findItem(R.id.apply_condition).setVisible(true);  //신청 현황 숨김
            navigationView.getMenu().findItem(R.id.message).setVisible(true);  //쪽지함 숨김
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                return changeDrawerMenu(menuItem);
            }
        });
    }

    //드로어 헤더 설정
    private void setUpNavDrawerHeader() {
        View headerLayout = navigationView.inflateHeaderView(R.layout.drawer_header);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    startActivity(new Intent(MainActivity.this, MypageActivity.class));
                } else {
                    Intent loginIntent = new Intent(mContext, LoginActivity.class);
                    loginIntent.putExtra("goBackPreviousPage", true);
                    startActivityForResult(loginIntent, RESULT_LOGIN);
                    overridePendingTransition(0, 0);
                }
            }
        });
        TextView txtName = (TextView) headerLayout.findViewById(R.id.name);
        if (user != null) {
            txtName.setText(user.getString("name"));
        } else {
            txtName.setText("로그인을 해 주세요.");
        }
        profileDrawer = (CircleImageView) headerLayout.findViewById(R.id.profile);
    }

    //드로어 프로필 사진 설정
    private void loadProfile() {
        boolean isProfileExists = false;
        if (user != null) {
            ParseFile parseFile = user.getParseFile("profile");
            if (parseFile != null) {
                isProfileExists = true;
                if (!isFinishing())
                    Glide.with(mContext).load(parseFile.getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(profileDrawer);
            }
        }
        if (!isProfileExists) {
            profileDrawer.setImageResource(R.drawable.ic_user);
        }
    }

    //네비게이션 드로어 아이템 클릭 설정
    private boolean changeDrawerMenu(MenuItem menuItem) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (menuItem.getItemId()) {
            //어바웃
            case R.id.about:
                Toast.makeText(mContext, "우리는 모무 ㅎㅎ 곧 만들거야", Toast.LENGTH_SHORT).show();
                return true;

            //설정
            case R.id.setup:
                startActivity(new Intent(MainActivity.this, SetupActivity.class));
                return true;

            //신청 현황
            case R.id.apply_condition:
                Toast.makeText(mContext, "준비중입니다.", Toast.LENGTH_SHORT).show();
                return true;

            //쪽지함
            case R.id.message:
                if (user != null)
                    startActivity(new Intent(MainActivity.this, MessageActivity.class));
                return true;
        }
        return true;
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (drawerLayout.isDrawerOpen(navigationView))
                    drawerLayout.closeDrawers();
                else {
                    backKeyPressed();
                }
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private long timeBackKeyPressed = 0;

    //백버튼 클릭 설정
    public void backKeyPressed() {
        if (System.currentTimeMillis() <= timeBackKeyPressed + 2000) {
            finish();
        } else {
            timeBackKeyPressed = System.currentTimeMillis();
            Toast.makeText(this, getString(R.string.back_button), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:    //검색 버튼 클릭 시
                startActivity(new Intent(mContext, SearchActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.action_alert:     //알림 버튼 클릭 시
                startActivity(new Intent(mContext, AlertActivity.class));
                overridePendingTransition(0, 0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Floating Action 버튼, 팀만들기
            case R.id.fab:
                //비로그인 시 로그인 화면으로 이동
                if (user == null) {
                    Toast.makeText(mContext, "팀을 생성하시려면 로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(mContext, LoginActivity.class);
                    loginIntent.putExtra("goBackPreviousPage", true);
                    startActivityForResult(loginIntent, RESULT_LOGIN_MAKE_TEAM);
                    overridePendingTransition(0, 0);
                    //로그인 시 팀만들기 화면으로 이동
                } else {
                    startActivityForResult(new Intent(MainActivity.this, TeamAddActivity.class), RESULT_MAKE_TEAM);
                }
                break;
        }

    }

    //팀 리스트 불러옴
    public void getListData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mainTeamItems != null)
                            mainTeamItems.clear();
                        else
                            mainTeamItems = new ArrayList<>();
                        if (myTeamItems != null)
                            myTeamItems.clear();
                        else
                            myTeamItems = new ArrayList<>();
                        if (!swipeRefreshLayout.isRefreshing()) {
                            progressBar.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.GONE);
                        }
                        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Team");
                        parseQuery.whereEqualTo("isVisible", true);
                        parseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(final List<ParseObject> list, ParseException e) {
                                if (swipeRefreshLayout.isRefreshing())
                                    swipeRefreshLayout.setRefreshing(false);
                                if (list == null || list.size() == 0) return;

                                if(MainActivity.this.user == null){         //로그인이 안되어 있을 경우
                                    for (final ParseObject parseObject : list) {        //전체 리스트 불러옴
                                        final ParseUser user = parseObject.getParseUser("admin_member");
                                        try {
                                            user.fetchIfNeeded();
                                            TeamItem item = new TeamItem(parseObject.getObjectId(), parseObject.getString("intro"), user.getString("name"), user.getUsername(), parseObject.getString("intro_detail"));
                                            mainTeamItems.add(item);
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }
                                    }   //end for

                                    progressBar.setVisibility(View.GONE);
                                    mainRecyclerView.setAdapter(new TeamRecyclerAdapter(mContext, mainTeamItems, R.layout.activity_team,myListSize));
                                    if (swipeRefreshLayout.getVisibility() == View.GONE) {
                                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                                    }
                                    return;
                                }

                                else {      //로그인이 되어 있을 경우
                                     for(final ParseObject parseObject : list) {        //전체 리스트 불러옴
                                        final ParseUser user = parseObject.getParseUser("admin_member");
                                        try {
                                            user.fetchIfNeeded();
                                            ParseRelation<ParseUser> memberRelation = parseObject.getRelation("member");
                                            final ParseQuery<ParseUser> memberQuery = memberRelation.getQuery();             //릴레이션을 가지고 쿼리문 돌림
                                            memberQuery.whereEqualTo("username", MainActivity.this.user.getUsername());     //내가 멤버로 있는 팀 검색
                                            memberQuery.findInBackground(new FindCallback<ParseUser>() {
                                                @Override
                                                public void done(List<ParseUser> mylist, ParseException e) {
                                                   if (mylist != null && mylist.size() > 0) {      //내 이름이 포함되어 있을 경우 내 그릅에 포함시킴
                                                        TeamItem item = new TeamItem(parseObject.getObjectId(), parseObject.getString("intro"), user.getString("name"), user.getUsername(), parseObject.getString("intro_detail"));
                                                        myTeamItems.add(item);
                                                    }
                                                    else{           //내 이름이 포함 되어있지 않을 경우 추천 그룹에 포함시킴
                                                        TeamItem item = new TeamItem(parseObject.getObjectId(), parseObject.getString("intro"), user.getString("name"), user.getUsername(), parseObject.getString("intro_detail"));
                                                        mainTeamItems.add(item);
                                                    }
                                                    if(list.size()==(myTeamItems.size()+mainTeamItems.size())){     //내 그룹 리스트와 추천 그룹 리스트 수가 전체 리스트 수와 같을 경우
                                                        mainTeamItems.addAll(0,myTeamItems);        //내 그룹 리스트를 전체 리스트 맨 앞에 포함시켜 둠
                                                        progressBar.setVisibility(View.GONE);
                                                        mainRecyclerView.setAdapter(new TeamRecyclerAdapter(mContext, mainTeamItems, R.layout.activity_team,myTeamItems.size()));
                                                        if (swipeRefreshLayout.getVisibility() == View.GONE) {
                                                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                }
                                            });

                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }

                            }
                        });
                    }
                });
            }
        }).start();
    }//getListData

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //팀 생성 성공 후 리스트 새로 불러오기
                case RESULT_MAKE_TEAM:
                    getListData();
                    break;

                //로그인 후 화면 변화
                case RESULT_LOGIN:
                    user = ParseUser.getCurrentUser();
                    if (drawerLayout.isDrawerOpen(navigationView)) {
                        drawerLayout.closeDrawers();
                    }
                    navigationView.removeHeaderView(navigationView.getHeaderView(0));        //기존 드로어 헤더뷰 삭제
                    setUpNavDrawerHeader();
                    loadProfile();
                    getListData();
                    break;

                //팀생성 버튼 비로그인 상태에서 클릭시 로그인 후 화면 변화
                case RESULT_LOGIN_MAKE_TEAM:
                    user = ParseUser.getCurrentUser();
                    navigationView.removeHeaderView(navigationView.getHeaderView(0));        //기존 드로어 헤더뷰 삭제
                    setUpNavDrawerHeader();
                    loadProfile();
                    fab.performClick();
                    getListData();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfile();
    }

    @Override
    public void onRefresh() {
        getListData();
    }
}
