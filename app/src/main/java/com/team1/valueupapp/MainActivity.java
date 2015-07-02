package com.team1.valueupapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    Button btn[] = new Button[3];
    ViewPager viewPager = null;
    Thread thread = null;
    Handler handler = null;
    int p=0;
    int v=1;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpNavDrawer();

        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        navigationView=(NavigationView)findViewById(R.id.navigationView);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()){
                    case R.id.invate:
                        Toast.makeText(getApplicationContext(),"팀원초대",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.teambuild:
                        Toast.makeText(getApplicationContext(),"팀빌딩",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.basket:
                        Toast.makeText(getApplicationContext(),"장바구니",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.setup:
                        Toast.makeText(getApplicationContext(),"설정",Toast.LENGTH_SHORT).show();
                        return true;

                }
                return true;
            }
        });
        viewPager.setAdapter(adapter);

        btn[0] = (Button)findViewById(R.id.btn_a);
        btn[1] = (Button)findViewById(R.id.btn_b);
        btn[2] = (Button)findViewById(R.id.btn_c);

        for(int i=0;i<btn.length; i++) btn[i].setOnClickListener(this);



        handler = new Handler(){


            public void handleMessage(android.os.Message msg) {
                if(p==0){
                    viewPager.setCurrentItem(1);
                    p++;
                    v=1;
                }if(p==1&&v==0){
                    viewPager.setCurrentItem(1);
                    p--;
                }if(p==1&&v==1){
                    viewPager.setCurrentItem(2);
                    p++;
                }if(p==2){
                    viewPager.setCurrentItem(1);
                    p--;
                    v=0;
                }
            }
        };
    }

    private void setUpNavDrawer() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("소개");
        toolbar.setNavigationIcon(R.drawable.drawericon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btn_a:
                viewPager.setCurrentItem(0);
                Toast.makeText(this, "기획자", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_b:
                viewPager.setCurrentItem(1);
                Toast.makeText(this,"개발자", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_c:
                viewPager.setCurrentItem(2);
                Toast.makeText(this,"디자이너", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
