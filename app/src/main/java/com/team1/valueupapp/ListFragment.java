package com.team1.valueupapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
    List<ListRecyclerItem> items;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cur_container=(LinearLayout)inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView=(RecyclerView)cur_container.findViewById(R.id.recyclerview);

        Bundle bundle=this.getArguments();
        cur_fragment=bundle.getInt("cur_fragment",0);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        items=new ArrayList<>();
        switch (cur_fragment){
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

        return cur_container;
    }
    private void getListData(String job) {
        ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery("ValueUp_people");
        parseQuery.whereContains("job", job);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (int i=0;i<list.size();i++) {
                    ListRecyclerItem item = new ListRecyclerItem(R.drawable.splash_logo,
                            list.get(i).getString("info"),list.get(i).getString("name"), false, "");
                    items.add(item);
                }
                recyclerView.setAdapter(new RecyclerAdpater(getActivity(), items, R.layout.item_listrecycler, 0));
            }
        });
    }


}