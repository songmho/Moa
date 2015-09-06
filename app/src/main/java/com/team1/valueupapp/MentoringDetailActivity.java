package com.team1.valueupapp;

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
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hyemi on 2015-08-05.
 */
public class MentoringDetailActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_mentoring_detail);

        final Intent intent=getIntent();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsing_toolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsing_toolbar.setTitle(intent.getStringExtra("title"));

        TextView mentor = (TextView) findViewById(R.id.mentor);
        TextView date = (TextView) findViewById(R.id.date);
        TextView venue = (TextView) findViewById(R.id.venue);
        TextView mentoring_info = (TextView) findViewById(R.id.mentoring_info);
        ImageView imageView=(ImageView)findViewById(R.id.imageView);

        Bitmap bm;
        if(intent.getStringExtra("job").equals("개발자")) {
            bm = BitmapFactory.decodeResource(getResources(), R.drawable.img_programming);
            imageView.setImageBitmap(blur(getApplicationContext(), bm, 20));
        }
        else if(intent.getStringExtra("job").equals("디자이너")) {
            bm = BitmapFactory.decodeResource(getResources(), R.drawable.img_design);
            imageView.setImageBitmap(blur(getApplicationContext(), bm, 20));
        }
        else {
            bm = BitmapFactory.decodeResource(getResources(), R.drawable.img_planning);
            imageView.setImageBitmap(blur(getApplicationContext(), bm, 20));
        }
        mentor.setText(intent.getStringExtra("mentor"));
        date.setText(intent.getStringExtra("date"));
        venue.setText(intent.getStringExtra("venue"));
        mentoring_info.setText(intent.getStringExtra("detail"));

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

}//class
