package com.team1.valueupapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.team1.valueupapp.R;
import com.team1.valueupapp.adapter.TeamRecyclerAdapter;
import com.team1.valueupapp.item.TeamItem;
import com.team1.valueupapp.item.UserItem;

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
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsing_toolbar;
    @Bind(R.id.profile_blur) ImageView profileBlur;
    @Bind(R.id.profile) CircleImageView profile;

    ParseUser parseUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String name = getIntent().getStringExtra("name");       //이름
        String info = getIntent().getStringExtra("info");       //소개
        final String username = getIntent().getStringExtra("username");     //유저네임(이메일)
        txtName.setText(name);
        txtInfo.setText(info);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                parseQuery.whereEqualTo("username", username);
                parseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        if (!list.isEmpty()) {
                            if (list.get(0).getJSONArray("tag") != null) {  //태그가 존재할 때
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

    public Bitmap blur(Context context, Bitmap sentBitmap, int radius) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius); //0.0f ~ 25.0f
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }
        return null;
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