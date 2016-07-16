package com.team1.valueupapp.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.team1.valueupapp.R;
import com.team1.valueupapp.utility.Utility;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hyemi on 2015-08-02.
 * 회원가입 1단계
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.tool_bar) Toolbar toolbar;
    @Bind(R.id.user_name) EditText userName;
    @Bind(R.id.mail) EditText mail;
    @Bind(R.id.pass) EditText pass;
    @Bind(R.id.pass_check) EditText pass_check;
    @Bind(R.id.btn_next) Button btn_next;

    public static Activity signUpActivity;      //signup2Activity에서 finish시키기 위해 쓰는 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpActivity = this;

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("회원가입");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog();
            }
        });

        btn_next.setOnClickListener(this);
    }//onCreate

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
            makeDialog();
        return super.onKeyDown(keyCode, event);
    }

    void makeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("회원가입을 취소하시겠습니까?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //다음 버튼이 눌렸을 때
            case R.id.btn_next:
                if (userName.getText().length() == 0) //이름이 입력되지 않았을 때
                    Toast.makeText(SignUpActivity.this, getString(R.string.signup_name_hint), Toast.LENGTH_SHORT).show();
                else if (mail.getText().length() == 0) //이메일이 입력되지 않았을 때
                    Toast.makeText(SignUpActivity.this, getString(R.string.signup_email_hint), Toast.LENGTH_SHORT).show();
                else if (!Utility.isEmailValid(mail.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.signup_email_valid_hint), Toast.LENGTH_SHORT).show();
                } else if (pass.getText().length() == 0 || pass_check.getText().length() == 0) { //비밀번호나 확인이 입력되지 않았을 때
                    Toast.makeText(SignUpActivity.this, getString(R.string.signup_password_hint), Toast.LENGTH_SHORT).show();

                }
                //전부 입력되었을 때
                else {
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("username", String.valueOf(mail.getText()));
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> list, ParseException e) {
                            if (list.isEmpty()) {
                                if (String.valueOf(pass.getText()).equals(String.valueOf(pass_check.getText()))) {   //비밀번호와 확인이 같을 경우
                                    Intent intent = new Intent(SignUpActivity.this, SignUp2Activity.class);
                                    intent.putExtra("username", String.valueOf(mail.getText()));      //이름, email, 비밀번호 SignUp_2_Activity로 전달
                                    intent.putExtra("password", String.valueOf(pass.getText()));
                                    intent.putExtra("name", String.valueOf(userName.getText()));
                                    startActivity(intent);              // SignUp_2_Activity로 이동
                                 }           //endif
                                else {      //비밀번호와 확인이 다를 경우
                                    Toast.makeText(SignUpActivity.this, "비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show(); //비밀번호 확인 관련 토스트
                                }       //end else
                            } else
                                Toast.makeText(SignUpActivity.this, "아이디를 확인하세요.", Toast.LENGTH_SHORT).show(); //아이디 확인 관련 토스트

                        }
                    });

                }
                break;
        }       //end switch
    }       //end onClick Method

}//class

