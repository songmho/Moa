package com.team1.valueupapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.ParseException;

/**
 * Created by hyemi on 2015-08-02.
 */
public class SignUpActivity extends AppCompatActivity {
    EditText user_name;
    EditText mail;
    EditText pass;
    EditText pass_check;
    TextView user_name_null;
    TextView mail_null;
    TextView pass_null;
    TextView pass_check_null;
    Button signup_btn;

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

        signup_btn = (Button) findViewById(R.id.signup_btn);

        progressBar=(ProgressBar)findViewById(R.id.progressbar);



        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if( nullCheck() == 0 ) {
                                    if (String.valueOf(pass.getText()).equals(String.valueOf(pass_check.getText()))) {

                                        progressBar.setVisibility(View.VISIBLE);
                                        ParseUser user = new ParseUser();

                                        user.setUsername(String.valueOf(mail.getText()));
                                        user.setPassword(String.valueOf(pass.getText()));
                                        user.put("name", String.valueOf(user_name.getText()));

                                        user.signUpInBackground(new SignUpCallback() {
                                            @Override
                                            public void done(com.parse.ParseException e) {
                                                progressBar.setVisibility(View.GONE);
                                                if (e == null) {
                                                    Toast.makeText(getApplicationContext(), "가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
//                                    Log.d("error", String.valueOf(e.getCode()));
                                                    if (e.getCode() == 202) {
                                                        mail_null.setText("이미 존재하는 mail 주소입니다.");
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                                                    }//end else

                                                }//end else
                                            }
                                        });//user.signUp

                                    } else {
                                        pass_check_null.setText("비밀번호를 확인하세요.");
                                    }//end else

                                }//end if
                            }
                        });
                    }
                }).start();

            }
        });//signup_btn.clickListner


    }//onCreate

    public int nullCheck() {
        int count = 0;

        if( String.valueOf(user_name.getText()).equals("") ) {
            user_name_null.setText("항목을 입력하세요.");
            count++;
        } else {
            user_name_null.setText("");
        }//end else

        if(String.valueOf(mail.getText()).equals("")) {
            mail_null.setText("항목을 입력하세요.");
            count++;
        } else {
            mail_null.setText("");
        }//end else

        if(String.valueOf(pass.getText()).equals("")) {
            pass_null.setText("항목을 입력하세요.");
            count++;
        } else {
            pass_null.setText("");
        }//end else

        if(String.valueOf(pass_check.getText()).equals("")) {
            pass_check_null.setText("항목을 입력하세요.");
            count++;
        } else {
            pass_check_null.setText("");
        }//end else

        return count;
    }//nullCheck

}//class

