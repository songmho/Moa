package com.team1.valueupapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
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
        cur_container=(FrameLayout)inflater.inflate(R.layout.fragment_build,container,false);
        recyclerView=(RecyclerView)cur_container.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        ImageButton add=(ImageButton)cur_container.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"add_준비중입니다.",Toast.LENGTH_SHORT).show();
            }
        });

        List<Grid_Item> items=new ArrayList<>();
        Grid_Item item1=new Grid_Item("버스킹어플",1,5,1);
        Grid_Item item2=new Grid_Item("버스킹어플",1,5,1);
        Grid_Item item3=new Grid_Item("버스킹어플",1,5,1);
        Grid_Item item4=new Grid_Item("버스킹어플",1,5,1);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        recyclerView.setAdapter(new RecyclerAdpater(getActivity(), items, R.layout.item_grid));
        return cur_container;
    }
}
