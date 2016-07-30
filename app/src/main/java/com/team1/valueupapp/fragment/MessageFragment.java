package com.team1.valueupapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.team1.valueupapp.R;
import com.team1.valueupapp.adapter.MessageAdapter;
import com.team1.valueupapp.item.MessageItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 쪽지함 fragment
 * Created by songmho on 2016-05-28.
 */
public class MessageFragment extends Fragment {
    ArrayList<MessageItem> items = new ArrayList<>();
    Bundle bundle;
    Context mContext;
    RecyclerView.LayoutManager layoutManager;

    private static final String TAG = "MessageFragment";

    @Bind(R.id.progressbar) ProgressBar progressBar;
    @Bind(R.id.recyclerview) RecyclerView recyclerview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        bundle = getArguments();     //Activity에서 보낸 정보 받아옴
        ButterKnife.bind(this, view);
        //recyclerview 초기화 및 설정

        recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerview.setLayoutManager(layoutManager);

        initData();
        return view;
    }

    public void initData() {
        items.clear();      //불러올 때마다 데이터 지움.
        progressBar.setVisibility(View.VISIBLE);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("message");
        query.addDescendingOrder("sendDate");
        if (bundle.getString("cur_state").equals("받은쪽지함"))       //받은 쪽지의 경우
            query.whereEqualTo("user_to", ParseUser.getCurrentUser());      //user_to에 현재 유저와 같은 경우만 씀
        else        //보낸 쪽지의 경우
            query.whereEqualTo("user_from", ParseUser.getCurrentUser());      //user_from에 현재 유저와 같은 경우만 씀
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                progressBar.setVisibility(View.GONE);
                MessageItem i;
                if (list != null) {
                    for (ParseObject o : list) {
                        try {
                            if (bundle.getString("cur_state").equals("받은쪽지함")) {     //현재 받은 쪽지함일 경우
                                ParseUser u = o.getParseUser("user_from");      //보낸 사람불러옴
                                u.fetchIfNeeded();          //ParseUser 변수에서 값을 가져다 쓰기 위해

                                i = new MessageItem("From. ", u.getString("name"), o.getString("text"), o.getString("sendDate"));
                                items.add(i);
                            } else {                //현재 보낸 쪽지함일 경우
                                ParseUser u = o.getParseUser("user_to");        //받은 사람 불러옴
                                u.fetchIfNeeded();          //ParseUser 변수에서 값을 가져다 쓰기 위해
                                i = new MessageItem("to. ", u.getString("name"), o.getString("text"), o.getString("sendDate"));
                                items.add(i);
                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else {
                    Log.e(TAG, "parseobject null");
                }
                recyclerview.setAdapter(new MessageAdapter(mContext, items));       //adapter 설정
            }
        });
    }
}