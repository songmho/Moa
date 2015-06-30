package com.team1.valueupapp;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by songmho on 2015-06-30.
 */
public class MainRecyclerAdpater extends RecyclerView.Adapter<MainRecyclerAdpater.ViewHolder> {
    Context context;
    List<MainRecyclerItem> items;
    int itemLayout;


    public MainRecyclerAdpater(Context context,List<MainRecyclerItem> items,int itemLayout) {
        this.context=context;
        this.items=items;
        this.itemLayout=itemLayout;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mainrecycler,viewGroup,false);
        return new ViewHolder(v,itemLayout);
       // return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        MainRecyclerItem item=items.get(i);
        viewHolder.itemView.setTag(item);
        viewHolder.name.setText(item.getName());
        viewHolder.app_name.setText(item.getApp_name());
        viewHolder.detail.setText(item.getDetail());
        viewHolder.star.setRating(item.getStar());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView app_name;
        TextView name;
        TextView detail;
        RatingBar star;
        CardView cardView;


        public ViewHolder(View itemView,int itemLayout) {
            super(itemView);
            app_name=(TextView)itemView.findViewById(R.id.app_name);
            name=(TextView)itemView.findViewById(R.id.name);
            star=(RatingBar)itemView.findViewById(R.id.star);
            detail=(TextView)itemView.findViewById(R.id.detail);
            cardView=(CardView)itemView.findViewById(R.id.cardview);
        }
    }
}
