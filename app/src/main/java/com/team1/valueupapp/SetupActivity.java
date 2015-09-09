package com.team1.valueupapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.io.File;
import java.util.List;

/**
 * Created by songmho on 2015-07-04.
 */
public class SetupActivity extends AppCompatActivity {
    Button logout;
    Switch swc_notice;
    Button btn_aboutpage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("설정");

        logout = (Button) findViewById(R.id.logout);
        swc_notice = (Switch) findViewById(R.id.swc_notice);
        btn_aboutpage = (Button) findViewById(R.id.btn_aboutpage);
        TextView last_ver=(TextView)findViewById(R.id.last_ver);
        TextView cur_ver=(TextView)findViewById(R.id.cur_ver);
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        cur_ver.setText(info.versionName);
        last_ver.setText(info.versionName);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                File file_up_path=new File("data/data/com.team1.valueupapp/files/");
                if (currentUser != null) {
                    File[] files = file_up_path.listFiles();
                    if(files!=null) {
                        for (File file : files) {
                            String fname = file.getName();
                            if (fname.equals("profile.jpg"))
                                file.delete();
                            ParseUser.getCurrentUser().remove("profile");
                        }
                    }
                    Toast.makeText(getApplicationContext(), "로그아웃되었습니다", Toast.LENGTH_SHORT).show();
                    currentUser.logOut();
                } else {
                    Toast.makeText(getApplicationContext().getApplicationContext(), "로그인정보를 확인하세요", Toast.LENGTH_SHORT).show();
                }//end else
            }
        });//OnClickListener

        swc_notice.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton cb, boolean isChecking) {

                if (isChecking)
                    Toast.makeText(getApplicationContext(), "교육일정알림 설정", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "교육일정알림 해제", Toast.LENGTH_SHORT).show();
            }
        });

        btn_aboutpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("http://tjdhksrb2.wix.com/essage"));
//                startActivity(intent);
                ParseQuery<ParseObject> picked_query = ParseQuery.getQuery("Picked");
                picked_query.whereEqualTo("user", ParseUser.getCurrentUser());
                picked_query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
//                        Log.d("set", list.size()+"");
                        ParseRelation<ParseUser> picked_relation = list.get(0).getRelation("picked");
                        picked_relation.getQuery().findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> list, ParseException e) {
                                int size;
                                if (list.isEmpty()) {
                                    size = 0;
                                } else {
                                    size = list.size();
                                }//end else
                                Log.d("set", size+"");
                            }
                        });
                    }
                });

            }
        });
    }

}//class
