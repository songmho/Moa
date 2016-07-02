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
import com.team1.valueupapp.item.TeamItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by knulps on 16. 3. 26..
 */
public class TeamSearchFragment extends android.support.v4.app.Fragment {
    public TeamSearchFragment() {

    }

    @Bind(R.id.progressbar) ProgressBar progressBar;
    @Bind(R.id.teamSearchRecyclerView) RecyclerView recyclerView;
    @Bind(R.id.empty_result) TextView emptyView;

    Context mContext;
    ArrayList<TeamItem> mainTeamItems;

    private static final String TAG = "TeamSearchFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.l_fragment_team_search, container, false);
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
                        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Team");
                        parseQuery.whereContains("intro", query);
                        parseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                Log.e(TAG, "search size :" + list.size());
                                for (ParseObject parseObject : list) {
                                    ParseUser user = parseObject.getParseUser("admin_member");
                                    try {
                                        user.fetchIfNeeded();
                                        TeamItem item = new TeamItem(parseObject.getObjectId(), parseObject.getString("intro"), user.getString("name"), user.getUsername(), parseObject.getString("intro_detail"));
                                        mainTeamItems.add(item);
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
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
                                recyclerView.setAdapter(new TeamRecyclerAdapter(mContext, mainTeamItems, R.layout.activity_team));
                            }
                        });
                    }
                });
            }
        }).start();
    }
}