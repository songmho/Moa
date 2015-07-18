package com.team1.valueupapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by hyemi on 2015-06-29.
 */
public class SplashActivity extends Activity {
    int SPLASH_TIME = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    SharedPreferences pref=getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor=pref.edit();
                    editor.putInt("count",1);
                    editor.commit();
                    try {
                        ParseUser.logIn("songmho@gmail.com","2013253070");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(SplashActivity.this, loginActivity.class));
                    finish();
                }//run

            }, SPLASH_TIME);
    }//onCreate

}//class