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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.team1.valueupapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by eugene on 2015-08-08.
 */
public class MypageActivity extends AppCompatActivity implements View.OnClickListener {
    ParseUser parseUser;
    String profileUrl = null;

    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.app_bar) AppBarLayout appBarLayout;
    @Bind(R.id.txt_info) TextView txtInfo;
    @Bind(R.id.txt_name) TextView txtName;
    @Bind(R.id.txt_tag) TextView txtTag;
    @Bind(R.id.profile) CircleImageView profile;
    @Bind(R.id.profile_blur) ImageView profileBlur;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.bt_sndMsg) Button bt_sndMsg;

    Context mContext;
    private static final String TAG = "MypageActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_mypage);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        parseUser = ParseUser.getCurrentUser();
        initAppBarLayout();
        loadProfile();

        bt_sndMsg.setOnClickListener(this);
    }

    //앱바 레이아웃 설정
    private void initAppBarLayout() {
        collapsingToolbarLayout.setTitle("");
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
                    collapsingToolbarLayout.setTitle("마이페이지");
                    profile.setVisibility(View.GONE);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle("");
                    profile.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });
    }

    //프로필 파일을 url로부터 로딩한다.
    private void loadProfile() {
        if (parseUser != null) {
            ParseFile parseFile = (ParseFile) parseUser.get("profile");
            if (parseFile != null) {
                profileUrl = parseFile.getUrl();
                Log.e(TAG, "parse file url : " + profileUrl);
                if (!isFinishing()) {
                    Glide.with(mContext).load(profileUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(profile);
                    Glide.with(mContext).load(profileUrl).diskCacheStrategy(DiskCacheStrategy.RESULT).bitmapTransform(new BlurTransformation(mContext)).into(profileBlur);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfile();
        if (parseUser == null)
            parseUser = ParseUser.getCurrentUser();
        txtName.setText(parseUser.getString("name"));
        txtInfo.setText(parseUser.getString("info"));
        try {
            String strTag = "";
            JSONArray tagArray = parseUser.getJSONArray("tag");
            for (int i = 0; i < tagArray.length(); i++) {
                if (!tagArray.getString(i).equals(""))
                    strTag += "#" + tagArray.getString(i) + " ";
            }
            txtTag.setText(strTag);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(MypageActivity.this, MyPageEditActivity.class);
            intent.putExtra("name", txtName.getText().toString());
            intent.putExtra("myInfo", txtInfo.getText().toString());
            intent.putExtra("tag", txtTag.getText().toString());
            intent.putExtra("profileUrl", profileUrl);

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

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.bt_sndMsg){
            startActivity(new Intent(this,SndMsgActivity.class));
        }
    }
}