package com.team1.valueupapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team1.valueupapp.R;
import com.team1.valueupapp.item.AlertItem;

import java.util.ArrayList;

/**
 * Created by songmho on 2016-05-08.
 */
public class AlertAdapter extends RecyclerView.Adapter {
    ArrayList<AlertItem> items;
    Context context;

    public AlertAdapter(Context context, ArrayList<AlertItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert,parent,false);
        return new Alert_body(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AlertItem item = items.get(position);
        ((Alert_body)holder).text.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    class Alert_body extends RecyclerView.ViewHolder{

        TextView text;

        public Alert_body(View itemView) {
            super(itemView);
            text = (TextView)itemView.findViewById(R.id.text);
        }
    }
}

