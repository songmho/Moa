package com.team1.valueupapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.team1.valueupapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by eugene on 2015-08-08.
 */
public class UserDetailActivity extends AppCompatActivity {
    @Bind(R.id.txt_info) TextView txtInfo;
    @Bind(R.id.txt_name) TextView txtName;
    @Bind(R.id.txt_tag) TextView txtTag;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.app_bar) AppBarLayout appBarLayout;
    @Bind(R.id.profile_blur) ImageView profileBlur;
    @Bind(R.id.profile) CircleImageView profile;
    @Bind(R.id.progressbar) ProgressBar progressBar;
    @Bind(R.id.toolbar) Toolbar toolbar;
    Context mContext;

    private static final String TAG = "UserDetailActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        ButterKnife.bind(this);
        mContext = this;
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String name = getIntent().getStringExtra("name");       //이름
        String info = getIntent().getStringExtra("info");       //소개
        final String username = getIntent().getStringExtra("username");     //유저네임(이메일)
        txtName.setText(name);
        txtInfo.setText(info);

        initAppBarLayout(); //앱바 초기화
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                parseQuery.whereEqualTo("username", username);
                parseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        progressBar.setVisibility(View.GONE);
                        if (!list.isEmpty()) {
                            ParseUser user = list.get(0);
                            ParseFile parseFile = user.getParseFile("profile");
                            //프로필 파일을 url로부터 로딩한다.
                            if (parseFile != null) {
                                String url = parseFile.getUrl();
                                Log.e(TAG, "parse file url : " + url);
                                if (!isFinishing())
                                    Glide.with(mContext).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(profile);
                            }

                            if (user.getJSONArray("tag") != null) {  //태그가 존재할 때
                                String strTag = "";
                                JSONArray tagArray = list.get(0).getJSONArray("tag");       //태그를 JsonArray형식으로 가져옴
                                for (int i = 0; i < tagArray.length(); i++) {
                                    try {
                                        if (!tagArray.getString(i).equals(""))
                                            strTag += "#" + tagArray.getString(i) + " ";
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                txtTag.setText(strTag);
                            } else        //리스트가 비어있거나(리스트가 비어있을 경우에 대한 예외처리 필요) 태그가 등록 되어 있지 않으면
                                txtTag.setText("");
                        }
                    }
                });
            }
        });

    }

    //앱바 레이아웃 설정
    private void initAppBarLayout() {
        collapsingToolbarLayout.setTitle("멤버 정보");
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.e(TAG, "verticallOffset : " + verticalOffset + " , scrollRange + verticalOffset : " + (scrollRange + verticalOffset));
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    profile.setVisibility(View.GONE);
                    isShow = true;
                } else if (isShow) {
                    profile.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(UserDetailActivity.this, MyPageEditActivity.class);
            intent.putExtra("name", txtName.getText().toString());
            intent.putExtra("myInfo", txtInfo.getText().toString());
            intent.putExtra("tag", txtTag.getText().toString());
            startActivity(intent);
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}