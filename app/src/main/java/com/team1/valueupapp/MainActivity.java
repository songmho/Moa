package com.team1.valueupapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    CircleImageView profile;

    FragmentTransaction fragmentTransaction;
    Fragment cur_fragment = new MainFragment();

    SearchFragment searchFragment = new SearchFragment();
    Boolean isvisible = true;

    CharSequence[] item={"카메라","갤러리에서 사진 가져오기","삭제"};
    Bitmap bm;
    String tempPath="data/data/com.team1.valueupapp/files/profile.jpg";
    File profileimage=new File("data/data/com.team1.valueupapp/files/profile.jpg");
    File file_up_path=new File("data/data/com.team1.valueupapp/files/");
    ParseFile profile_parse;
    ParseUser user=ParseUser.getCurrentUser();
    int CAMERA_REQUEST=1000;
    int SELECT_FILE=2000;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ParseUser.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpNavDrawer();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, cur_fragment);
        fragmentTransaction.commit();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        navigationView = (NavigationView) findViewById(R.id.navigationView);

        makeDrawerHeader();
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakingAlertDialog();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //현재 클릭된 그룹 알아내서 클릭 설정하는 코드
                if (menuItem.getGroupId() == R.id.group_mentor) {       //멘토 관련 그룹 클릭 됬을 때
                    navigationView.getMenu().setGroupCheckable(R.id.group_setup, false, true);
                    navigationView.getMenu().setGroupCheckable(R.id.group_team, false, true);
                    navigationView.getMenu().setGroupCheckable(R.id.group_mentor, true, true);
                    navigationView.getMenu().setGroupCheckable(R.id.group_mypage, false, true);

                } else if (menuItem.getGroupId() == R.id.group_team) {        //팀 관련 그룹 클릭 됬을 때
                    navigationView.getMenu().setGroupCheckable(R.id.group_setup, false, true);
                    navigationView.getMenu().setGroupCheckable(R.id.group_team, true, true);
                    navigationView.getMenu().setGroupCheckable(R.id.group_mentor, false, true);
                    navigationView.getMenu().setGroupCheckable(R.id.group_mypage, false, true);

                } else if (menuItem.getGroupId() == R.id.group_mypage) {
                    navigationView.getMenu().setGroupCheckable(R.id.group_setup, false, true);
                    navigationView.getMenu().setGroupCheckable(R.id.group_team, false, true);
                    navigationView.getMenu().setGroupCheckable(R.id.group_mentor, false, true);
                    navigationView.getMenu().setGroupCheckable(R.id.group_mypage, true, true);
                } else {                                                   //설정 그룹 클릭 됬을 때
                    navigationView.getMenu().setGroupCheckable(R.id.group_setup, true, true);
                    navigationView.getMenu().setGroupCheckable(R.id.group_team, false, true);
                    navigationView.getMenu().setGroupCheckable(R.id.group_mentor, false, true);
                    navigationView.getMenu().setGroupCheckable(R.id.group_mypage, false, true);

                }

                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.introduce:
                        getSupportActionBar().setTitle("참가자 소개");
                        cur_fragment = new MainFragment();
                        fragmentTransaction.replace(R.id.container, cur_fragment);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();
                        isvisible = true;
                        invalidateOptionsMenu();
                        return true;

                    case R.id.basket:
                        getSupportActionBar().setTitle("관심멤버");
                        cur_fragment = new BasketFragment();
                        fragmentTransaction.replace(R.id.container, cur_fragment);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();
                        isvisible = true;
                        invalidateOptionsMenu();
                        return true;

                    case R.id.mentor_info:
                        getSupportActionBar().setTitle("멘토소개");
                        cur_fragment = new MentorFragment();
                        fragmentTransaction.replace(R.id.container, cur_fragment);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();
                        isvisible = false;
                        invalidateOptionsMenu();
                        return true;


                    case R.id.mentoring:
                        getSupportActionBar().setTitle("멘토링 일정");
                        cur_fragment = new MentoringFragment();
                        fragmentTransaction.replace(R.id.container, cur_fragment);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();
                        isvisible = false;
                        invalidateOptionsMenu();
                        return true;

                    case R.id.mypage:
                        getSupportActionBar().setTitle("마이페이지");
                        startActivity(new Intent(MainActivity.this, MypageActivity.class));
                        drawerLayout.closeDrawers();
                        isvisible = false;
                        invalidateOptionsMenu();
                        return true;

                    case R.id.setup:
                        getSupportActionBar().setTitle("설정");
                        cur_fragment = new SetupFragment();
                        fragmentTransaction.replace(R.id.container, cur_fragment);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();
                        isvisible = false;
                        invalidateOptionsMenu();
                        return true;
                }
                return true;
            }
        });

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
                        gallery.addCategory(Intent.CATEGORY_OPENABLE);gallery.setType("image/*");
                        startActivityForResult(Intent.createChooser(gallery, "갤러리 선택"), SELECT_FILE);
                    } else if (item[position].equals("삭제")) {
                    File[] files=file_up_path.listFiles();
                    for(int i=0;i<files.length;i++){
                        String fname=files[i].getName();
                        if(fname.equals("profile.jpg"))
                            files[i].delete();
                    }
                    ParseUser.getCurrentUser().remove("profile");
                    Toast.makeText(getApplicationContext(),"삭제하였습니다.",Toast.LENGTH_SHORT).show();
                    Bitmap b=BitmapFactory.decodeResource(getResources(),R.drawable.splash_logo);
                    profile.setImageBitmap(b);
                }
            }
        });
        builder.show();
    }

    private void makeDrawerHeader() {
        profile = (CircleImageView) navigationView.findViewById(R.id.profile);
        TextView t = (TextView) navigationView.findViewById(R.id.name);
        if (ParseUser.getCurrentUser() != null)
            t.setText(ParseUser.getCurrentUser().getString("name"));
        else
            t.setText("Hello");
        if(profileimage.exists()){
            Bitmap bm=BitmapFactory.decodeFile(tempPath);
            profile.setImageBitmap(bm);}
        else{
            Bitmap b=BitmapFactory.decodeResource(getResources(),R.drawable.splash_logo);
            profile.setImageBitmap(b);
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

    private void setUpNavDrawer() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("소개");
        toolbar.setNavigationIcon(R.drawable.drawericon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(isvisible);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.clearFocus();
            searchView.setQueryHint("이름");
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d("query", query);
                    Bundle bundle = new Bundle();
                    bundle.putString("query", query);
                    searchFragment.setArguments(bundle);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.hide(cur_fragment);
                    fragmentTransaction.add(R.id.container, searchFragment);
                    fragmentTransaction.commit();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d("newText", newText);
                    return false;
                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(searchFragment);
                    fragmentTransaction.show(cur_fragment);
                    fragmentTransaction.commit();
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
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap thum = null;
        if(resultCode==RESULT_OK && data!=null){
            if(requestCode==CAMERA_REQUEST){
                thum=(Bitmap)data.getExtras().get("data");
                profile.setImageBitmap(thum);
                imgSendParse(thum);
            }
            else if(requestCode==SELECT_FILE){
                Uri uri=data.getData();
                try {
                    thum = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    profile.setImageBitmap(thum);
                    imgSendParse(thum);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            File file=new File("profile.jpg");
            FileOutputStream fos= null;
            try {
                fos = openFileOutput("profile.jpg",0);
                thum.compress(Bitmap.CompressFormat.JPEG,50,fos);
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
        profile_parse=new ParseFile("profile.jpg",bitmapTobyte(thum));
        if(user.get("profile")!=null)
            user.remove("profile");
        user.put("profile", profile_parse);
        user.saveInBackground();
    }

    private byte[] bitmapTobyte(Bitmap bm) {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] bytes=stream.toByteArray();
        return bytes;
    }

}
