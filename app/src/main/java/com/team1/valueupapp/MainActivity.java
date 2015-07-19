package com.team1.valueupapp;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends ActionBarActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    Toolbar toolbar;

    FragmentTransaction fragmentTransaction;
    Fragment cur_fragment=new MainFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(ParseUser.getCurrentUser()==null) {
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpNavDrawer();

        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, cur_fragment);
        fragmentTransaction.commit();
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);

        navigationView=(NavigationView)findViewById(R.id.navigationView);

        makeDrawerHeader();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                fragmentTransaction=getSupportFragmentManager().beginTransaction();
                menuItem.setChecked(true);
                switch (menuItem.getItemId()){
                    case R.id.introduce:
                        getSupportActionBar().setTitle("소개");

                        cur_fragment=new MainFragment();
                        fragmentTransaction.replace(R.id.container, cur_fragment);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.teambuild:
                        getSupportActionBar().setTitle("팀빌딩");
                        cur_fragment=new BuildFragment();
                        fragmentTransaction.replace(R.id.container, cur_fragment);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.basket:
                        getSupportActionBar().setTitle("관심멤버");
                        cur_fragment=new BasketFragment();
                        fragmentTransaction.replace(R.id.container, cur_fragment);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.setup:
                        getSupportActionBar().setTitle("설정");
                        Toast.makeText(getApplicationContext(),"준비중입니다.",Toast.LENGTH_SHORT).show();
                        cur_fragment=new SetupFragment();
                        fragmentTransaction.replace(R.id.container, cur_fragment);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();
                        return true;

                }
                return true;
            }
        });

    }

    private void makeDrawerHeader() {
        CircleImageView c=(CircleImageView)navigationView.findViewById(R.id.profile);
        TextView t=(TextView)navigationView.findViewById(R.id.name);
        if(ParseUser.getCurrentUser()!=null)
            t.setText(ParseUser.getCurrentUser().getString("name"));
        else
            t.setText("Hello");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if(drawerLayout.isDrawerOpen(navigationView))
                    drawerLayout.closeDrawers();
                else {
                    moveTaskToBack(true);
                    finish();
                }
                break;
        }

        return true;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchManager searchManager=(SearchManager)MainActivity.this.getSystemService(SEARCH_SERVICE);
        SearchView searchView=null;
        if(searchItem!=null){
            searchView=(SearchView)searchItem.getActionView();
        }
        if(searchView!=null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
