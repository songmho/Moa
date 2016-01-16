package com.team1.valueupapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.team1.valueupapp.R;
import com.team1.valueupapp.adapter.RecyclerAdpater;
import com.team1.valueupapp.item.ListRecyclerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugene on 2015-06-30.
 */

public class ListFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FrameLayout cur_container;
    int cur_job;
    List<ListRecyclerItem> items;
    ProgressBar progressBar;

//    ListRecyclerItem item;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cur_container = (FrameLayout) inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = (RecyclerView) cur_container.findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) cur_container.findViewById(R.id.progressbar);

        Bundle bundle = this.getArguments();
        cur_job = bundle.getInt("cur_job", 0);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        items = new ArrayList<>();

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
                        switch (cur_job) {
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
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereContains("job", job);
        parseQuery.addAscendingOrder("name");
        parseQuery.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(final List<ParseUser> list, ParseException e) {
                if (list != null) {
                    for (ParseUser user : list) {
                        byte[] bytes = new byte[0];
                        ListRecyclerItem item;

                        ParseFile parse_file = (ParseFile) user.get("profile");
                        try {
                            if (parse_file == null)
                                bytes = null;
                            else {
                                bytes = parse_file.getData();
                            }//end else
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }//end catch

                        if (!list.isEmpty()) {
                            item = new ListRecyclerItem(bytes,
                                    user.getString("info"), user.getString("name"), cur_job, recyclerView);
                        } else {
                            item = new ListRecyclerItem(bytes,
                                    user.getString("info"), user.getString("name"), cur_job, recyclerView);
                        }//end else

                        items.add(item);
                        recyclerView.setAdapter(new RecyclerAdpater(getActivity(), items, R.layout.item_listrecycler, 0));

                    }
                    progressBar.setVisibility(View.GONE);
                }//end if
            }
        });

    }


}