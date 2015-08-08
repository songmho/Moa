package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

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
    de.hdodenhof.circleimageview.CircleImageView my_imag;
    int job_int;
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

        my_imag=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.my_image);

        Intent getIntent=getIntent();
        name.setText(getIntent.getStringExtra("name"));
        info.setText(getIntent.getStringExtra("info"));
        myinfo.setText(getIntent.getStringExtra("myinfo"));
        detail.setText(getIntent.getStringExtra("mydetail"));

        my_imag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_SHORT).show();
            }
        });

        switch (getIntent.getStringExtra("job")){
            case "기획자":
                field1.setChecked(true);
                break;
            case "개발자":
                field2.setChecked(true);
                break;
            case "디자이너":
                field3.setChecked(true);
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
                        job_int=0;
                        break;
                    case R.id.field2:
                        field1.setChecked(false);
                        field2.setChecked(true);
                        field3.setChecked(false);
                        job_int=1;
                        break;
                    case R.id.field3:
                        field1.setChecked(false);
                        field2.setChecked(false);
                        field3.setChecked(true);
                        job_int=2;
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
            user.saveInBackground();
            finish();
            Toast.makeText(getApplicationContext(),"저장되었습니다.",Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
