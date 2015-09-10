package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
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
    ParseUser user;

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

        Log.d("iff", intent.getStringExtra("name"));
        Log.d("iff", intent.getStringExtra("idea"));

        final ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
        parseQuery.whereEqualTo("name", intent.getStringExtra("name"));
        parseQuery.whereEqualTo("info", intent.getStringExtra("idea"));
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, com.parse.ParseException e) {
                for (int i = 0; i < list.size(); i++) {
                    user = list.get(i);
                }//end for
            }
        });

        Log.d("iff", "ffffffffffffffffff");

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

                if (flag == true) {
                    ParseUser.getCurrentUser().getList("memo_owner").remove(intent.getStringExtra("name"));
                    ParseUser.getCurrentUser().getList("memo").remove(memo_list.get(flag_num));
                    ParseUser.getCurrentUser().saveInBackground();
                }//이미 memo가 있다면 메모 삭제

                final ParseRelation<ParseUser> relation = ParseUser.getCurrentUser().getRelation("my_pick");
                ParseQuery<ParseUser> query = relation.getQuery();
                query.whereContains("objectId", user.getObjectId());
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, com.parse.ParseException e) {
                        if (list.isEmpty()) {
                            relation.add(user);
                            ParseUser.getCurrentUser().saveInBackground();
                        }
                    }
                });

                ParseUser.getCurrentUser().getList("memo_owner").add(intent.getStringExtra("name"));
                ParseUser.getCurrentUser().getList("memo").add(value);
                ParseUser.getCurrentUser().saveInBackground();

                ParseQuery<ParseObject> picked_query = ParseQuery.getQuery("Picked");
                picked_query.whereEqualTo("user", user);
                picked_query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, com.parse.ParseException e) {
                        if (!list.isEmpty()) {
//                                Log.d("sss",list.size()+"");
                            ParseRelation<ParseUser> picked_relation = list.get(0).getRelation("picked");
                            picked_relation.add(ParseUser.getCurrentUser());
                            list.get(0).saveInBackground();
                        }
                    }
                });

                Toast.makeText(getApplicationContext(), "관심멤버에 추가되었습니다..", Toast.LENGTH_SHORT).show();

                finish();
            }
        });//input_button.setOnClickListener

    }

}//class
