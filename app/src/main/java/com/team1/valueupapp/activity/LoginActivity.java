package com.team1.valueupapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.team1.valueupapp.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eugene on 2015-07-18.
 */
public class LoginActivity extends AppCompatActivity {
    @Bind(R.id.id_text) EditText txtId;
    @Bind(R.id.pass_text) EditText txtPassword;
    @Bind(R.id.login_btn) Button btnLogin;
    @Bind(R.id.signup_btn) Button btnSignUp;
    @Bind(R.id.progressbar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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
                                            if (user != null) {
                                                ParseFile parse_file = (ParseFile) ParseUser.getCurrentUser().get("profile");

                                                if (parse_file != null) {
                                                    try {
                                                        byte[] bytes;
                                                        bytes = parse_file.getData();
                                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                        FileOutputStream fos = openFileOutput("profile.jpg", 0);
                                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                                        fos.flush();
                                                        fos.close();
                                                    } catch (ParseException e1) {
                                                        e1.printStackTrace();
                                                    } catch (FileNotFoundException e1) {
                                                        e1.printStackTrace();
                                                    } catch (IOException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                }
                                                progressBar.setVisibility(View.GONE);
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                finish();
                                            } else {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "틀렸습니다.", Toast.LENGTH_SHORT).show();
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
