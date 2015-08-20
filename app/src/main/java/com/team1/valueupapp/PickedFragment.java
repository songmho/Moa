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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyemi on 2015-08-18.
 */
public class PickedFragment extends Fragment {

    FrameLayout cur_container;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    int cur_job;
    List<ListRecyclerItem> items = new ArrayList<>();

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

        return cur_container;
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeList();
                    }
                });
            }
        }).start();

    }

    private void makeList() {
        items.clear();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("pick", ParseUser.getCurrentUser().getString("name"));
        query.addAscendingOrder("name");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (list.isEmpty())
                    Toast.makeText(getActivity().getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
//                Log.d("dddd", "" + list.size());
//                Log.d("sss", ""+ParseUser.getCurrentUser().getList("memo").size());
                final List<String> memo_owner = ParseUser.getCurrentUser().getList("memo_owner");
                final List<String> memo_list = ParseUser.getCurrentUser().getList("memo");

                for (int i = 0; i < list.size(); i++) {
                    String memo = "";
//                    Log.d("ddd", "" + list.get(i).getList("memo_owner").size());
                    for(int j = 0; j < memo_owner.size(); j++) {
                        if(list.get(i).getString("name").equals(memo_owner.get(j))) {
                            memo = memo_list.get(j);
                        }//관심멤버의 이름이 memo_owner에 있으면 memo_owner번째 메모 memo변수에 저장
                    }
                    if (list.get(i).getString("job").equals("plan"))
                        cur_job = 0;
                    else if (list.get(i).getString("job").equals("dev"))
                        cur_job = 1;
                    else
                        cur_job = 2;
                    ParseFile parse_file = (ParseFile) list.get(i).get("profile");
                    try {
                        byte[] bytes;
                        if (parse_file != null)
                            bytes = parse_file.getData();
                        else
                            bytes = null;
                        ListRecyclerItem item = new ListRecyclerItem(bytes, list.get(i).getString("info"),
                                list.get(i).getString("name"), true, cur_job, memo, recyclerView);
                        items.add(item);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
                recyclerView.setAdapter(new RecyclerAdpater(getActivity(), items, R.layout.item_interest, 0));
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}//class