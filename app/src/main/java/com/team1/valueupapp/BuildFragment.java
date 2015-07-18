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
        cur_container=(FrameLayout)inflater.inflate(R.layout.fragment_build,container,false);
        recyclerView=(RecyclerView)cur_container.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));

        ImageButton add=(ImageButton)cur_container.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"add_준비중입니다.",Toast.LENGTH_SHORT).show();
            }
        });

        List<Grid_Item> items=new ArrayList<>();
        Grid_Item[] item=new Grid_Item[6];

        item[0]=new Grid_Item("팀빌딩어플",1,0,2);
        item[1]=new Grid_Item("핼스어플",1,2,0);
        item[2]=new Grid_Item("가계부어플",1,3,1);
        item[3]=new Grid_Item("밥먹기어플",2,2,0);
        item[4]=new Grid_Item("여행어플",1,1,3);
        item[5]=new Grid_Item("고민어플",1,0,0);

        items.addAll(Arrays.asList(item).subList(0, 6));
        recyclerView.setAdapter(new RecyclerAdpater(getActivity(), items, R.layout.item_grid));
        return cur_container;
    }
}
