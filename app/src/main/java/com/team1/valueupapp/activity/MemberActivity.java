package com.team1.valueupapp.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.team1.valueupapp.adapter.MyViewPagerAdapter;
import com.team1.valueupapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by songmho on 2015-07-03.
 */
public class MemberActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.tablayout) TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        ButterKnife.bind(this);

        toolbar.setTitle("전체멤버");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
//        searchItem.setVisible(isvisible);
        SearchManager searchManager = (SearchManager) MemberActivity.this.getSystemService(SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.clearFocus();
            searchView.setQueryHint("이름 검색");
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MemberActivity.this.getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Intent intent = new Intent(MemberActivity.this, SearchActivity_Legacy.class);
                    intent.putExtra("query", query);
                    intent.putExtra("page", "main");
                    startActivity(intent);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return (item.getItemId() == R.id.action_search) || super.onOptionsItemSelected(item);
    }
}
