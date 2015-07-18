package com.team1.valueupapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by songmho on 2015-07-04.
 */
public class BuildFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FrameLayout cur_container;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cur_container=(FrameLayout)inflater.inflate(R.layout.fragment_build, container, false);
        recyclerView=(RecyclerView)cur_container.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        ImageButton add=(ImageButton)cur_container.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"add_준비중입니다.",Toast.LENGTH_SHORT).show();
            }
        });



        ParseQuery<ParseObject> query = ParseQuery.getQuery("ValueUp_team");
        query.findInBackground(new FindCallback<ParseObject>() {

            final List<Grid_Item> items=new ArrayList<>();
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        ParseObject ob = list.get(i);
                        Grid_Item grid_item = new Grid_Item(ob.getString("idea"), ob.getString("state"),
                                ob.getInt("plan"), ob.getInt("dev"), ob.getInt("dis"));
                        items.add(grid_item);
                    }
                    recyclerView.setAdapter(new RecyclerAdpater(getActivity(), items, R.layout.item_grid));
                }
            }
        });

        return cur_container;
    }
}
