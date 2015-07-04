package com.team1.valueupapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songmho on 2015-07-03.
 */
public class BasketFragment extends Fragment {

    LinearLayout cur_container;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cur_container=(LinearLayout)inflater.inflate(R.layout.fragment_basket,container,false);
        recyclerView=(RecyclerView)cur_container.findViewById(R.id.recyclerview);

            recyclerView.setHasFixedSize(true);
            layoutManager=new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

        List<ListRecyclerItem> items=new ArrayList<>();
        ListRecyclerItem[] item = new ListRecyclerItem[6];
        item[0]=new ListRecyclerItem(R.drawable.test,"팀빌딩어플","서완규",true,"팀빌딩을 위한 어플");
        item[1]=new ListRecyclerItem(R.drawable.test2,"","송명호",true,"c, c++, 안드로이드,java, 포토샵 쪼금");
        item[2]=new ListRecyclerItem(R.drawable.test4,"","김유진",true,"c, c++, java, html, css");
        item[3]=new ListRecyclerItem(R.drawable.test3,"","한혜미",true,"c, c++, java, mysql");
        item[4]=new ListRecyclerItem(R.drawable.test,"","최에스더",true,"포토샵, 일러스트레이터, UI, UX, 인디자인");
        item[5]=new ListRecyclerItem(R.drawable.test2,"","황의찬",true,"포토샵, 일러스트레이터, HTML, CSS");
        for(int i=0;i<6;i++)
            items.add(item[i]);
        recyclerView.setAdapter(new RecyclerAdpater(getActivity(), items, R.layout.item_listrecycler, 0));
        return cur_container;
    }
}
