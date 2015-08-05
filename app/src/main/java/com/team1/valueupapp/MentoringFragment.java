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
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songmho on 15. 8. 4.
 */
public class MentoringFragment extends Fragment {
    FrameLayout mentoring_container;
    List<Mentoring_item> items;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mentoring_container=(FrameLayout)inflater.inflate(R.layout.fragment_mentoring, container, false);

        recyclerView=(RecyclerView)mentoring_container.findViewById(R.id.recyclerview);
        progressBar=(ProgressBar)mentoring_container.findViewById(R.id.progressbar);

        items=new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return mentoring_container;
    }

    @Override
    public void onResume() {
        super.onResume();

        Makinglist();
    }

    private void Makinglist() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        items.clear();
                        ParseQuery<ParseObject> query=ParseQuery.getQuery("ValueUp_mentoring");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {

                                for(ParseObject o:list) {
                                    Mentoring_item item = new Mentoring_item(o.getString("job"),
                                            o.getInt("year"), o.getInt("month"), o.getInt("day"),
                                            o.getString("title"), o.getString("mentor"), o.getString("venue"));
                                    items.add(item);
                                }
                                recyclerView.setAdapter(new Mentoring_adapter(getActivity(), items, R.layout.fragment_mentoring));
                                progressBar.setVisibility(View.GONE);

                            }
                        });

                    }
                });
            }
        }).start();

    }
}
