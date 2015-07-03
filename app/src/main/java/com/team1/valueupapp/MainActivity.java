package com.team1.valueupapp;

import android.app.SearchManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    Toolbar toolbar;

    FragmentTransaction fragmentTransaction;
    Fragment cur_fragment=new MainFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpNavDrawer();

        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, cur_fragment);
        fragmentTransaction.commit();
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        navigationView=(NavigationView)findViewById(R.id.navigationView);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
