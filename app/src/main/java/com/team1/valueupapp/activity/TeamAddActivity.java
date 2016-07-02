package com.team1.valueupapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.team1.valueupapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hyemi on 2015-07-26.
 */
public class TeamAddActivity extends AppCompatActivity implements View.OnClickListener {            //동명이인 처리가능하게 변경해야 됨.

    Context mContext;
    private List<String> arrTags = new ArrayList<>();

    @Bind(R.id.btn_tag_1) Button btnTag1;
    @Bind(R.id.btn_tag_2) Button btnTag2;
    @Bind(R.id.btn_tag_3) Button btnTag3;
    @Bind(R.id.btn_make_team) TextView btnMakeTeam;
    @Bind(R.id.edit_tag) EditText editTag;
    @Bind(R.id.title) EditText editTitle;
    @Bind(R.id.detail) EditText editDetail;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.btn_remove_team) TextView btn_remove_team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_teamadd);
        ButterKnife.bind(this);
        toolbar.setTitle("그룹 만들기");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_remove_team.setVisibility(View.GONE);
        btnMakeTeam.setOnClickListener(this);
        btnTag1.setOnClickListener(this);
        btnTag2.setOnClickListener(this);
        btnTag3.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (editTitle.getText() != null && editTitle.getText().length() > 0
                        || editDetail.getText() != null && editDetail.getText().length() > 0) {
                    quitAlert();
                } else {
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (editTitle.getText() != null && editTitle.getText().length() > 0
                        || editDetail.getText() != null && editDetail.getText().length() > 0) {
                    quitAlert();
                } else {
                    finish();
                }
                break;
        }

        return true;
    }

    //백버튼 누르면 종료 여부를 묻는 다이얼로그
    public void quitAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("작성중인 내용을 취소하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tag_1:                                               //관심사 태그에서 1번째
                if (!arrTags.contains(btnTag1.getText().toString())) {      //리스트에 관심사 1번 태그가 없으면
                    arrTags.add(btnTag1.getText().toString());             //리스트에 관심사 추가
                    editTag.append(btnTag1.getText().toString() + " ");   //edittext에 이어서 씀
                }
                break;
            case R.id.btn_tag_2:                                               //관심사 태그에서 2번째
                if (!arrTags.contains(btnTag2.getText().toString())) {      //리스트에 관심사 2번 태그가 없으면
                    arrTags.add(btnTag2.getText().toString());             //리스트에 관심사 추가
                    editTag.append(btnTag2.getText().toString() + " ");   //edittext에 이어서 씀
                }
                break;
            case R.id.btn_tag_3:                                               //관심사 태그에서 3번째
                if (!arrTags.contains(btnTag3.getText().toString())) {      //리스트에 관심사 3번 태그가 없으면
                    arrTags.add(btnTag3.getText().toString());             //리스트에 관심사 추가
                    editTag.append(btnTag3.getText().toString() + " ");   //edittext에 이어서 씀
                }
                break;
            case R.id.btn_make_team:
                if (editTitle.getText().length() < 1) {
                    Toast.makeText(mContext, "그룹명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (editDetail.getText().length() < 1) {
                    Toast.makeText(mContext, "그룹 상세 설명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (editTag.getText().length() < 1) {
                    Toast.makeText(mContext, "그룹 관심사를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (editTitle.getText().length() > 15) {
                    Toast.makeText(mContext, "그룹명은 15자 이내로 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    ParseObject object = new ParseObject("Team");
                    object.put("intro", String.valueOf(editTitle.getText()));
                    object.put("intro_detail", String.valueOf(editDetail.getText()));
                    object.put("admin_member", ParseUser.getCurrentUser());
                    if (arrTags != null) {
                        arrTags.clear();
                    }
                    String[] arr_s = editTag.getText().toString().split("#");
                    for (String s : arr_s) {
                        if (!s.equals(""))
                            arrTags.add(s.trim());
                    }
                    object.put("tag", arrTags);
                    object.put("isVisible",true);
                    object.getRelation("member").add(ParseUser.getCurrentUser());
                    object.saveInBackground();
                    setResult(RESULT_OK, new Intent());
                    Toast.makeText(mContext, "그룹이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
}//class
