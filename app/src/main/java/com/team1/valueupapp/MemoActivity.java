package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.List;

/**
 * Created by hyemi on 2015-08-20.
 */
public class MemoActivity extends AppCompatActivity{
    Intent intent;
    EditText memo_input;
    Button input_button;
    boolean flag=false;
    int flag_num=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("메모");

        intent = getIntent();

        memo_input = (EditText) findViewById(R.id.memo_input);
        input_button = (Button) findViewById(R.id.input_button);

        final List<String> memo_owner = ParseUser.getCurrentUser().getList("memo_owner");
        final List<String> memo_list = ParseUser.getCurrentUser().getList("memo");

        for (int i = 0; i < memo_owner.size(); i++) {
            if (memo_owner.get(i).equals(intent.getStringExtra("name"))) {
                memo_input.setText(memo_list.get(i));
                flag = true;
                flag_num=i;
            }//end if
        }//end for

        input_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = memo_input.getText().toString().trim();

                if (ParseUser.getCurrentUser().getList("pick").contains(intent.getStringExtra("name"))) {
                    ParseUser.getCurrentUser().getList("pick").remove(intent.getStringExtra("name"));
                    ParseUser.getCurrentUser().saveInBackground();
                }//end for

                if (flag == true) {
                    ParseUser.getCurrentUser().getList("memo_owner").remove(intent.getStringExtra("name"));
                    ParseUser.getCurrentUser().getList("memo").remove(memo_list.get(flag_num));
                    ParseUser.getCurrentUser().saveInBackground();
                }//이미 memo가 있다면 메모 삭제

                if(ParseUser.getCurrentUser()!=null) {
                    ParseUser.getCurrentUser().getList("memo_owner").add(intent.getStringExtra("name"));
                    ParseUser.getCurrentUser().getList("pick").add(intent.getStringExtra("name"));
                    ParseUser.getCurrentUser().getList("memo").add(value);
                    ParseUser.getCurrentUser().saveInBackground();

                    Toast.makeText(getApplicationContext(), "관심멤버에 추가되었습니다..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "로그인 정보를 확인하세요.", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });//input_button.setOnClickListener

    }

}//class
