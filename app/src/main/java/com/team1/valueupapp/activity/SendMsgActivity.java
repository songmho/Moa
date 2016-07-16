package com.team1.valueupapp.activity;

import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by songmho on 16. 6. 27.
 */
public class SendMsgActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.receiver_name) TextView recvName;
    @Bind(R.id.bt_choose) Button btChoose;
    @Bind(R.id.text) EditText text;
    @Bind(R.id.bt_send) Button btSend;

    String recvObjId;
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

        btChoose.setText("선택");
        btChoose.setOnClickListener(this);
        btSend.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==19000)
            if(resultCode==RESULT_OK){
                recvName.setText(data.getStringExtra("name")+" ("+data.getStringExtra("eMail")+")");
                recvObjId =data.getStringExtra("objectId");
                btChoose.setText("다시 선택");
            }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.bt_choose){
            Intent i=new Intent(this,MemberActivity.class);
            startActivityForResult(i, 19000);
        }
        else if(v.getId()==R.id.bt_send){
            ParseObject msg=new ParseObject("message");
            try {
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
                msg.put("user_from", ParseUser.getCurrentUser());
                msg.put("user_to",ParseUser.getQuery().get(recvObjId));
                msg.put("text",text.getText().toString());
                msg.put("sendDate",format.format(date));
                msg.saveInBackground();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Toast.makeText(SendMsgActivity.this, "쪽지를 전송했습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
