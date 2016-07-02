package com.team1.valueupapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.team1.valueupapp.R;
import com.team1.valueupapp.adapter.TeamAddAdapter;
import com.team1.valueupapp.item.TeamAddItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hyemi on 2015-07-26.
 */
public class TeamEditActivity extends AppCompatActivity implements View.OnClickListener {            //동명이인 처리가능하게 변경해야 됨.

    @Bind(R.id.btn_tag_1) Button btnTag1;
    @Bind(R.id.btn_tag_2) Button btnTag2;
    @Bind(R.id.btn_tag_3) Button btnTag3;
    @Bind(R.id.btn_make_team)
    TextView btnMakeTeam;
    @Bind(R.id.edit_tag) EditText editTag;
    @Bind(R.id.title) EditText editTitle;
    @Bind(R.id.detail) EditText editDetail;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.btn_remove_team) TextView btn_remove_team;

    public static final int RESULT_EDIT_FINISH = 53;
    Context mContext;
    Intent intent;
    private List<String> arrTags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_teamadd);
        ButterKnife.bind(this);


        intent = getIntent();
        Log.d("fdfdfd",intent.getStringExtra("title"));

        toolbar.setTitle("그룹 정보 수정");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTitle.setText(intent.getStringExtra("title"));
        editDetail.setText(intent.getStringExtra("detail"));
        editTag.setText(intent.getStringExtra("tag"));

        ParseQuery<ParseObject> query=ParseQuery.getQuery("Team");
        query.whereEqualTo("objectId",intent.getStringExtra("objId"));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                JSONArray a = parseObject.getJSONArray("tag");
                for(int i = 0 ;i<a.length();i++) {
                    try {
                        arrTags.add("#"+a.getString(i));
                        Log.d("asdfadsfasdf",arrTags.get(i));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        btnMakeTeam.setVisibility(View.GONE);
        btn_remove_team.setOnClickListener(this);
        btnTag1.setOnClickListener(this);
        btnTag2.setOnClickListener(this);
        btnTag3.setOnClickListener(this);

    }//onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_teamadd, menu);
      return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_add:
                if (editTitle.getText().length() < 1) {
                    Toast.makeText(mContext, "그룹 간략 소개를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (editDetail.getText().length() < 1) {
                    Toast.makeText(mContext, "그룹 상세 설명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (editTag.getText().length() < 1) {
                    Toast.makeText(mContext, "그룹 관심사를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (editTitle.getText().length() > 15) {
                    Toast.makeText(mContext, "그룹 간략 소개는 15자 이내로 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    ParseQuery<ParseObject> object = ParseQuery.getQuery("Team");
                    object.whereEqualTo("objectId",intent.getStringExtra("objId"));
                    object.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            parseObject.put("intro", String.valueOf(editTitle.getText()));
                            parseObject.put("intro_detail", String.valueOf(editDetail.getText()));
                            int i=0;
                            for (String s : arrTags) {
                                arrTags.set(i,s.replace("#",""));
                                i++;
                            }
                            parseObject.put("tag", arrTags);
                            parseObject.saveInBackground();
                            Toast.makeText(mContext, "변경 되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    String strTag= "";
                    for (String a : arrTags) {
                            if (!a.equals(""))
                                strTag += "#" + a + " ";
                    }

                    Intent i = new Intent(TeamEditActivity.this,TeamDetailActivity.class);
                    i.putExtra("objId",intent.getStringExtra("objId"));
                    i.putExtra("title", String.valueOf(editTitle.getText()));
                    i.putExtra("name", ParseUser.getCurrentUser().getString("name"));
                    i.putExtra("username", ParseUser.getCurrentUser().getUsername());
                    i.putExtra("detail", String.valueOf(editDetail.getText()));
                    i.putExtra("tag",strTag);
                    setResult(RESULT_EDIT_FINISH,i);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
            case R.id.btn_remove_team:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert");
                builder.setMessage("그룹을 삭제하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParseQuery<ParseObject> query=ParseQuery.getQuery("Team");
                        query.whereEqualTo("objectId",intent.getStringExtra("objId"));
                        query.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                parseObject.put("isVisible",false);
                                parseObject.saveInBackground();
                            }
                        });
                        TeamDetailActivity activity = (TeamDetailActivity) TeamDetailActivity.activity;
                        activity.finish();
                        finish();
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;

        }
    }
}//class
