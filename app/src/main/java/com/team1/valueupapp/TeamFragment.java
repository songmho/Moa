package com.team1.valueupapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyemi on 2015-08-16.
 */
public class TeamFragment extends Fragment {
    FrameLayout container;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    RecyclerView.LayoutManager layoutManager;
    List<Team_item> items;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        container = (FrameLayout) inflater.inflate(R.layout.fragment_team, viewGroup, false);
        recyclerView=(RecyclerView)container.findViewById(R.id.recyclerview);
        progressBar=(ProgressBar)container.findViewById(R.id.progressbar);

        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        items=new ArrayList<>();

        return container;
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        Team_item item = new Team_item(R.drawable.splash_logo, "팀빌딩 어플", "팀빌딩을 도와주는 앱"
                                , "서완규", "한혜미", "김유진", "송명호", "최에스더", "황의찬", true);
                        items.add(item);
                        recyclerView.setAdapter(new Team_RecyclerAdapter(getActivity(), items, R.layout.fragment_team));
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }
}//class
