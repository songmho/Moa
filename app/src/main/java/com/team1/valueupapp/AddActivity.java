package com.team1.valueupapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by hyemi on 2015-07-26.
 */
public class AddActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout container;
    List<Add_item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teamadd);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ValueUp_people");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        ParseObject ob = list.get(i);
                        Add_item add_item = new Add_item(ob.getString("name"), R.drawable.splash_logo, true);
                        items.add(add_item);
                    }
                    recyclerView.setAdapter(new Add_RecyclerAdapter(getApplicationContext(), items, R.layout.add_recycler));
                }
            }
        });

    }//on create
}//class
