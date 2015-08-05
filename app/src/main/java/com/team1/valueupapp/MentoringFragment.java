package com.team1.valueupapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songmho on 15. 8. 4.
 */
public class MentoringFragment extends Fragment {
    LinearLayout mentoring_container;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mentoring_container=(LinearLayout)inflater.inflate(R.layout.fragment_mentoring,container,false);

        RecyclerView recyclerView=(RecyclerView)mentoring_container.findViewById(R.id.recyclerview);
        List<Mentoring_item> items=new ArrayList<>();
        Mentoring_item item=new Mentoring_item("디자이너",2015,8,05,"GUI 가이드라인 프로세스","송효수 멘토님","강남 알럿");
        items.add(item);items.add(item);items.add(item);items.add(item);items.add(item);
        recyclerView.setAdapter(new Mentoring_adapter(getActivity(), items, R.layout.fragment_mentoring));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return mentoring_container;
    }
}
