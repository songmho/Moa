package com.team1.valueupapp;

import android.os.Bundle;
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
 * Created by eugene on 2015-06-30.
 */
public class ListFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout cur_container;
    int cur_fragment;
    public ListFragment(int i) {
        this.cur_fragment=i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        cur_container=(LinearLayout)inflater.inflate(R.layout.fragment_list,container,false);
        recyclerView=(RecyclerView)cur_container.findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        List<ListRecyclerItem> items=new ArrayList<>();
        ListRecyclerItem item = null;
        switch (cur_fragment){
            case 0:
                item=new ListRecyclerItem(R.drawable.test,"버스킹어플","최에스더",false,"blar blar");
                break;
            case 1:
                item=new ListRecyclerItem(R.drawable.test,"핼스어플","최에스더",false,"blar blar");
                break;
            case 2:
                item=new ListRecyclerItem(R.drawable.test,"팀빌딩어플","최에스더",false,"blar blar");
                break;
            default:
                break;
        }
        items.add(item);
        recyclerView.setAdapter(new RecyclerAdpater(getActivity(),items,R.layout.item_listrecycler,0));

        return cur_container;
    }
}