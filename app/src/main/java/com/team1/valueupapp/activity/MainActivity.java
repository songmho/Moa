package com.team1.valueupapp.activity;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CircleImageView profile;
    CircleImageView profile_drawer;
    Context mContext;
    FragmentTransaction fragmentTransaction;

    CharSequence[] item = {"카메라", "갤러리에서 사진 가져오기", "삭제"};
    String tempPath = "data/data/com.team1.valueupapp/files/profile.jpg";
    File profileImage = new File("data/data/com.team1.valueupapp/files/profile.jpg");
    ParseFile profile_parse;
    ParseUser user = ParseUser.getCurrentUser();

    ArrayList<TeamItem> mainTeamItems;


    public static final String TAG = "MainActivity";


    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.drawerlayout) DrawerLayout drawerLayout;
    @Bind(R.id.navigationView) NavigationView navigationView;
    @Bind(R.id.name) TextView name;
    @Bind(R.id.main_recyclerview) RecyclerView mainRecyclerView;

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
                        makeList();
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
            case R.id.about:
//            {
//                ParseQuery<ParseObject> picked_query = ParseQuery.getQuery("Picked");
//                picked_query.whereEqualTo("user", ParseUser.getCurrentUser());
//                picked_query.findInBackground(new FindCallback<ParseObject>() {
//                    @Override
//                    public void done(List<ParseObject> list, ParseException e) {
////                        Log.d("set", list.size()+"");
//                        ParseRelation<ParseUser> picked_relation = list.get(0).getRelation("picked");
//                        picked_relation.getQuery().findInBackground(new FindCallback<ParseUser>() {
//                            @Override
//                            public void done(List<ParseUser> list, ParseException e) {
//                                int size;
//                                if (list.isEmpty()) {
//                                    size = 0;
//                                } else {
//                                    size = list.size();
//                                }//end else
//                                Log.d("set", size + "");
//                            }
//                        });
//                    }
//                });
//            } // TODO: 16. 3. 19. 뻗음
                Toast.makeText(mContext, "우리는 모무 ㅎㅎ 곧 만들거야", Toast.LENGTH_SHORT).show();
                return true;

           /* case R.id.introduce:
                drawerLayout.closeDrawers();
                startActivity(new Intent(MainActivity.this, MemberActivity.class));
                return true;*/

       /*     case R.id.basket:
                drawerLayout.closeDrawers();
                if (user != null)
                    startActivity(new Intent(MainActivity.this, InterestActivity.class));
                else {
                    Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                return true;


            case R.id.mypage:
                drawerLayout.closeDrawers();
                if (user != null)
                    startActivity(new Intent(MainActivity.this, MypageActivity.class));
                else {
                    Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                return true;*/

            case R.id.setup:
                startActivity(new Intent(MainActivity.this, SetupActivity.class));
                return true;
        }
        return true;
    }


    private void makeDrawerHeader() {
        profile_drawer = (CircleImageView) navigationView.findViewById(R.id.profile);
        navigationView.findViewById(R.id.header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    startActivity(new Intent(MainActivity.this, MypageActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });
        TextView t = (TextView) navigationView.findViewById(R.id.name);
        if (user != null) {
            t.setText(user.getString("name"));
        } else {
            t.setText("로그인을 해 주세요.");
        }
        if (profileImage.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(tempPath);
            profile_drawer.setImageBitmap(bm);
        } else {
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user);
            profile_drawer.setImageBitmap(b);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (drawerLayout.isDrawerOpen(navigationView))
                    drawerLayout.closeDrawers();
                else {
                    moveTaskToBack(true);
                    finish();
                }
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_2, menu);
       /* MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.clearFocus();
            searchView.setQueryHint("이름 검색");
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("query", query);
                    intent.putExtra("page", "main");
                    startActivity(intent);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    return false;
                }
            });
        }*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
//            startActivity(new Intent(mContext, SearchActivity.class));
//            overridePendingTransition(0, 0);
            Toast.makeText(mContext, "준비중입니다 '-'", Toast.LENGTH_SHORT).show();
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
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    overridePendingTransition(0, 0);
                    //로그인 시 팀만들기 화면으로 이동
                } else {
                    startActivity(new Intent(MainActivity.this, TeamAddActivity.class));
                }
                break;
        }

    }

    //팀 리스트 불러옴
    public void makeList() {
        if (mainTeamItems != null)
            mainTeamItems.clear();
        else
            mainTeamItems = new ArrayList<>();
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Team");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list == null || list.size() == 0) return;
                for (ParseObject parseObject : list) {
                    ParseRelation<ParseUser> member_relatrion = parseObject.getRelation("member");
                    ParseUser user = parseObject.getParseUser("admin_member");
                    try {
                        user.fetchIfNeeded();
                        TeamItem item = new TeamItem(parseObject.getString("intro"), user.getString("name"), user.getUsername(), parseObject.getString("intro_detail"));
                        mainTeamItems.add(item);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                }
                mainRecyclerView.setAdapter(new TeamRecyclerAdapter(mContext, mainTeamItems, R.layout.activity_team));
            }
        });
    }//makeList
}
