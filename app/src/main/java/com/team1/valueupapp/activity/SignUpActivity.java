package com.team1.valueupapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.team1.valueupapp.R;

import java.util.ArrayList;

/**
 * Created by hyemi on 2015-08-02.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    EditText user_name;
    EditText mail;
    EditText pass;
    EditText pass_check;
    TextView user_name_null;
    TextView mail_null;
    TextView pass_null;
    TextView pass_check_null;
    Button btn_next;
    Toolbar toolbar;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("회원가입");

        user_name = (EditText) findViewById(R.id.user_name);
        mail = (EditText) findViewById(R.id.mail);
        pass = (EditText) findViewById(R.id.pass);
        pass_check = (EditText) findViewById(R.id.pass_check);
        user_name_null = (TextView) findViewById(R.id.user_name_null);
        mail_null = (TextView) findViewById(R.id.mail_null);
        pass_null = (TextView) findViewById(R.id.pass_null);
        pass_check_null = (TextView) findViewById(R.id.pass_check_null);
        btn_next = (Button) findViewById(R.id.btn_next);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        btn_next.setOnClickListener(this);
    }//onCreate

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:             //다음 버튼이 눌렸을 때
                if(user_name.getText().length()==0) //이름이 입력되지 않았을 때
                    Toast.makeText(SignUpActivity.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                else if(mail.getText().length()==0) //이메일이 입력되지 않았을 때
                    Toast.makeText(SignUpActivity.this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                else if(pass.getText().length()==0 || pass_check.getText().length()==0) //비밀번호나 확인이 입력되지 않았을 때
                    Toast.makeText(SignUpActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                else {          //전부 입력되었을 때
                    if (String.valueOf(pass.getText()).equals(String.valueOf(pass_check.getText()))) {   //비밀번호와 확인이 같을 경우
                        Intent intent = new Intent(SignUpActivity.this, SignUp_2_Activity.class);
                        intent.putExtra("email", String.valueOf(mail.getText()));      //이름, email, 비밀번호 SignUp_2_Activity로 전달
                        intent.putExtra("password", String.valueOf(pass.getText()));
                        intent.putExtra("name", String.valueOf(user_name.getText()));

                        startActivity(intent);              // SignUp_2_Activity로 이동
                    }           //endif
                    else {      //비밀번호와 확인이 다를 경우
                        Toast.makeText(SignUpActivity.this, "비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show(); //비밀번호 확인 관련 토스트
                    }       //end else
                }
                break;
        }       //end switch
    }       //end onClick Method

}//class

