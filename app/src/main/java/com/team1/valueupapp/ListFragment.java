package com.team1.valueupapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
 * Created by eugene on 2015-06-30.
 */

public class ListFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FrameLayout cur_container;
    int cur_fragment;
    List<ListRecyclerItem> items;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cur_container=(FrameLayout)inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView=(RecyclerView)cur_container.findViewById(R.id.recyclerview);
        progressBar=(ProgressBar)cur_container.findViewById(R.id.progressbar);

        Bundle bundle=this.getArguments();
        cur_fragment=bundle.getInt("cur_fragment",0);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        items=new ArrayList<>();

        return cur_container;
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
                        progressBar.setVisibility(View.VISIBLE);
                        items.clear();
                        switch (cur_fragment) {
                            case 0:
                                getListData("plan");
                                break;
                            case 1:
                                getListData("dev");
                                break;
                            case 2:
                                getListData("dis");
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        }).start();
    }

    private void getListData(String job) {
        ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery("ValueUp_people");
        parseQuery.whereContains("job", job);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (int i=0;i<list.size();i++) {
                    ListRecyclerItem item;
                    ParseObject parseObject=list.get(i);
                    if(parseObject.getList("pick").contains(ParseUser.getCurrentUser().get("name")))
                        item = new ListRecyclerItem(R.drawable.splash_logo,
                                parseObject.getString("info"),parseObject.getString("name"), true);
                    else
                        item = new ListRecyclerItem(R.drawable.splash_logo,
                                parseObject.getString("info"),parseObject.getString("name"), false);
                    items.add(item);
                }
                recyclerView.setAdapter(new RecyclerAdpater(getActivity(), items, R.layout.item_listrecycler,0));
                progressBar.setVisibility(View.GONE);
            }
        });
    }


}