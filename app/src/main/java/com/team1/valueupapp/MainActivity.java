package com.team1.valueupapp;

import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ActionBarDrawerToggle drawerToggle;
    Button btn[] = new Button[3];
    ViewPager viewPager = null;
    Thread thread = null;
    Handler handler = null;
    int p=0;
    int v=1;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("소개");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF08ae9e));

        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigationView);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());

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

        drawerToggle =new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(drawerToggle.onOptionsItemSelected(item))
            return true;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
