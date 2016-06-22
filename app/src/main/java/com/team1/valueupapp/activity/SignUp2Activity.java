package com.team1.valueupapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
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
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by songmho on 16. 2. 27.
 */
public class SignUp2Activity extends AppCompatActivity implements View.OnClickListener {
    private List<String> arrTags = new ArrayList<>();
    private ParseUser signUp_User = new ParseUser();

    int CAMERA_REQUEST = 1000;
    int SELECT_FILE = 2000;
    CharSequence[] item = {"카메라", "갤러리에서 사진 가져오기", "삭제"};
    ParseFile profileParse;
    Bitmap thum = null;

    @Bind(R.id.profile) ImageView profile;
    @Bind(R.id.edit_info) EditText editInfo;
    @Bind(R.id.btn_tag_1) Button btnTag1;
    @Bind(R.id.btn_tag_2) Button btnTag2;
    @Bind(R.id.btn_tag_3) Button btnTag3;
    @Bind(R.id.bt_signUp) Button btnSignUp;
    @Bind(R.id.edit_tag) EditText editTags;
    @Bind(R.id.tool_bar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_2);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("회원가입");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.with(getApplicationContext()).load(R.drawable.ic_user).placeholder(R.drawable.ic_user).
                bitmapTransform(new CropCircleTransformation(getApplicationContext())).into(profile);    //profile 이미지 씌우기. ic_user가 placeholder

        profile.setOnClickListener(this);
        btnTag1.setOnClickListener(this);
        btnTag2.setOnClickListener(this);
        btnTag3.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

    }     //onCreate

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile:          //profile을 클릭했을 때
                //  MakingAlertDialog();
                Toast.makeText(SignUp2Activity.this, "아직 기능이 완벽히 구현이 안됐으니 이쁜사진을 골라두자!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_tag_1:                                               //관심사 태그에서 1번째
                if (!arrTags.contains(btnTag1.getText().toString())) {      //리스트에 관심사 1번 태그가 없으면
                    arrTags.add(btnTag1.getText().toString());             //리스트에 관심사 추가
                    editTags.append(btnTag1.getText().toString() + " ");   //edittext에 이어서 씀
                }
                break;
            case R.id.btn_tag_2:                                               //관심사 태그에서 2번째
                if (!arrTags.contains(btnTag2.getText().toString())) {      //리스트에 관심사 2번 태그가 없으면
                    arrTags.add(btnTag2.getText().toString());             //리스트에 관심사 추가
                    editTags.append(btnTag2.getText().toString() + " ");   //edittext에 이어서 씀
                }
                break;
            case R.id.btn_tag_3:                                               //관심사 태그에서 3번째
                if (!arrTags.contains(btnTag3.getText().toString())) {      //리스트에 관심사 3번 태그가 없으면
                    arrTags.add(btnTag3.getText().toString());             //리스트에 관심사 추가
                    editTags.append(btnTag3.getText().toString() + " ");   //edittext에 이어서 씀
                }
                break;
            case R.id.bt_signUp:                            //회원가입 버튼 눌렀을 때
                arrTags.clear();
                String[] arr_s = editTags.getText().toString().split("#");
                for (String s : arr_s) {
                    if (!s.equals(""))
                        arrTags.add(s);
                }
                signUp_User.setUsername(getIntent().getStringExtra("username"));
                signUp_User.put("name", getIntent().getStringExtra("name"));
                signUp_User.setPassword(getIntent().getStringExtra("password"));
                signUp_User.setEmail(getIntent().getStringExtra("username"));
                signUp_User.put("info", editInfo.getText().toString());
                signUp_User.put("tag", arrTags);
            /*    if(thum!=null)
                    imgSendParse(thum);
            */
                signUp_User.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(SignUp2Activity.this, "가입 완료!", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(SignUp2Activity.this, MainActivity.class));
                        }       //endif
                    }       //done method
                });
                break;
        }       //switch
    }       //onClick

    private void MakingAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp2Activity.this, R.style.dialog);
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
                    ParseUser.getCurrentUser().remove("profile");
                    Toast.makeText(getApplicationContext(), "삭제하였습니다.", Toast.LENGTH_SHORT).show();
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
        thum = null;
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == CAMERA_REQUEST) {
                thum = (Bitmap) data.getExtras().get("data");
                profile.setImageBitmap(thum);
            } else if (requestCode == SELECT_FILE) {
                Uri uri = data.getData();
                try {
                    thum = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    profile.setImageBitmap(thum);
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
        if (signUp_User.get("profile") != null)
            signUp_User.remove("profile");
        signUp_User.put("profile", profileParse);
        signUp_User.saveInBackground();
    }

    private byte[] bitmapToByteArray(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }


}       //class
