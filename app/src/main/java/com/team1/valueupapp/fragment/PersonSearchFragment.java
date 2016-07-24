package com.team1.valueupapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.team1.valueupapp.R;
import com.team1.valueupapp.adapter.TeamRecyclerAdapter;
import com.team1.valueupapp.adapter.UserRecyclerAdapter;
import com.team1.valueupapp.item.TeamItem;
import com.team1.valueupapp.item.UserItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by knulps on 16. 3. 26..
 */
public class PersonSearchFragment extends android.support.v4.app.Fragment {
    public PersonSearchFragment() {

    }

    @Bind(R.id.progressbar) ProgressBar progressBar;
    @Bind(R.id.personSearchRecyclerView) RecyclerView recyclerView;
    @Bind(R.id.empty_result) TextView emptyView;
    Context mContext;
    ArrayList<UserItem> mainTeamItems;

    private static final String TAG = "PersonSearchFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.l_fragment_person_search, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    public void search(final String query) {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mainTeamItems != null)
                    mainTeamItems.clear();
                else
                    mainTeamItems = new ArrayList<>();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                        parseQuery.whereContains("name", query);
                        parseQuery.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> list, ParseException e) {
                                Log.e(TAG, "search size :" + list.size());
                                for (ParseObject parseObject : list) {
                                    UserItem item = new UserItem(parseObject.getObjectId(),parseObject.getString("username"), parseObject.getString("name"), parseObject.getString("info"));
                                    mainTeamItems.add(item);
                                }
                                if (isAdded()) {
                                    progressBar.setVisibility(View.GONE);
                                }
                                Log.e(TAG, "added item size :" + mainTeamItems.size());
                                if (mainTeamItems.size() == 0) {
                                    recyclerView.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                } else {
                                    emptyView.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                                recyclerView.setAdapter(new UserRecyclerAdapter(mContext, mainTeamItems, R.layout.activity_team));
                            }
                        });
                    }
                });
            }
        }).start();
    }
}
