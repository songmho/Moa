package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by eugene on 2015-07-18.
 */
public class loginActivity extends ActionBarActivity {
    EditText id_text;
    EditText pass_text;
    Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        id_text= (EditText) findViewById(R.id.id_text);
        pass_text= (EditText) findViewById(R.id.pass_text);
        login_btn= (Button) findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(loginActivity.this, MainActivity.class));
                finish();
            }
        });

    }
}
