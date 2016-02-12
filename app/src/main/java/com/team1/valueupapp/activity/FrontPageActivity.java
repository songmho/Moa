package com.team1.valueupapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseUser;
import com.team1.valueupapp.R;

/**
 * Created by knulps on 16. 1. 30..
 * 첫페이지
 */
public class FrontPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_activity_front);

        //1. 팀 만들기
        findViewById(R.id.btn_make_team).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ParseUser.getCurrentUser() != null) {
                    startActivity(new Intent(FrontPageActivity.this, TeamAddActivity.class));

                } else {
                    Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FrontPageActivity.this, LoginActivity.class));
                    finish();
                }

            }
        });

        //2. 둘러보기
        findViewById(R.id.btn_look_around).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FrontPageActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
