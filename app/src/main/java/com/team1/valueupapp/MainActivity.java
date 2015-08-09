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
import android.media.ExifInterface;
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
    String file_up_path="data/data/com.team1.valueupapp/files/";
    byte[] profile_byte;
    ParseFile profile_parse;
    ParseUser user;
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
                    Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file=new File(Environment.getExternalStorageDirectory(),"tmp.jpg");
                    camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(camera, CAMERA_REQUEST);
                } else if (item[position].equals("갤러리에서 사진 가져오기")) {
                    Intent gallery=new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    gallery.addCategory(Intent.CATEGORY_OPENABLE);
                    gallery.setType("image/*");
                    startActivityForResult(Intent.createChooser(gallery,"갤러리 선택"),SELECT_FILE);
                } else if (item[position].equals("삭제")) {
                    File file = new File("data/data/com.team1.valueupapp/files/");
                    File file1 = new File("data/data/com.team1.valueupapp/files/profile.jpg");
                    File[] files = file.listFiles();

                    if (file1.exists()) {
                        for (File file2 : files) {
                            String fname = file2.getName();
                            if (fname.equals("profile.jpg")) {
                                file2.delete();
                            }
                        }
                        profile.setImageResource(R.drawable.splash_logo);
                    }
                    user.remove("userProfile");
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
            bm= BitmapFactory.decodeFile(tempPath);
            int degree=0;
            profile.setImageResource(R.drawable.ic_edit);}
        else
            profile.setImageResource(R.drawable.splash_logo);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==CAMERA_REQUEST){
                File f=new File(Environment.getExternalStorageDirectory().toString());
                for(File tmp:f.listFiles()){
                    if(tmp.getName().equals("tmp.jpg")){
                        f=tmp;
                        break;
                    }
                }
                try {
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(), btmapOptions);
                    int degree = GetExifOrientation(tempPath);
                    bm = GetRotatedBitmap(bm, degree);
                    profile.setImageBitmap(bm);

                    profile_byte = bitmapTobyte(bm);
                    profileToparse(profile_byte);

                    OutputStream fout = null;
                    File file = new File(tempPath);
                    File file_up = new File(file_up_path);
                    try {
                        if (!file_up.exists()) {
                            file_up.mkdirs();
                        }
                        file.createNewFile();
                        fout = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.JPEG, 70, fout);
                        fout.flush();
                        fout.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            else if(requestCode==SELECT_FILE){
                Uri selectedImageUri = data.getData();
                String path = getPath(selectedImageUri, MainActivity.this);
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(path, btmapOptions);
                int degree = GetExifOrientation(tempPath);
                bm = GetRotatedBitmap(bm, degree);
                profile.setImageBitmap(bm);

               // profile_byte = bitmapTobyte(bm);
               // profileToparse(profile_byte);

                OutputStream outputStream = null;
                File file = new File(tempPath);
                File file_up = new File(file_up_path);
                try {
                    if (!file_up.exists()) {
                        file_up.mkdirs();
                    }
                    file.createNewFile();
                    outputStream = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private synchronized static Bitmap GetRotatedBitmap(Bitmap bm, int degree) {
        if(degree != 0 && bm!=null){
            Matrix m=new Matrix();
            m.setRotate(degree,(float)bm.getWidth()/2,(float)bm.getHeight()/2);

            try{
                Bitmap b2=Bitmap.createBitmap(bm,0,0, bm.getWidth(),bm.getHeight(),m,true);
                if(b2!=b2){
                    bm.recycle();
                    bm=b2;
                }
            }
            catch (OutOfMemoryError ignored){}
        }

        return bm;
    }

    private synchronized static int GetExifOrientation(String tempPath) {
        int degree=0;
        ExifInterface exifInterface=null;

        try {
            exifInterface=new ExifInterface(tempPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(exifInterface !=null){
            int orientation=exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,-1);

            if(orientation!= -1){
                switch (orientation){
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree=90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree=180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree=270;
                        break;
                }
            }
        }
        return degree;
    }

    private byte[] bitmapTobyte(Bitmap bm) {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] bytes=stream.toByteArray();
        return bytes;
    }

    private String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void profileToparse(byte[] profile_byte) {
        user=ParseUser.getCurrentUser();
        profile_parse=new ParseFile("profile.jpg",profile_byte);
        if(user.get("userProfile")!=null){
            user.remove("userProfile");
        }
        else{
            Log.d("parse", "profile image not saved in Parse.com");
        }
        user.put("profile",profile_parse);
        user.saveInBackground();
    }
}
