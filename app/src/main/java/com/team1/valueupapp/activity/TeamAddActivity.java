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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.team1.valueupapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hyemi on 2015-07-26.
 */
public class TeamAddActivity extends AppCompatActivity implements View.OnClickListener{            //동명이인 처리가능하게 변경해야 됨.

    EditText editTitle;
    EditText editDetail;
    Context mContext;

    /*RecyclerView recyclerView;*/
    /*RecyclerView.LayoutManager layoutManager;*/
//    ArrayList<TeamAddItem> items;
//    ProgressBar progressBar;
//    ArrayList<ParseUser> s;
    private List<String> arr_inter = new ArrayList<>();

    @Bind(R.id.bt_inter_1) Button bt_inter_1;
    @Bind(R.id.bt_inter_2) Button bt_inter_2;
    @Bind(R.id.bt_inter_3) Button bt_inter_3;
    @Bind(R.id.edit_inter) EditText edit_inter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_teamadd);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("그룹 만들기");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        editTitle = (EditText) findViewById(R.id.title);
        editDetail = (EditText) findViewById(R.id.detail);
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        progressBar = (ProgressBar) findViewById(R.id.progressbar);

//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(layoutManager);

        findViewById(R.id.btn_make_team).setOnClickListener(new View.OnClickListener() {        //팀생성버튼 클릭 시
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        ParseObject object = new ParseObject("Team");
                        object.put("idea", String.valueOf(editTitle.getText()));
                        object.put("idea_info", String.valueOf(editDetail.getText()));
                        object.put("admin_member", ParseUser.getCurrentUser());

                        object.getRelation("member").add(ParseUser.getCurrentUser());
                        object.saveInBackground();
                    }
                });
                startActivity(new Intent(TeamAddActivity.this, TeamActivity.class));
                finish();

                /*s = new ArrayList<>();
                for (TeamAddItem i : items) {
                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereEqualTo("objectId", i.getObjectId());
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(final List<ParseUser> list, ParseException e) {
                            s.add(list.get(0));
                        }
                    });
                }

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
                query.whereEqualTo("admin_member", ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (list.isEmpty()) {
                        ParseObject object = new ParseObject("Team");
                        object.put("idea", String.valueOf(editTitle.getText()));
                        object.put("idea_info", String.valueOf(editDetail.getText()));
                        object.put("admin_member", ParseUser.getCurrentUser());
                        ParseRelation<ParseUser> parseUsers = object.getRelation("member");
                        parseUsers.add(ParseUser.getCurrentUser());
                        object.saveInBackground();

                            for (ParseUser user : s) {
                                object.getRelation("member").add(user);
                                object.saveInBackground();
                            }
                        } else {
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).deleteInBackground();
                            }//end for
                            ParseObject object = new ParseObject("Team");
                            object.put("idea", String.valueOf(title.getText()));
                            object.put("idea_info", String.valueOf(detail.getText()));
                            object.put("admin_member", ParseUser.getCurrentUser());
                            ParseRelation<ParseUser> parseUsers = object.getRelation("member");
                            parseUsers.add(ParseUser.getCurrentUser());
                            object.saveInBackground();

                            for (ParseUser user : s) {
                                object.getRelation("member").add(user);
                                object.saveInBackground();
                            }
                        }*/
            }
        });
//
//        Intent intent = new Intent(TeamAddActivity.this, TeamMemberAddActivity.class);
//        startActivity(intent);
        bt_inter_1.setOnClickListener(this);
        bt_inter_2.setOnClickListener(this);
        bt_inter_3.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        items = new ArrayList<>();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressBar.setVisibility(View.VISIBLE);
//                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
//                        query.whereEqualTo("admin_member", ParseUser.getCurrentUser());
//                        query.findInBackground(new FindCallback<ParseObject>() {
//                            @Override
//                            public void done(List<ParseObject> list, ParseException e) {
//                                if (list.isEmpty()) {
//                                    title.setText(ParseUser.getCurrentUser().getString("info"));
//                                    detail.setText(ParseUser.getCurrentUser().getString("detail"));
//                                    TeamAddItem item = new TeamAddItem(null, ParseUser.getCurrentUser().getString("name"), ParseUser.getCurrentUser().getObjectId());
//                                    items.add(item);
//                                } else {
//                                    title.setText(list.get(0).getString("idea"));
//                                    detail.setText(list.get(0).getString("idea_info"));
//                                    ParseRelation<ParseUser> relation = list.get(0).getRelation("member");
//                                    relation.getQuery().findInBackground(new FindCallback<ParseUser>() {
//                                        @Override
//                                        public void done(List<ParseUser> list, ParseException e) {
//                                            for (int i = 0; i < list.size(); i++) {
//                                                TeamAddItem item = new TeamAddItem(null, list.get(i).getString("name"), list.get(i).getObjectId());
//                                                items.add(item);
//                                            }
//                                        }
//                                    });
//                                }//end else
////                                recyclerView.setAdapter(new TeamAddAdapter(getApplicationContext(), items));
//                                progressBar.setVisibility(View.GONE);
//
//                            }
//                        });
//                    }
//                });
//            }
//        }).start();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {/*
        getMenuInflater().inflate(R.menu.menu_teamadd, menu);
        MenuItem additem = menu.findItem(R.id.action_add);
        additem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
                query.whereEqualTo("admin_member", ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (!list.isEmpty()) {
                            ParseObject object = list.get(0);
//                          object.remove("idea");
                            object.put("idea", String.valueOf(title.getText()));
                            object.remove("idea_info");
                            object.put("idea_info", String.valueOf(detail.getText()));
                            object.saveInBackground();
                        } else {
                            ParseObject object = new ParseObject("Team");
                            object.put("idea", String.valueOf(title.getText()));
                            object.put("idea_info", String.valueOf(detail.getText()));
                            object.put("admin_member", ParseUser.getCurrentUser());

                            object.getRelation("member").add(ParseUser.getCurrentUser());
                            object.saveInBackground();
                        }
                    }
                });
                finish();
                return false;
            }
        });*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
                query.whereEqualTo("admin_member", ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        try {
                            if (!list.isEmpty())
                                list.get(0).delete();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
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
        builder.setTitle("Dialog");
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
        switch(v.getId()) {
            case R.id.bt_inter_1:                                               //관심사 태그에서 1번째
                if (!arr_inter.contains(bt_inter_1.getText().toString())) {      //리스트에 관심사 1번 태그가 없으면
                    arr_inter.add(bt_inter_1.getText().toString());             //리스트에 관심사 추가
                    edit_inter.append(bt_inter_1.getText().toString() + " ");   //edittext에 이어서 씀
                }
                break;
            case R.id.bt_inter_2:                                               //관심사 태그에서 2번째
                if (!arr_inter.contains(bt_inter_2.getText().toString())) {      //리스트에 관심사 2번 태그가 없으면
                    arr_inter.add(bt_inter_2.getText().toString());             //리스트에 관심사 추가
                    edit_inter.append(bt_inter_2.getText().toString() + " ");   //edittext에 이어서 씀
                }
                break;
            case R.id.bt_inter_3:                                               //관심사 태그에서 3번째
                if (!arr_inter.contains(bt_inter_3.getText().toString())) {      //리스트에 관심사 3번 태그가 없으면
                    arr_inter.add(bt_inter_3.getText().toString());             //리스트에 관심사 추가
                    edit_inter.append(bt_inter_3.getText().toString() + " ");   //edittext에 이어서 씀
                }
                break;
        }
    }
}//class
