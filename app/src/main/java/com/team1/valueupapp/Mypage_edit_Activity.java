package com.team1.valueupapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by songmho on 15. 8. 8.
 */
public class Mypage_edit_Activity extends AppCompatActivity {

    EditText name;
    RadioGroup fieldgroup;
    RadioButton field1;
    RadioButton field2;
    RadioButton field3;
    TextView info;
    EditText myinfo;
    EditText detail;
    de.hdodenhof.circleimageview.CircleImageView profile;
    int job_int;
    String tempPath="data/data/com.team1.valueupapp/files/profile.jpg";
    File profileimage=new File("data/data/com.team1.valueupapp/files/profile.jpg");
    ParseObject object;


    ParseFile profile_parse;
    ParseUser user=ParseUser.getCurrentUser();
    int CAMERA_REQUEST=1000;
    int SELECT_FILE=2000;
    CharSequence[] item={"카메라","갤러리에서 사진 가져오기","삭제"};
    File file_up_path=new File("data/data/com.team1.valueupapp/files/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_edit);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("내정보 수정");

        name=(EditText)findViewById(R.id.name);
        fieldgroup=(RadioGroup)findViewById(R.id.fieldgroup);
        field1=(RadioButton)findViewById(R.id.field1);
        field2=(RadioButton)findViewById(R.id.field2);
        field3=(RadioButton)findViewById(R.id.field3);
        info=(TextView)findViewById(R.id.info);
        myinfo=(EditText)findViewById(R.id.myinfo);
        detail=(EditText)findViewById(R.id.detail);

        profile =(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.my_image);
        if(profileimage.exists()){
            Bitmap bm=BitmapFactory.decodeFile(tempPath);
            profile.setImageBitmap(bm);}
        else{
            Bitmap b=BitmapFactory.decodeResource(getResources(),R.drawable.ic_user);
            profile.setImageBitmap(b);
        }
        Intent getIntent=getIntent();
        name.setText(getIntent.getStringExtra("name"));
        myinfo.setText(getIntent.getStringExtra("myinfo"));
        detail.setText(getIntent.getStringExtra("mydetail"));

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakingAlertDialog();
            }
        });

        switch (getIntent.getStringExtra("job")){
            case "기획자":
                field1.setChecked(true);
                info.setText("아이디어");
                break;
            case "개발자":
                field2.setChecked(true);
                info.setText("스킬");
                break;
            case "디자이너":
                field3.setChecked(true);
                info.setText("스킬");
                break;
        }

        fieldgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.field1:
                        field1.setChecked(true);
                        field2.setChecked(false);
                        field3.setChecked(false);
                        info.setText("아이디어");
                        job_int=0;
                        break;
                    case R.id.field2:
                        field1.setChecked(false);
                        field2.setChecked(true);
                        field3.setChecked(false);
                        info.setText("스킬");
                        job_int=1;
                        break;
                    case R.id.field3:
                        field1.setChecked(false);
                        field2.setChecked(false);
                        field3.setChecked(true);
                        job_int=2;
                        info.setText("스킬");
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        MenuItem checkItem = menu.findItem(R.id.action_check);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_check) {
            ParseUser user=ParseUser.getCurrentUser();
            user.put("name", String.valueOf(name.getText()));
            user.put("detail",String.valueOf(detail.getText()));
            user.put("info",String.valueOf(myinfo.getText()));
            switch (job_int){
                case 0:
                    user.put("job","plan");
                    break;
                case 1:
                    user.put("job","dev");
                    break;
                case 2:
                    user.put("job","dis");
                    break;
            }

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Picked");
            query.whereEqualTo("user", user);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    ParseUser.getCurrentUser().put("picked", list.get(0));
                    ParseUser.getCurrentUser().saveInBackground();
                }
            });


//            user.put("picked", object);

            user.saveInBackground();
            finish();
            Toast.makeText(getApplicationContext(),"저장되었습니다.",Toast.LENGTH_SHORT).show();
            return true;
        }
        if(id==android.R.id.home && getIntent().getIntExtra("signup",0)==1){
            startActivity(new Intent(Mypage_edit_Activity.this,MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void MakingAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Mypage_edit_Activity.this, R.style.dialog);
        builder.setTitle("프로필 사진 추가하기");
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if (item[position].equals("카메라")) {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (camera.resolveActivity(getPackageManager()) != null)
                        startActivityForResult(camera, CAMERA_REQUEST);
                } else if (item[position].equals("갤러리에서 사진 가져오기")) {
                    Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    gallery.addCategory(Intent.CATEGORY_OPENABLE);
                    gallery.setType("image/*");
                    startActivityForResult(Intent.createChooser(gallery, "갤러리 선택"), SELECT_FILE);
                } else if (item[position].equals("삭제")) {
                    File[] files = file_up_path.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        String fname = files[i].getName();
                        if (fname.equals("profile.jpg"))
                            files[i].delete();
                    }
                    ParseUser.getCurrentUser().remove("profile");
                    Toast.makeText(getApplicationContext(), "삭제하였습니다.", Toast.LENGTH_SHORT).show();
                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user);
                    profile.setImageBitmap(b);
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap thum = null;
        if(resultCode==RESULT_OK && data!=null){
            if(requestCode==CAMERA_REQUEST){
                thum=(Bitmap)data.getExtras().get("data");
                profile.setImageBitmap(thum);
                imgSendParse(thum);
            }
            else if(requestCode==SELECT_FILE){
                Uri uri=data.getData();
                try {
                    thum = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    profile.setImageBitmap(thum);
                    imgSendParse(thum);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            File file=new File("profile.jpg");
            FileOutputStream fos= null;
            try {
                fos = openFileOutput("profile.jpg",0);
                thum.compress(Bitmap.CompressFormat.JPEG,50,fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void imgSendParse(Bitmap thum) {
        profile_parse=new ParseFile("profile.jpg",bitmapTobyte(thum));
        if(user.get("profile")!=null)
            user.remove("profile");
        user.put("profile", profile_parse);
        user.saveInBackground();
    }

    private byte[] bitmapTobyte(Bitmap bm) {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] bytes=stream.toByteArray();
        return bytes;
    }
}
