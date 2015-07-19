package com.team1.valueupapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

/**
 * Created by hyemi on 2015-06-29.
 */
public class SplashActivity extends Activity {
    int SPLASH_TIME = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, loginActivity.class));
                    finish();
                }//run

            }, SPLASH_TIME);
    }//onCreate

}//class