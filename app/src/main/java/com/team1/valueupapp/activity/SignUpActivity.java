package com.team1.valueupapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.team1.valueupapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hyemi on 2015-08-02.
 * 가입 첫번째 페이지
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.tool_bar) Toolbar toolbar;
    @Bind(R.id.user_name) EditText userName;
    @Bind(R.id.mail) EditText mail;
    @Bind(R.id.pass) EditText pass;
    @Bind(R.id.pass_check) EditText pass_check;
    @Bind(R.id.btn_next) Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("회원가입");

        btn_next.setOnClickListener(this);
    }//onCreate

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //다음 버튼이 눌렸을 때
            case R.id.btn_next:
                if (userName.getText().length() == 0) //이름이 입력되지 않았을 때
                    Toast.makeText(SignUpActivity.this, getString(R.string.signup_name_hint), Toast.LENGTH_SHORT).show();
                else if (mail.getText().length() == 0) //이메일이 입력되지 않았을 때
                    Toast.makeText(SignUpActivity.this, getString(R.string.signup_email_hint), Toast.LENGTH_SHORT).show();
                else if (pass.getText().length() == 0 || pass_check.getText().length() == 0) //비밀번호나 확인이 입력되지 않았을 때
                    Toast.makeText(SignUpActivity.this, getString(R.string.signup_password_hint), Toast.LENGTH_SHORT).show();

                    //전부 입력되었을 때
                else {
                    if (String.valueOf(pass.getText()).equals(String.valueOf(pass_check.getText()))) {   //비밀번호와 확인이 같을 경우
                        Intent intent = new Intent(SignUpActivity.this, SignUp2Activity.class);
                        intent.putExtra("username", String.valueOf(mail.getText()));      //이름, email, 비밀번호 SignUp_2_Activity로 전달
                        intent.putExtra("password", String.valueOf(pass.getText()));
                        intent.putExtra("name", String.valueOf(userName.getText()));
                        startActivity(intent);              // SignUp_2_Activity로 이동
                        finish();
                    }           //endif
                    else {      //비밀번호와 확인이 다를 경우
                        Toast.makeText(SignUpActivity.this, "비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show(); //비밀번호 확인 관련 토스트
                    }       //end else
                }
                break;
        }       //end switch
    }       //end onClick Method

}//class

