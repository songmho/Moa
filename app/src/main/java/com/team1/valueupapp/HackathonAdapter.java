package com.team1.valueupapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by eugene on 2015-08-02.
 */
public class HackathonAdapter extends RecyclerView.Adapter {

    private List<HackathonItem> dataItems;

    public HackathonAdapter(List<HackathonItem> dataItems) {

        this.dataItems = dataItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View layoutView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_hackathon, null);
        return new MyViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        HackathonItem dataItem = dataItems.get(position);
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        myViewHolder.hkt_image.setBackgroundColor(dataItem.getImage());
        myViewHolder.hkt_name.setText(dataItem.getName());
    }

    @Override
    public int getItemCount() {

        return dataItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView hkt_name;
        public ImageView hkt_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            hkt_name = (TextView) itemView.findViewById(R.id.hkt_name);
            hkt_image = (ImageView) itemView.findViewById(R.id.hkt_image);

        }
    }
}