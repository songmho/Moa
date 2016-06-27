package com.team1.valueupapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.team1.valueupapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by songmho on 16. 6. 27.
 */
public class SndMsgActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.receiver_name) TextView recv_name;
    @Bind(R.id.bt_choose) Button bt_choose;
    @Bind(R.id.text) EditText text;
    @Bind(R.id.bt_send) Button bt_send;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snd_msg);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("쪽지보내기");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_choose.setOnClickListener(this);
        bt_send.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.bt_choose){


        }
        else if(v.getId()==R.id.bt_send){
            ParseObject msg=new ParseObject("message");
            try {
                msg.put("user_from", ParseUser.getCurrentUser());
                msg.put("user_to",ParseUser.getQuery().get("KEL34NrfVa"));
                msg.put("text",text.getText().toString());
                msg.saveInBackground();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Toast.makeText(SndMsgActivity.this, "쪽지를 전송했습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
