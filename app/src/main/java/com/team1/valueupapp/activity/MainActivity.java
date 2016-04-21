package com.team1.valueupapp.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    CircleImageView profile;
    CircleImageView profile_drawer;
    Context mContext;
    FragmentTransaction fragmentTransaction;

    CharSequence[] item = {"카메라", "갤러리에서 사진 가져오기", "삭제"};
    ParseUser user = ParseUser.getCurrentUser();

    ArrayList<TeamItem> mainTeamItems;

    public static final int RESULT_LOGIN = 11;
    public static final int RESULT_LOGIN_MAKE_TEAM = 12;
    public static final int RESULT_MAKE_TEAM = 13;

    public static final String TAG = "MainActivity";

    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.drawerlayout) DrawerLayout drawerLayout;
    @Bind(R.id.navigationView) NavigationView navigationView;
    @Bind(R.id.main_recyclerview) RecyclerView mainRecyclerView;
    @Bind(R.id.progressbar) ProgressBar progressBar;
    @Bind(R.id.layout_refresh) SwipeRefreshLayout swipeRefreshLayout;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;


        SharedPreferences shpref = getSharedPreferences("myPref", 0);
        int count = shpref.getInt("Count", -100);
        if (count == -100) {
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
            count = 1;
        } else {
            count++;
        }
        SharedPreferences.Editor editor = shpref.edit();
        editor.putInt("Count", count).apply();

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        swipeRefreshLayout.setOnRefreshListener(this);

        mainRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mainRecyclerView.setLayoutManager(layoutManager);

        fab.setOnClickListener(this);
        setSupportActionBar(toolbar);

        setUpNavDrawer();

        makeDrawerHeader();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                return changeDrawerMenu(menuItem);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getListData();
                    }
                });
            }
        }).start();
    }

    private void setUpNavDrawer() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("모아");
        toolbar.setNavigationIcon(R.drawable.drawericon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

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
        }
        return true;
    }

    //드로어 헤더 설정
    private void makeDrawerHeader() {
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
        profile_drawer = (CircleImageView) headerLayout.findViewById(R.id.profile);
    }

    //드로어 프로필 사진 설정
    private void loadProfile() {
        boolean isProfileExists = false;
        if (user != null) {
            ParseFile parseFile = user.getParseFile("profile");
            if (parseFile != null) {
                isProfileExists = true;
                if (!isFinishing())
                    Glide.with(mContext).load(parseFile.getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(profile_drawer);
            }
        }
        if (!isProfileExists) {
            profile_drawer.setImageResource(R.drawable.ic_user);
        }
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

    public void backKeyPressed() {
        if (System.currentTimeMillis() <= timeBackKeyPressed + 2000) {
            finish();
        } else {
            timeBackKeyPressed = System.currentTimeMillis();
            Toast.makeText(this, "뒤로 가기 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(mContext, SearchActivity.class));
            overridePendingTransition(0, 0);
//            Toast.makeText(mContext, "준비중입니다 '-'", Toast.LENGTH_SHORT).show();
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
        if (mainTeamItems != null)
            mainTeamItems.clear();
        else
            mainTeamItems = new ArrayList<>();
        if (!swipeRefreshLayout.isRefreshing())
            progressBar.setVisibility(View.VISIBLE);
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Team");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                if (list == null || list.size() == 0) return;
                for (ParseObject parseObject : list) {
                    ParseUser user = parseObject.getParseUser("admin_member");
                    try {
                        user.fetchIfNeeded();
                        TeamItem item = new TeamItem(parseObject.getString("intro"), user.getString("name"), user.getUsername(), parseObject.getString("intro_detail"));
                        mainTeamItems.add(item);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                }
                if (!isFinishing())
                    progressBar.setVisibility(View.GONE);
                mainRecyclerView.setAdapter(new TeamRecyclerAdapter(mContext, mainTeamItems, R.layout.activity_team));
            }
        });
    }//getListData

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_MAKE_TEAM && resultCode == RESULT_OK) {
            //팀 생성 성공 후 리스트 새로 불러오기.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getListData();
                        }
                    });
                }
            }).start();
        } else if (requestCode == RESULT_LOGIN && resultCode == RESULT_OK) {
            //로그인 후 화면 변화
            user = ParseUser.getCurrentUser();
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawers();
            }
            navigationView.removeHeaderView(navigationView.getHeaderView(0));        //기존 드로어 헤더뷰 삭제
            makeDrawerHeader();
            loadProfile();
        } else if (requestCode == RESULT_LOGIN_MAKE_TEAM && resultCode == RESULT_OK) {
            //로그인 후 화면 변화
            user = ParseUser.getCurrentUser();
            navigationView.removeHeaderView(navigationView.getHeaderView(0));        //기존 드로어 헤더뷰 삭제
            makeDrawerHeader();
            loadProfile();
            fab.performClick();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfile();
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getListData();
                    }
                });
            }
        }).start();
    }
}
