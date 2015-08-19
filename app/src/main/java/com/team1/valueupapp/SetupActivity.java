package com.team1.valueupapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.ParseUser;

import java.io.File;

/**
 * Created by songmho on 2015-07-04.
 */
public class SetupActivity extends AppCompatActivity {
    Button logout;
    Switch swc_notice;
    Button btn_aboutpage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("설정");

        logout = (Button) findViewById(R.id.logout);
        swc_notice = (Switch) findViewById(R.id.swc_notice);
        btn_aboutpage = (Button) findViewById(R.id.btn_aboutpage);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                File file_up_path=new File("data/data/com.team1.valueupapp/files/");
                if (currentUser != null) {
                    File[] files = file_up_path.listFiles();
                    if(files!=null) {
                        for (File file : files) {
                            String fname = file.getName();
                            if (fname.equals("profile.jpg"))
                                file.delete();
                            ParseUser.getCurrentUser().remove("profile");
                        }
                    }
                    Toast.makeText(getApplicationContext(), "로그아웃되었습니다", Toast.LENGTH_SHORT).show();
                    currentUser.logOut();
                    startActivity(new Intent(SetupActivity.this, SplashActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext().getApplicationContext(), "로그인정보를 확인하세요", Toast.LENGTH_SHORT).show();
                }//end else
            }
        });//OnClickListener

        swc_notice.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton cb, boolean isChecking) {

                if (isChecking)
                    Toast.makeText(getApplicationContext(), "교육일정알림 설정", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "교육일정알림 해제", Toast.LENGTH_SHORT).show();
            }
        });

        btn_aboutpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://moa-tjdhksrb2.c9.io/"));
                startActivity(intent);
            }
        });
    }

}//class
