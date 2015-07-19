package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by songmho on 2015-07-04.
 */
public class InfoActivity extends ActionBarActivity {
    Toolbar toolbar;
    Boolean ischeck;

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
        final Intent intent=getIntent();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(intent.getStringExtra("name"));

        TextView idea=(TextView)findViewById(R.id.idea);
        TextView detail=(TextView)findViewById(R.id.detail);
        final ImageButton star=(ImageButton)findViewById(R.id.star);
        CircleImageView profile=(CircleImageView)findViewById(R.id.profile);


        //ImageView
        idea.setText(intent.getStringExtra("idea"));
        detail.setText(intent.getStringExtra("detail"));
        star.setSelected(intent.getBooleanExtra("star", false));
        profile.setImageResource(intent.getIntExtra("profile",R.drawable.splash_logo));
        ischeck=intent.getBooleanExtra("star",false);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query=ParseQuery.getQuery("ValueUp_people");
                query.whereEqualTo("name",intent.getStringExtra("name"));
                query.whereEqualTo("info",intent.getStringExtra("idea"));
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        for (int i=0;i<list.size();i++) {
                            ParseObject parseObject = list.get(i);
                            if (parseObject.getList("pick").contains(ParseUser.getCurrentUser().get("name"))) {
                                star.setSelected(false);
                                Toast.makeText(getApplicationContext(), "관심멤버에서 제외합니다.", Toast.LENGTH_SHORT).show();
                                parseObject.getList("pick").remove(ParseUser.getCurrentUser().get("name"));
                            } else {
                                star.setSelected(true);
                                Toast.makeText(getApplicationContext(), "관심멤버에 추가합니다.", Toast.LENGTH_SHORT).show();
                                parseObject.getList("pick").add(ParseUser.getCurrentUser().get("name"));
                            }
                            parseObject.saveInBackground();
                        }
                    }
                });

            }
        });

    }
}
