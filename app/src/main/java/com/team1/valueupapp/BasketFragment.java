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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songmho on 2015-07-03.
 */
public class BasketFragment extends Fragment {

    FrameLayout cur_container;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cur_container=(FrameLayout)inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView=(RecyclerView)cur_container.findViewById(R.id.recyclerview);
        progressBar=(ProgressBar)cur_container.findViewById(R.id.progressbar);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        makeList();
        return cur_container;
    }

    private void makeList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ParseQuery<ParseObject> query=ParseQuery.getQuery("ValueUp_people");
                        query.whereEqualTo("pick", ParseUser.getCurrentUser().getString("name"));       //pick array에 현재 유저네임 있으면 그사람 출력.
                        final List<ListRecyclerItem> items=new ArrayList<>();
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                for(int i=0;i<list.size();i++){
                                    ListRecyclerItem item=new ListRecyclerItem(R.drawable.splash_logo,list.get(i).getString("info"),
                                            list.get(i).getString("name"),true);
                                    items.add(item);
                                }
                                recyclerView.setAdapter(new RecyclerAdpater(getActivity(), items, R.layout.item_listrecycler, 0));
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        }).start();

    }
}
