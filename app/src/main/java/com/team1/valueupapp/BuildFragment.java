package com.team1.valueupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songmho on 2015-07-04.
 */
public class BuildFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CoordinatorLayout cur_container;
    SwipeRefreshLayout refreshLayout;
    List<Grid_Item> items;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        cur_container=(CoordinatorLayout)inflater.inflate(R.layout.fragment_build, container, false);
        recyclerView=(RecyclerView)cur_container.findViewById(R.id.recyclerview);
        FloatingActionButton add=(FloatingActionButton)cur_container.findViewById(R.id.add);
        refreshLayout=(SwipeRefreshLayout)cur_container.findViewById(R.id.refreshlayout);

        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        items=new ArrayList<>();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity().getApplicationContext(), "add_준비중입니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {     //swiperefreshlayout이 trigger됬을 때
            @Override
            public void onRefresh() {
                makingList();
            }
        });

        return cur_container;
    }

    @Override
    public void onResume() {
        super.onResume();

        makingList();
    }

    private void makingList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();      //item array 초기화

                         refreshLayout.setRefreshing(true);      //swiperefreshlayout 보이게 하기

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("ValueUp_team");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                if (e == null) {
                                    for (int i = 0; i < list.size(); i++) {
                                        ParseObject ob = list.get(i);
                                        List<String> s=new ArrayList<>();
                                        List<String> l1=ob.getList("plan");
                                        List<String> l2=ob.getList("dev");
                                        List<String> l3=ob.getList("dis");
                                        s.addAll(l1);
                                        s.addAll(l2);
                                        s.addAll(l3);

                                        Grid_Item grid_item = new Grid_Item(ob.getString("idea"), ob.getString("state"),
                                                ob.getJSONArray("plan").length(), ob.getJSONArray("dev").length(),
                                                ob.getJSONArray("dis").length(), ob.getString("info"),s,ob.getString("constructor"));
                                        items.add(grid_item);
                                    }
                                    recyclerView.setAdapter(new RecyclerAdpater(getActivity(), items, R.layout.item_grid));
                                    refreshLayout.setRefreshing(false);         //데이터 다 불러온 후 swiperefreshlayout 보이지 않게.
                                }
                            }
                        });
                    }
                });
            }
        }).start();


    }
}
