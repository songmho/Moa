package com.team1.valueupapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by eugene on 2015-07-18.
 */
public class loginActivity extends Activity {
    AppCompatEditText id_text;
    AppCompatEditText pass_text;
    Button login_btn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        id_text= (AppCompatEditText) findViewById(R.id.id_text);
        pass_text= (AppCompatEditText) findViewById(R.id.pass_text);
        login_btn= (Button) findViewById(R.id.login_btn);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.VISIBLE);
                                ParseUser.logInInBackground(String.valueOf(id_text.getText()), String.valueOf(pass_text.getText()), new LogInCallback() {
                                    @Override
                                    public void done(ParseUser user, ParseException e) {
                                        progressBar.setVisibility(View.GONE);
                                        if(user != null){
                                            startActivity(new Intent(loginActivity.this, MainActivity.class));
                                            finish();
                                        }else{
                                            Toast.makeText(getApplicationContext(),"틀렸습니다.",Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });
                            }
                        });
                    }
                }).start();

            }
        });

    }
}
