package com.team1.valueupapp.activity;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
import com.team1.valueupapp.item.MainListItem;
import com.team1.valueupapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    CircleImageView profile;
    CircleImageView profile_drawer;

    FragmentTransaction fragmentTransaction;

    CharSequence[] item = {"카메라", "갤러리에서 사진 가져오기", "삭제"};
    String tempPath = "data/data/com.team1.valueupapp/files/profile.jpg";
    File profileImage = new File("data/data/com.team1.valueupapp/files/profile.jpg");
    File file_up_path = new File("data/data/com.team1.valueupapp/files/");
    ParseFile profile_parse;
    ParseUser user = ParseUser.getCurrentUser();
    int CAMERA_REQUEST = 1000;
    int SELECT_FILE = 2000;

    //    RecyclerView recyclerView;
//    RecyclerView.LayoutManager layoutManager;
    List<MainListItem> items;

    int cur_fragment_int = 0;

//    TextView current_int = null;

    TextView name = null;
    int mem_count = 0;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpNavDrawer();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        name = (TextView) findViewById(R.id.name);

        if (user != null) {
            setMain();
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);    //로그인시 락모드 해제
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);   //비로그인시 드로어 락모드
        }

        makeDrawerHeader();

        profile_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null)
                    MakingAlertDialog();
                else {
                    Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                return changeDrawerMenu(menuItem);
            }
        });

        findViewById(R.id.action_find_person).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MemberActivity.class));
            }
        });

        findViewById(R.id.action_find_team).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TeamActivity.class));
            }
        });
    }

    private void setUpNavDrawer() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("모아");
        //로그인 한 상태에서만 햄버거 아이콘을 보여준다.
        if (user != null) {
            toolbar.setNavigationIcon(R.drawable.drawericon);
        //로그아웃 상태에서는 백버튼 아이콘을 보여준다.
        } else {
            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    startActivity(new Intent(MainActivity.this, FrontPageActivity.class));
                    finish();
                }
            }
        });
    }

    private void setMain() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Admin");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                name.setText(list.get(0).getString("title"));
            }
        });


        final ParseRelation<ParseUser> relation = user.getRelation("pick");
        relation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    int size;
                    if (list.isEmpty()) {
                        size = 0;
                    } else {
                        size = list.size();
                    }//end else
                } else {
                }
            }
        });
//        pick_int.setText(""+size);  //ListFragment와 같음.. 수정해야함

        ParseQuery<ParseObject> picked_query = ParseQuery.getQuery("Picked");
        picked_query.whereEqualTo("user", user);
        picked_query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
//                        Log.d("set", list.size()+"");
                if (list == null || list.size() == 0) {
                    return;
                }

                ParseRelation<ParseUser> picked_relation = list.get(0).getRelation("picked");
                picked_relation.getQuery().findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        int picked_size;
                        if (list.isEmpty()) {
                            picked_size = 0;
                        } else {
                            picked_size = list.size();
                        }//end else
                    }
                });
            }
        });

    }

    private boolean changeDrawerMenu(MenuItem menuItem) {

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (menuItem.getItemId()) {
            case R.id.team:
                startActivity(new Intent(MainActivity.this, TeamAddActivity.class));
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

    private void MakingAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.dialog);
        builder.setTitle("프로필 사진 추가하기");
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if (item[position].equals("카메라")) {
                    Intent camera;
                    camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (camera.resolveActivity(getPackageManager()) != null)
                        startActivityForResult(camera, CAMERA_REQUEST);
                } else if (item[position].equals("갤러리에서 사진 가져오기")) {
                    Intent gallery = null;
                    gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    gallery.addCategory(Intent.CATEGORY_OPENABLE);
                    gallery.setType("image/*");
                    startActivityForResult(Intent.createChooser(gallery, "갤러리 선택"), SELECT_FILE);
                } else if (item[position].equals("삭제")) {
                    File[] files = file_up_path.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        String fname = files[i].getName();
                        if (fname.equals("profile.jpg"))
                            files[i].delete();
                    }
                    user.remove("profile");
                    Toast.makeText(getApplicationContext(), "삭제하였습니다.", Toast.LENGTH_SHORT).show();
                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user);
                    profile.setImageBitmap(b);
                }
            }
        });
        builder.show();
    }

    private void makeDrawerHeader() {
        profile_drawer = (CircleImageView) navigationView.findViewById(R.id.profile);
        navigationView.findViewById(R.id.header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MypageActivity.class));
            }
        });
        TextView t = (TextView) navigationView.findViewById(R.id.name);
        if (user != null)
            t.setText(user.getString("name"));
        else
            t.setText("이름");
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
                    //로그인 되지 않은 상태에서는 백버튼 누르면 프런트 페이지로 돌아간다.
                    if(user == null) {
                        startActivity(new Intent(MainActivity.this, FrontPageActivity.class));
                    }
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
        MenuItem searchItem = menu.findItem(R.id.action_search);
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
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        } else if (id == R.id.action_refresh) {
            if (user != null)
                setMain();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap thum = null;
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == CAMERA_REQUEST) {
                thum = (Bitmap) data.getExtras().get("data");
                profile_drawer.setImageBitmap(thum);
                profile.setImageBitmap(thum);
                imgSendParse(thum);
            } else if (requestCode == SELECT_FILE && data != null) {
                Uri uri = data.getData();
                try {
                    AssetFileDescriptor afd = getContentResolver().openAssetFileDescriptor(uri, "r");
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inSampleSize = 4;
                    thum = BitmapFactory.decodeFileDescriptor(afd.getFileDescriptor(), null, opt);
                    profile_drawer.setImageBitmap(thum);
                    profile.setImageBitmap(thum);
                    imgSendParse(thum);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            File file = new File("profile.jpg");
            FileOutputStream fos = null;
            try {
                fos = openFileOutput("profile.jpg", 0);
                thum.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void imgSendParse(Bitmap thum) {
        profile_parse = new ParseFile("profile.jpg", bitmapTobyte(thum));
        if (user.get("profile") != null)
            user.remove("profile");
        user.put("profile", profile_parse);
        user.saveInBackground();
    }

    private byte[] bitmapTobyte(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.team:
                startActivity(new Intent(MainActivity.this, MemberActivity.class));
                break;
        }

    }
}
