package com.team1.valueupapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team1.valueupapp.R;
import com.team1.valueupapp.item.AlertItem;
import com.team1.valueupapp.item.MessageItem;

import java.util.ArrayList;

/**
 * Created by songmho on 2016-05-28.
 */
public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MessageItem> items;

    public MessageAdapter(Context mContext, ArrayList<MessageItem> items) {
        this.context=mContext;
        this.items=items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        return new Body(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageItem item = items.get(position);

        ((Body)holder).title_tag.setText(item.getStatus());
        ((Body)holder).title.setText(item.getTitle());
        ((Body)holder).body.setText(item.getBody());
        ((Body)holder).time.setText(item.getTime());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    class Body extends RecyclerView.ViewHolder{
        TextView title;
        TextView body;
        TextView time;
        TextView title_tag;
        public Body(View itemView) {
            super(itemView);
            title_tag = (TextView)itemView.findViewById(R.id.title_tag);
            title=(TextView)itemView.findViewById(R.id.title);
            body=(TextView)itemView.findViewById(R.id.body);
            time=(TextView)itemView.findViewById(R.id.time);
        }
    }
}


