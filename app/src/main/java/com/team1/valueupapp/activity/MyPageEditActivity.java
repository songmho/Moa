package com.team1.valueupapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseUser;
import com.team1.valueupapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by songmho on 15. 8. 8.
 */
public class MyPageEditActivity extends AppCompatActivity implements View.OnClickListener {
    de.hdodenhof.circleimageview.CircleImageView profile;
    String tempPath = "data/data/com.team1.valueupapp/files/profile.jpg";
    File profileimage = new File("data/data/com.team1.valueupapp/files/profile.jpg");


    ParseFile profileParse;
    ParseUser user = ParseUser.getCurrentUser();
    int CAMERA_REQUEST = 1000;
    int SELECT_FILE = 2000;
    CharSequence[] item = {"카메라", "갤러리에서 사진 가져오기", "삭제"};
    File file_up_path = new File("data/data/com.team1.valueupapp/files/");
    private List<String> arrTags;
    Context mContext;

    @Bind(R.id.name) EditText editName;
    @Bind(R.id.myinfo) EditText editInfo;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.edit_tag) EditText editTags;
    @Bind(R.id.btn_tag_1) Button btnTag1;
    @Bind(R.id.btn_tag_2) Button btnTag2;
    @Bind(R.id.btn_tag_3) Button btnTag3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrTags = new ArrayList<>();
        mContext = this;
        setContentView(R.layout.activity_mypage_edit);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle("내정보 수정");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        profile = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.my_image);
        if (profileimage.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(tempPath);
            profile.setImageBitmap(bm);
        } else {
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user);
            profile.setImageBitmap(b);
        }
        Intent getIntent = getIntent();
        editName.setText(getIntent.getStringExtra("name"));
        editInfo.setText(getIntent.getStringExtra("myInfo"));

        profile.setOnClickListener(this);
        btnTag1.setOnClickListener(this);
        btnTag2.setOnClickListener(this);
        btnTag3.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_check) {
            if (editName.getText().toString().trim().equals("")) {
                Toast.makeText(mContext, "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            } else if (editInfo.getText().toString().trim().equals("")) {
                Toast.makeText(mContext, "자기 소개를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            } else if (editTags.getText().toString().trim().equals("")) {
                Toast.makeText(mContext, "관심사 태그를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            } else {    //모두 입력 되었을 때 서버로 전송한다.
                ParseUser user = ParseUser.getCurrentUser();
                user.put("name", String.valueOf(editName.getText()));
                user.put("info", String.valueOf(editInfo.getText()));
                user.put("tag", makeTagArray());
                user.saveInBackground();
                finish();
                Toast.makeText(mContext, "저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (id == android.R.id.home && getIntent().getIntExtra("signup", 0) == 1) {
            startActivity(new Intent(MyPageEditActivity.this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void MakingAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyPageEditActivity.this, R.style.dialog);
        builder.setTitle("프로필 사진 추가하기");
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if (item[position].equals("카메라")) {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (camera.resolveActivity(getPackageManager()) != null)
                        startActivityForResult(camera, CAMERA_REQUEST);
                } else if (item[position].equals("갤러리에서 사진 가져오기")) {
                    Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
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
                    ParseUser.getCurrentUser().remove("profile");
                    Toast.makeText(mContext, "삭제하였습니다.", Toast.LENGTH_SHORT).show();
                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user);
                    profile.setImageBitmap(b);
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap thum = null;
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == CAMERA_REQUEST) {
                thum = (Bitmap) data.getExtras().get("data");
                profile.setImageBitmap(thum);
                imgSendParse(thum);
            } else if (requestCode == SELECT_FILE) {
                Uri uri = data.getData();
                try {
                    thum = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    profile.setImageBitmap(thum);
                    imgSendParse(thum);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream fos = null;
            try {
                fos = openFileOutput("profile.jpg", 0);
                if (thum != null) {
                    thum.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                    fos.flush();
                }
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void imgSendParse(Bitmap thum) {
        profileParse = new ParseFile("profile.jpg", bitmapToByteArray(thum));
        if (user.get("profile") != null)
            user.remove("profile");
        user.put("profile", profileParse);
        user.saveInBackground();
    }

    private byte[] bitmapToByteArray(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //프로필 사진 설정
            case R.id.my_image:
                MakingAlertDialog();
                break;
            //태그 1
            case R.id.btn_tag_1:                                               //관심사 태그에서 1번째
                if (!arrTags.contains(btnTag1.getText().toString())) {      //리스트에 관심사 1번 태그가 없으면
                    arrTags.add(btnTag1.getText().toString());             //리스트에 관심사 추가
                    editTags.append(btnTag1.getText().toString() + " ");   //edittext에 이어서 씀
                }
                break;
            //태그 2
            case R.id.btn_tag_2:                                               //관심사 태그에서 2번째
                if (!arrTags.contains(btnTag2.getText().toString())) {      //리스트에 관심사 2번 태그가 없으면
                    arrTags.add(btnTag2.getText().toString());             //리스트에 관심사 추가
                    editTags.append(btnTag2.getText().toString() + " ");   //edittext에 이어서 씀
                }
                break;
            //태그 3
            case R.id.btn_tag_3:                                               //관심사 태그에서 3번째
                if (!arrTags.contains(btnTag3.getText().toString())) {      //리스트에 관심사 3번 태그가 없으면
                    arrTags.add(btnTag3.getText().toString());             //리스트에 관심사 추가
                    editTags.append(btnTag3.getText().toString() + " ");   //edittext에 이어서 씀
                }
                break;
        }
    }

    public ArrayList<String> makeTagArray() {
        ArrayList<String> arrTags = new ArrayList<>();
        String[] arr_s = editTags.getText().toString().split("#");
        for (String s : arr_s) {
            if (!s.equals(""))
                arrTags.add(s);
        }
        return arrTags;
    }
}
