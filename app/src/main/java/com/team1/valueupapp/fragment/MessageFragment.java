package com.team1.valueupapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.team1.valueupapp.R;
import com.team1.valueupapp.adapter.MessageAdapter;
import com.team1.valueupapp.item.MessageItem;

import java.util.ArrayList;

/**
 * Created by songmho on 2016-05-28.
 */
public class MessageFragment extends Fragment {
    Context mContext;
    ArrayList<MessageItem> items = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        Bundle b=getArguments();

        RecyclerView recyclerview=(RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerview.setLayoutManager(layoutManager);

        Log.d("testtest",b.getString("cur_state"));
        MessageItem[] item=new MessageItem[5];
        for(int i=0;i<5;i++){
            if(b.getString("cur_state").equals("받은쪽지함"))
                item[i]=new MessageItem("From. ","정형인","I'm Danhobak.","2016.05.28 16:21");
            else
                item[i]=new MessageItem("To. ","정형인","빨리 개발하겠습니다.","2016.05.28 16:21");

            items.add(item[i]);
        }

        recyclerview.setAdapter(new MessageAdapter(mContext, items));
        return view;
    }
}
