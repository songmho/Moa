package com.team1.valueupapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugene on 2015-08-08.
 */
public class SearchFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FrameLayout cur_container;
    int cur_job;
    List<ListRecyclerItem> items;
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

        Bundle bundle=this.getArguments();
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        String search = new String(bundle.getString("query",""));

        //search=bundle.getString("search");
        Log.d("search", search);

        makeList(search);


        return cur_container;
    }
    public void makeList(final String search) {
        Log.d("aa",search);
        new Thread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ParseQuery<ParseUser> query=ParseUser.getQuery();
                        query.whereContains("name",search);
                        final List<ListRecyclerItem> items=new ArrayList<>();
                        query.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> list, ParseException e) {
                                Log.d("dddd",""+list.size());
                                for(int i=0;i<list.size();i++){
                                    if(list.get(i).getString("job").equals("plan"))
                                        cur_job=0;
                                    else if(list.get(i).getString("job").equals("dev"))
                                        cur_job=1;
                                    else
                                        cur_job=2;

                                    ParseFile parse_file=(ParseFile)list.get(i).get("profile");
                                    try {
                                        byte[] bytes;
                                        if(parse_file!=null)
                                            bytes=parse_file.getData();
                                        else
                                            bytes=null;
                                        ListRecyclerItem item=new ListRecyclerItem(bytes,list.get(i).getString("info"),
                                                list.get(i).getString("name"),true,cur_job,recyclerView);
                                        items.add(item);
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
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
