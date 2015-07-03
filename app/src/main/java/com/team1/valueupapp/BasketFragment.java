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
        ListRecyclerItem item1=new ListRecyclerItem(R.drawable.test,"버스킹어플","최에스더",false,"blar blar");
        ListRecyclerItem item2=new ListRecyclerItem(R.drawable.test,"버스킹어플","최에스더",false,"blar blar");
        ListRecyclerItem item3=new ListRecyclerItem(R.drawable.test,"버스킹어플","최에스더",false,"blar blar");
        ListRecyclerItem item4=new ListRecyclerItem(R.drawable.test,"버스킹어플","최에스더",false,"blar blar");
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        recyclerView.setAdapter(new RecyclerAdpater(getActivity(), items, R.layout.item_listrecycler, 0));
        return cur_container;
    }
}
