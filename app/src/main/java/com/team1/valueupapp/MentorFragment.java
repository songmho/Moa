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
 * Created by eugene on 2015-08-07.
 */
public class MentorFragment extends Fragment {
    LinearLayout mentor_container;
    List<Mentor_item> items;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mentor_container = (LinearLayout) inflater.inflate(R.layout.fragment_mentor, container, false);

        recyclerView = (RecyclerView) mentor_container.findViewById(R.id.recyclerview);

        items = new ArrayList<>();
        Mentor_item item;
        item= new Mentor_item("송효수", "디자인", "엔터프라이즈", "mentor@test.com");
        items.add(item);items.add(item);items.add(item);items.add(item);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new Mentor_Adapter(getActivity(), items, R.layout.fragment_mentor));

        return mentor_container;
    }
}