package com.team1.valueupapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.team1.valueupapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by songmho on 16. 2. 27.
 */
public class SignUp2Activity extends AppCompatActivity implements View.OnClickListener {
    private List<String> arr_inter = new ArrayList<>();
    private ParseUser signUp_User = new ParseUser();

    @Bind(R.id.profile) ImageView profile;
    @Bind(R.id.edit_info) EditText edit_info;
    @Bind(R.id.bt_inter_1) Button bt_inter_1;
    @Bind(R.id.bt_inter_2) Button bt_inter_2;
    @Bind(R.id.bt_inter_3) Button bt_inter_3;
    @Bind(R.id.bt_signUp) Button bt_signUp;
    @Bind(R.id.edit_inter) EditText edit_inter;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_2);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("회원가입");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.with(getApplicationContext()).load(R.drawable.ic_user).placeholder(R.drawable.ic_user).
                bitmapTransform(new CropCircleTransformation(getApplicationContext())).into(profile);    //profile 이미지 씌우기. ic_user가 placeholder

        profile.setOnClickListener(this);
        bt_inter_1.setOnClickListener(this);
        bt_inter_2.setOnClickListener(this);
        bt_inter_3.setOnClickListener(this);
        bt_signUp.setOnClickListener(this);

    }     //onCreate

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile:          //profile을 클릭했을 때
                break;
            case R.id.bt_inter_1:                                               //관심사 태그에서 1번째
                if (!arr_inter.contains(bt_inter_1.getText().toString())) {      //리스트에 관심사 1번 태그가 없으면
                    arr_inter.add(bt_inter_1.getText().toString());             //리스트에 관심사 추가
                    edit_inter.append(bt_inter_1.getText().toString() + " ");   //edittext에 이어서 씀
                }
                break;
            case R.id.bt_inter_2:                                               //관심사 태그에서 2번째
                if (!arr_inter.contains(bt_inter_2.getText().toString())) {      //리스트에 관심사 2번 태그가 없으면
                    arr_inter.add(bt_inter_2.getText().toString());             //리스트에 관심사 추가
                    edit_inter.append(bt_inter_2.getText().toString() + " ");   //edittext에 이어서 씀
                }
                break;
            case R.id.bt_inter_3:                                               //관심사 태그에서 3번째
                if (!arr_inter.contains(bt_inter_3.getText().toString())) {      //리스트에 관심사 3번 태그가 없으면
                    arr_inter.add(bt_inter_3.getText().toString());             //리스트에 관심사 추가
                    edit_inter.append(bt_inter_3.getText().toString() + " ");   //edittext에 이어서 씀
                }
                break;
            case R.id.bt_signUp:                            //회원가입 버튼 눌렀을 때
                arr_inter.clear();
                String[] arr_s = edit_inter.getText().toString().split("#");
                for (String s : arr_s)
                    arr_inter.add(s);
                signUp_User.setUsername(getIntent().getStringExtra("username"));
                signUp_User.put("name", getIntent().getStringExtra("name"));
                signUp_User.setPassword(getIntent().getStringExtra("password"));
                signUp_User.setEmail(getIntent().getStringExtra("username"));
                signUp_User.put("info", edit_info.getText().toString());
                signUp_User.put("tag", arr_inter);
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
}       //class
