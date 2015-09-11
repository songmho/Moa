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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by songmho on 2015-07-04.
 */
public class InfoActivity extends AppCompatActivity {
    FloatingActionButton fab;
    TextView mydetail;
    Intent intent;
    Button memobutton;
    TextView myjob;
    TextView myinfo;
    TextView mymemo;
    TextView str_info;
    CollapsingToolbarLayout collapsing_toolbar;
    ImageView profile_blur;
    CircleImageView profile;
    ParseUser user;
    ParseObject parseObject;





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

         collapsing_toolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
         myjob=(TextView)findViewById(R.id.myjob);
//        TextView title=(TextView)findViewById(R.id.info);
         myinfo=(TextView)findViewById(R.id.myinfo);
         mymemo=(TextView)findViewById(R.id.mymemo);
        mydetail=(TextView)findViewById(R.id.mydetail);
        memobutton=(Button)findViewById(R.id.memobutton);
         str_info=(TextView)findViewById(R.id.str_info);
        profile_blur=(ImageView)findViewById(R.id.profile_blur);
        profile=(CircleImageView)findViewById(R.id.profile);

        fab=(FloatingActionButton)findViewById(R.id.fab);


        //
        final ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
        parseQuery.whereEqualTo("name",intent.getStringExtra("name"));
        parseQuery.whereEqualTo("info", intent.getStringExtra("idea"));
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                for (int i = 0; i < list.size(); i++) {
                    user = list.get(i);
                    parseObject = list.get(i);
                }
            }
        });




        Bitmap bitmap;
        if(intent.getByteArrayExtra("profile")!=null) {
            bitmap = BitmapFactory.decodeByteArray(intent.getByteArrayExtra("profile"), 0, intent.getByteArrayExtra("profile").length);
            profile.setImageBitmap(bitmap);
            profile_blur.setImageBitmap(blur(getApplicationContext(), bitmap, 20));
        }
        else{
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_page);
            profile_blur.setImageBitmap(blur(getApplicationContext(), bitmap, 20));
            profile.setImageResource(R.drawable.ic_user);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(intent.getBooleanExtra("star",true ))
            fab.setImageResource(R.drawable.ic_check_white);
        else
            fab.setImageResource(R.drawable.add);


        collapsing_toolbar.setTitle(intent.getStringExtra("name"));

        switch (intent.getIntExtra("cur_job",0)){      //직종과 아이디어 및 스킬 텍스트 설정
            case 0:
                myjob.setText("기획자");
                str_info.setText("아이디어");
                myjob.setTextColor(getResources().getColor(R.color.tab_color));
                myjob.setBackgroundColor(getResources().getColor(R.color.planner));
                myjob.setPadding(30, 15, 30, 15);
//                title.setText("아이디어");
                break;
            case 1:
                myjob.setText("개발자");
                str_info.setText("스킬");
                myjob.setTextColor(getResources().getColor(R.color.tab_color));
                myjob.setBackgroundColor(getResources().getColor(R.color.developer));
                myjob.setPadding(30, 15, 30, 15);

//                title.setText("스킬");
                break;
            case 2:
                myjob.setText("디자이너");
                str_info.setText("스킬");
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
//        mymemo.setText(memo);

        loadingData(intent, 0);     //detail 불러오기
//        mydetail.setText(user.getString("detail"));


        List<String> memo_owner;
        if(ParseUser.getCurrentUser()!=null)
            memo_owner = ParseUser.getCurrentUser().getList("memo_owner");
        else
            memo_owner=null;
        if(memo_owner!=null && memo_owner.contains(intent.getStringExtra("name"))) {
            memobutton.setText("메모수정");
            List<String> memo_list = ParseUser.getCurrentUser().getList("memo");
            for (int i = 0; i < memo_owner.size(); i++) {
                if (memo_owner.get(i).equals(intent.getStringExtra("name"))) {
                    mymemo.setText(memo_list.get(i));
                }//end if
            }//end for
        }//end if

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingData(intent, 1);
            }
        });
        memobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ParseUser.getCurrentUser()!=null) {
                    Intent go_to = new Intent(InfoActivity.this, MemoActivity.class);
                    go_to.putExtra("name", intent.getStringExtra("name"));
                    go_to.putExtra("idea", intent.getStringExtra("idea"));
                    startActivity(go_to);
                }
                else{
                    Toast.makeText(getApplicationContext(),"로그인이 필요합니다.", Toast.LENGTH_SHORT);
                    startActivity(new Intent(InfoActivity.this,LoginActivity.class));
                }
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

    private void loadingData(Intent intent, final int action) {
        //
        ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
        parseQuery.whereEqualTo("name",intent.getStringExtra("name"));
        parseQuery.whereEqualTo("info", intent.getStringExtra("idea"));
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                for (int i=0;i<list.size();i++) {
                    final ParseObject parseObject = list.get(i);
                    if(action==0) {
                        mydetail.setText(parseObject.getString("detail"));
                    } else {
                        fab_clicked(parseObject);
                    }// end else
                }//end for
            }
        });

    }//loadingData



    private void fab_clicked(final ParseObject parseObject) {
        final ParseRelation<ParseUser> relation = ParseUser.getCurrentUser().getRelation("pick");
        ParseQuery<ParseUser> query = relation.getQuery();
        query.whereContains("objectId", user.getObjectId());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if(!list.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "관심멤버에서 제외합니다.", Toast.LENGTH_SHORT).show();
                    fab.setImageResource(R.drawable.add);

                    relation.remove(user);
                    ParseUser.getCurrentUser().saveInBackground();

                    ParseQuery<ParseObject> picked_query = ParseQuery.getQuery("Picked");
                    picked_query.whereEqualTo("user", user);
                    picked_query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (!list.isEmpty()) {
//                                Log.d("sss",list.size()+"");
                                ParseRelation<ParseUser> picked_relation = list.get(0).getRelation("picked");
                                picked_relation.remove(ParseUser.getCurrentUser());
                                list.get(0).saveInBackground();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "관심멤버에서 추가합니다.", Toast.LENGTH_SHORT).show();
                    fab.setImageResource(R.drawable.ic_check_white);

                    relation.add(user);
                    ParseUser.getCurrentUser().saveInBackground();

                    ParseQuery<ParseObject> picked_query = ParseQuery.getQuery("Picked");
                    picked_query.whereEqualTo("user", user);
                    picked_query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (!list.isEmpty()) {
//                                Log.d("sss",list.size()+"");
                                ParseRelation<ParseUser> picked_relation = list.get(0).getRelation("picked");
                                picked_relation.add(ParseUser.getCurrentUser());
                                list.get(0).saveInBackground();
                            }
                        }
                    });
                }//end else
            }
        }); //query

    }//fab_clicked

}//class
