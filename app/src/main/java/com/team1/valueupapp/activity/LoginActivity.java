package com.team1.valueupapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.team1.valueupapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 로그인 액티비티
 * Created by eugene on 2015-07-18.
 */
public class LoginActivity extends AppCompatActivity {
    @Bind(R.id.id_text) EditText txtId;
    @Bind(R.id.pass_text) EditText txtPassword;
    @Bind(R.id.login_btn) Button btnLogin;
    @Bind(R.id.signup_btn) TextView btnSignUp;
    @Bind(R.id.progressbar) ProgressBar progressBar;

    public static Activity loginActivity;      //signup2Activity에서 finish시키기 위해 쓰는 변수
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginActivity = this;

        if (ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btnSignUp.setText(Html.fromHtml("<u>회원가입하기</u>"));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtId.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (txtPassword.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.VISIBLE);
                                    ParseUser.logInInBackground(String.valueOf(txtId.getText()), String.valueOf(txtPassword.getText()), new LogInCallback() {
                                        @Override
                                        public void done(ParseUser user, ParseException e) {
                                            if (user != null) {     //로그인 성공
                                                progressBar.setVisibility(View.GONE);
                                                //이전 페이지로 돌아갈 경우
                                                if (getIntent().hasExtra("goBackPreviousPage") && getIntent().getBooleanExtra("goBackPreviousPage", false)) {
                                                    setResult(RESULT_OK);
                                                    finish();
                                                } else {
                                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                    finish();
                                                }

                                            } else {    //로그인 실패
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "잘못된 이메일 혹은 비밀번호입니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }).start();
                }

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });//btnSignUp.clickListner
    }
}
