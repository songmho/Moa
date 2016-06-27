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

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.team1.valueupapp.R;
import com.team1.valueupapp.adapter.MessageAdapter;
import com.team1.valueupapp.item.MessageItem;

import java.util.ArrayList;
import java.util.List;

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
        final Bundle b=getArguments();

        final RecyclerView recyclerview=(RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerview.setLayoutManager(layoutManager);

        ParseQuery<ParseObject> query=ParseQuery.getQuery("message");
        if(b.getString("cur_state").equals("받은쪽지함"))
            query.whereEqualTo("user_to", ParseUser.getCurrentUser());
        else
            query.whereEqualTo("user_from",ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                MessageItem i;
                for(ParseObject o : list){
                    Log.d("dfdfd",""+list.size());
                    try {
                        if(b.getString("cur_state").equals("받은쪽지함")) {
                            ParseUser u= o.getParseUser("user_from");
                            u.fetchIfNeeded();
                            i = new MessageItem("From. ", u.getString("name"), o.getString("text"), o.getString("createdAt"));
                            items.add(i);
                        }else {
                            ParseUser u= o.getParseUser("user_to");
                            u.fetchIfNeeded();
                            i = new MessageItem("to. ", u.getString("name"), o.getString("text"), o.getString("createdAt"));
                            items.add(i);
                        }
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
                recyclerview.setAdapter(new MessageAdapter(mContext, items));

            }
        });
        return view;
    }
}
