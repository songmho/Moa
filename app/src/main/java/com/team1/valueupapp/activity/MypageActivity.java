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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.team1.valueupapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by eugene on 2015-08-08.
 */
public class MypageActivity extends AppCompatActivity {
    ParseUser parseUser;
    String str_job;
    ImageView profile_blur;
    CircleImageView profile;

    CollapsingToolbarLayout collapsing_toolbar;

    ImageView imageView;
    TextView str_info;

    @Bind(R.id.txt_info) TextView txtInfo;
    @Bind(R.id.txt_name) TextView txtName;
    @Bind(R.id.txt_tag) TextView txtTag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        imageView = (ImageView) findViewById(R.id.image);
        str_info = (TextView) findViewById(R.id.str_info);
        profile_blur = (ImageView) findViewById(R.id.profile_blur);
        profile = (CircleImageView) findViewById(R.id.profile);

        String tempPath = "data/data/com.team1.valueupapp/files/profile.jpg";
        Bitmap bm = BitmapFactory.decodeFile(tempPath);
        if (bm != null) {
            profile_blur.setImageBitmap(blur(getApplicationContext(), bm, 20));
            profile.setImageBitmap(bm);
        } else {
            bm = BitmapFactory.decodeResource(getResources(), R.drawable.img_page);
            profile_blur.setImageBitmap(blur(getApplicationContext(), bm, 20));
            profile.setImageResource(R.drawable.ic_user);
        }
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
    protected void onResume() {
        super.onResume();
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
        MenuItem editItem = menu.findItem(R.id.action_edit);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(MypageActivity.this, MyPageEditActivity.class);
            intent.putExtra("name", txtName.getText().toString());
            intent.putExtra("myinfo", txtInfo.getText().toString());
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