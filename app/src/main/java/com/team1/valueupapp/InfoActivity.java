package com.team1.valueupapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by songmho on 2015-07-04.
 */
public class InfoActivity extends AppCompatActivity {
    FloatingActionButton fab;
    TextView mydetail;
    Intent intent;
    Button memobutton;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        intent=getIntent();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsing_toolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        TextView myjob=(TextView)findViewById(R.id.myjob);
//        TextView title=(TextView)findViewById(R.id.info);
        TextView myinfo=(TextView)findViewById(R.id.myinfo);
        mydetail=(TextView)findViewById(R.id.mydetail);
        memobutton=(Button)findViewById(R.id.memobutton);

        fab=(FloatingActionButton)findViewById(R.id.fab);




        if(intent.getBooleanExtra("star",true ))
            fab.setImageResource(R.drawable.ic_check_white);
        else
            fab.setImageResource(R.drawable.add);


        collapsing_toolbar.setTitle(intent.getStringExtra("name"));

        switch (intent.getIntExtra("cur_job",0)){      //직종과 아이디어 및 스킬 텍스트 설정
            case 0:
                myjob.setText("기획자");
                myjob.setTextColor(getResources().getColor(R.color.tab_color));
                myjob.setBackgroundColor(getResources().getColor(R.color.planner));
                myjob.setPadding(30, 15, 30, 15);
//                title.setText("아이디어");
                break;
            case 1:
                myjob.setText("개발자");
                myjob.setTextColor(getResources().getColor(R.color.tab_color));
                myjob.setBackgroundColor(getResources().getColor(R.color.developer));
                myjob.setPadding(30, 15, 30, 15);

//                title.setText("스킬");
                break;
            case 2:
                myjob.setText("디자이너");
                myjob.setTextColor(getResources().getColor(R.color.tab_color));
                myjob.setBackgroundColor(getResources().getColor(R.color.designer));
                myjob.setPadding(30, 15, 30, 15);
//                title.setText("스킬");
                break;
        }
        String idea=intent.getStringExtra("idea");
            if(intent.getIntExtra("cur_job",0)!=0) {
                idea = intent.getStringExtra("idea").replaceAll(", ", "\n ");
                idea = " " + idea;
            }
        myinfo.setText(idea);

        loadingData(intent, 0);     //detail 불러오기

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingData(intent, 1);
            }
        });
        memobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(InfoActivity.this);
                final EditText input = new EditText(InfoActivity.this);
                input.setSingleLine();
                FrameLayout container = new FrameLayout(InfoActivity.this);
                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                input.setLayoutParams(params);
                container.addView(input);
                alert.setTitle("메모를 입력하세요");
                alert.setView(container);
                params.setMargins(50,0,50,0);
                input.setLayoutParams(params);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString().trim();
                    }
                });

                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                alert.show();


               /* AlertDialog dialog = create_inputDialog();
                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.popup_memo, (ViewGroup)findViewById(R.id.popup_root));
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                dialog.setView(layout);
                dialog.show();*/
             //   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

            }
        });


    }

    private void loadingData(Intent intent, final int action) {
        ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
        parseQuery.whereEqualTo("name",intent.getStringExtra("name"));
        parseQuery.whereEqualTo("info",intent.getStringExtra("idea"));
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                for (int i=0;i<list.size();i++) {
                    final ParseObject parseObject = list.get(i);
                    if(action==0)
                        mydetail.setText(parseObject.getString("detail"));
                    else
                        fab_clicked(parseObject);
                }
            }
        });
    }

    private void fab_clicked(final ParseObject parseObject) {
        View container=findViewById(R.id.container);

        if (ParseUser.getCurrentUser().getList("pick").contains(intent.getStringExtra("name"))) {

            Snackbar snackbar=Snackbar.make(container,"관심멤버에서 제외합니다.",Snackbar.LENGTH_LONG);
            ParseUser.getCurrentUser().getList("pick").remove(intent.getStringExtra("name"));
            ParseUser.getCurrentUser().saveInBackground();
            fab.setImageResource(R.drawable.add);

            snackbar.setAction("실행취소", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseUser.getCurrentUser().getList("pick").add(intent.getStringExtra("name"));
                    ParseUser.getCurrentUser().saveInBackground();
                    fab.setImageResource(R.drawable.ic_check_white);
                }
            });
            snackbar.show();

        } else {
            Snackbar snackbar=Snackbar.make(container,"관심멤버에 추가합니다.",Snackbar.LENGTH_LONG);
            ParseUser.getCurrentUser().getList("pick").add(intent.getStringExtra("name"));
            ParseUser.getCurrentUser().saveInBackground();
            fab.setImageResource(R.drawable.ic_check_white);
            snackbar.setAction("실행취소", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseUser.getCurrentUser().getList("pick").remove(intent.getStringExtra("name"));
                    ParseUser.getCurrentUser().saveInBackground();
                    fab.setImageResource(R.drawable.add);
                }
            });
            snackbar.show();
        }
    }
    private AlertDialog create_inputDialog() {
        AlertDialog dialogBox = new AlertDialog.Builder(this)
                .setTitle("메모를 입력하세요")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        return dialogBox;
    }
}
